package dev.zhirkovtagir0.cybercraft;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = Cybercraft.MOD_ID)
public final class CybercraftNetwork {
    private CybercraftNetwork() {}

    public record SelectQuickhack(int hack) implements CustomPacketPayload {
        public static final Type<SelectQuickhack> TYPE =
                new Type<>(Identifier.fromNamespaceAndPath(Cybercraft.MOD_ID, "select_quickhack"));
        public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, SelectQuickhack> STREAM_CODEC =
                StreamCodec.composite(ByteBufCodecs.VAR_INT, SelectQuickhack::hack, SelectQuickhack::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(SelectQuickhack.TYPE, SelectQuickhack.STREAM_CODEC,
                CybercraftNetwork::handleSelectQuickhack);
    }

    private static void handleSelectQuickhack(SelectQuickhack payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (payload.hack() < 0 || payload.hack() > 2) return;
            context.player().getPersistentData().putInt("cybercraft_selected_quickhack", payload.hack());
            context.player().sendSystemMessage(net.minecraft.network.chat.Component.literal(
                    "CYBERDECK // " + switch (payload.hack()) {
                        case 0 -> "PING";
                        case 1 -> "CRIPPLE MOVEMENT";
                        default -> "SYSTEM SHOCK";
                    } + " SELECTED").withStyle(net.minecraft.ChatFormatting.GREEN));
        });
    }
}
