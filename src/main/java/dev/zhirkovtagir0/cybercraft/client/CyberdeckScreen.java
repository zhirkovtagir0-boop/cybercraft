package dev.zhirkovtagir0.cybercraft.client;

import dev.zhirkovtagir0.cybercraft.CybercraftNetwork;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public final class CyberdeckScreen extends Screen {
    public CyberdeckScreen() {
        super(Component.translatable("gui.cybercraft.cyberdeck.title"));
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 105;
        int top = this.height / 2 - 44;
        addRenderableWidget(Button.builder(Component.translatable("gui.cybercraft.quickhack.ping"),
                button -> select(0)).bounds(left, top, 210, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cybercraft.quickhack.cripple"),
                button -> select(1)).bounds(left, top + 25, 210, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cybercraft.quickhack.shock"),
                button -> select(2)).bounds(left, top + 50, 210, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(left, top + 80, 210, 20).build());
    }

    private void select(int hack) {
        ClientPacketDistributor.sendToServer(new CybercraftNetwork.SelectQuickhack(hack));
        onClose();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int left = this.width / 2 - 125;
        int top = this.height / 2 - 75;
        graphics.fill(left, top, left + 250, top + 175, 0xE5101820);
        graphics.fill(left, top, left + 250, top + 2, 0xFF35F2C1);
        graphics.fill(left, top + 173, left + 250, top + 175, 0xFF35F2C1);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.centeredText(this.font, this.title, this.width / 2, this.height / 2 - 63, 0xFF35F2C1);
        graphics.centeredText(this.font, Component.translatable("gui.cybercraft.cyberdeck.subtitle"),
                this.width / 2, this.height / 2 - 46, 0xFFB8C7D9);
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
