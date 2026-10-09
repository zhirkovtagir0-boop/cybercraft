package dev.zhirkovtagir0.cybercraft;

import java.util.List;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class CyberwareItem extends Item {
    public enum Ability {
        SANDEVISTAN, MANTIS_BLADES, MONOWIRE, CYBERDECK
    }

    private static final String OS_SLOT = "cybercraft_implant_os";
    private static final String ARMS_SLOT = "cybercraft_implant_arms";

    private final Ability ability;

    public CyberwareItem(Properties properties, Ability ability) {
        super(properties);
        this.ability = ability;
    }

    private String slotKey() {
        return switch (ability) {
            case SANDEVISTAN, CYBERDECK -> OS_SLOT;
            case MANTIS_BLADES, MONOWIRE -> ARMS_SLOT;
        };
    }

    private String slotName() {
        return slotKey().equals(OS_SLOT) ? "OPERATING SYSTEM" : "ARMS";
    }

    private String abilityId() {
        return switch (ability) {
            case SANDEVISTAN -> "sandevistan";
            case MANTIS_BLADES -> "mantis_blades";
            case MONOWIRE -> "monowire";
            case CYBERDECK -> "cyberdeck";
        };
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Sneak + right-click installs or removes the implant in its body slot.
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                var data = player.getPersistentData();
                String current = data.getString(slotKey());
                if (current.equals(abilityId())) {
                    data.remove(slotKey());
                    actionBar(player, slotName() + " // IMPLANT REMOVED", ChatFormatting.YELLOW);
                } else {
                    data.putString(slotKey(), abilityId());
                    actionBar(player, slotName() + " // " + abilityId().toUpperCase() + " INSTALLED",
                            ChatFormatting.AQUA);
                }
            }
            return InteractionResultHolder.success(stack);
        }

        if (!level.isClientSide()) {
            String installed = player.getPersistentData().getString(slotKey());
            if (!installed.equals(abilityId())) {
                actionBar(player, "IMPLANT NOT INSTALLED // SNEAK + RIGHT-CLICK TO INSTALL",
                        ChatFormatting.RED);
                return InteractionResultHolder.fail(stack);
            }
        }

        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide()) {
            switch (ability) {
                case SANDEVISTAN -> {
                    player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                            net.minecraft.world.effect.MobEffects.SPEED, 100, 2));
                    player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                            net.minecraft.world.effect.MobEffects.RESISTANCE, 60, 0));
                    player.getCooldowns().addCooldown(this, 240);
                    actionBar(player, "SANDEVISTAN // SYSTEM ONLINE", ChatFormatting.AQUA);
                }
                case MANTIS_BLADES -> {
                    int hits = strike(level, player, 2.8, 7.0f, 1.4);
                    player.getCooldowns().addCooldown(this, 14);
                    actionBar(player, "MANTIS BLADES // " + hits + " HIT(S)", ChatFormatting.RED);
                }
                case MONOWIRE -> {
                    int hits = strike(level, player, 4.5, 5.0f, 3.0);
                    player.getCooldowns().addCooldown(this, 24);
                    actionBar(player, "MONOWIRE // " + hits + " TARGET(S) CUT", ChatFormatting.LIGHT_PURPLE);
                }
                case CYBERDECK -> {
                    int hacked = 0;
                    for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class,
                            player.getBoundingBox().inflate(12.0),
                            entity -> entity instanceof Monster && entity.isAlive())) {
                        target.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                                net.minecraft.world.effect.MobEffects.GLOWING, 100, 0));
                        target.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                                net.minecraft.world.effect.MobEffects.SLOWNESS, 60, 1));
                        hacked++;
                    }
                    player.getCooldowns().addCooldown(this, 160);
                    actionBar(player, "QUICKHACK // " + hacked + " TARGET(S)", ChatFormatting.GREEN);
                }
            }
        }
        return InteractionResultHolder.success(stack);
    }

    private static int strike(Level level, Player player, double reach, float damage, double width) {
        Vec3 look = player.getLookAngle();
        Vec3 center = player.getEyePosition().add(look.scale(reach));
        AABB area = new AABB(center, center).inflate(width, 1.25, width);
        int hits = 0;
        for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class, area,
                entity -> entity != player && entity.isAlive() && player.hasLineOfSight(entity))) {
            Vec3 direction = target.getEyePosition().subtract(player.getEyePosition()).normalize();
            if (look.dot(direction) > 0.2) {
                if (target.hurt(level.damageSources().playerAttack(player), damage)) {
                    target.knockback(0.45, -look.x, -look.z);
                    hits++;
                }
            }
        }
        return hits;
    }

    private static void actionBar(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(Component.literal(message).withStyle(color), true);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        switch (ability) {
            case SANDEVISTAN -> tooltip.add(Component.literal("OS slot • speed burst • 12s cooldown").withStyle(ChatFormatting.AQUA));
            case MANTIS_BLADES -> tooltip.add(Component.literal("Arms slot • focused melee strike").withStyle(ChatFormatting.RED));
            case MONOWIRE -> tooltip.add(Component.literal("Arms slot • wide melee sweep").withStyle(ChatFormatting.LIGHT_PURPLE));
            case CYBERDECK -> tooltip.add(Component.literal("OS slot • reveal and slow nearby hostiles").withStyle(ChatFormatting.GREEN));
        }
        tooltip.add(Component.literal("Sneak + right-click: install/remove in slot").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("Right-click: use installed implant").withStyle(ChatFormatting.GRAY));
    }
}
