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

    private final Ability ability;

    public CyberwareItem(Properties properties, Ability ability) {
        super(properties);
        this.ability = ability;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide()) {
            switch (ability) {
                case SANDEVISTAN -> {
                    player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                            net.minecraft.world.effect.MobEffects.MOVEMENT_SPEED, 100, 2));
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
            case SANDEVISTAN -> tooltip.add(Component.literal("OS implant • speed burst • 12s cooldown").withStyle(ChatFormatting.AQUA));
            case MANTIS_BLADES -> tooltip.add(Component.literal("Arms implant • focused melee strike").withStyle(ChatFormatting.RED));
            case MONOWIRE -> tooltip.add(Component.literal("Arms implant • wide melee sweep").withStyle(ChatFormatting.LIGHT_PURPLE));
            case CYBERDECK -> tooltip.add(Component.literal("Quickhack • reveal and slow nearby hostiles").withStyle(ChatFormatting.GREEN));
        }
        tooltip.add(Component.literal("Prototype: activate with right-click").withStyle(ChatFormatting.GRAY));
    }
}
