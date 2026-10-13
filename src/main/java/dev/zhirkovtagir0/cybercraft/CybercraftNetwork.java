package dev.zhirkovtagir0.cybercraft;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = Cybercraft.MOD_ID)
public final class CybercraftNetwork {
    private static final String SELECTED_QUICKHACK_KEY = "cybercraft_selected_quickhack";

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
            Player player = context.player();
            if (payload.hack() < 0 || payload.hack() > 2) {
                return;
            }

            // Treat the client as untrusted: the quickhack console can only change
            // selection while the player actually carries a Cyberdeck.
            boolean hasCyberdeck = player.getMainHandItem().is(ModItems.CYBERDECK.get())
                    || player.getOffhandItem().is(ModItems.CYBERDECK.get());
            if (!hasCyberdeck) {
                player.sendSystemMessage(Component.translatable("message.cybercraft.cyberdeck_required")
                        .withStyle(ChatFormatting.RED));
                return;
            }

            player.getPersistentData().putInt(SELECTED_QUICKHACK_KEY, payload.hack());
            String hackName = switch (payload.hack()) {
                case 0 -> "PING";
                case 1 -> "CRIPPLE MOVEMENT";
                default -> "SYSTEM SHOCK";
            };
            player.sendSystemMessage(Component.translatable("message.cybercraft.quickhack_selected", hackName)
                    .withStyle(ChatFormatting.GREEN));
        });
    }
}
