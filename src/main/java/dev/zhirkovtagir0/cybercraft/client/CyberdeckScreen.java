package dev.zhirkovtagir0.cybercraft.client;

import dev.zhirkovtagir0.cybercraft.CybercraftNetwork;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CyberdeckScreen extends Screen {
    public CyberdeckScreen() {
        super(Component.literal("CYBERDECK // QUICKHACKS"));
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 105;
        int top = this.height / 2 - 44;
        addRenderableWidget(Button.builder(Component.literal("PING  //  Scan threats"), button -> select(0))
                .bounds(left, top, 210, 20).build());
        addRenderableWidget(Button.builder(Component.literal("CRIPPLE  //  Slow target"), button -> select(1))
                .bounds(left, top + 25, 210, 20).build());
        addRenderableWidget(Button.builder(Component.literal("SYSTEM SHOCK  //  Damage"), button -> select(2))
                .bounds(left, top + 50, 210, 20).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.done"), button -> onClose())
                .bounds(left, top + 80, 210, 20).build());
    }

    private void select(int hack) {
        PacketDistributor.sendToServer(new CybercraftNetwork.SelectQuickhack(hack));
        onClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        int left = this.width / 2 - 125;
        int top = this.height / 2 - 75;
        graphics.fill(left, top, left + 250, top + 175, 0xE5101820);
        graphics.fill(left, top, left + 250, top + 2, 0xFF35F2C1);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, top + 12, 0xFF35F2C1);
        graphics.drawCenteredString(this.font, "SELECT ONE QUICKHACK", this.width / 2, top + 29, 0xFFB8C7D9);
        super.render(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
