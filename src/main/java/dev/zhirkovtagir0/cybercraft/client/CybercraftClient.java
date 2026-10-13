package dev.zhirkovtagir0.cybercraft.client;

import dev.zhirkovtagir0.cybercraft.Cybercraft;
import dev.zhirkovtagir0.cybercraft.ModItems;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Cybercraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CybercraftClient {
    private static final KeyMapping OPEN_CYBERDECK = new KeyMapping(
            "key.cybercraft.open_cyberdeck", GLFW.GLFW_KEY_G, "key.categories.cybercraft");

    private CybercraftClient() {}

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_CYBERDECK);
        NeoForge.EVENT_BUS.addListener(CybercraftClient::onClientTick);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) return;
        while (OPEN_CYBERDECK.consumeClick()) {
            LocalPlayer player = minecraft.player;
            ItemStack main = player.getMainHandItem();
            ItemStack off = player.getOffhandItem();
            if (main.is(ModItems.CYBERDECK.get()) || off.is(ModItems.CYBERDECK.get())) {
                minecraft.setScreen(new CyberdeckScreen());
            }
        }
    }
}
