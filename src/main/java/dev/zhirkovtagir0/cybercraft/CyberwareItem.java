package dev.zhirkovtagir0.cybercraft;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class CyberwareItem extends Item {
    public enum Ability {
        SANDEVISTAN, MANTIS_BLADES, MONOWIRE, CYBERDECK
    }

    private static final String OS_SLOT = "cybercraft_implant_os";
    private static final String ARMS_SLOT = "cybercraft_implant_arms";
    private static final String STRAIN_KEY = "cybercraft_neural_strain";
    private static final String STRAIN_TIME_KEY = "cybercraft_neural_strain_time";
    private static final int CYBERPSYCHOSIS_THRESHOLD = 12;

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

    private int strainCost() {
        return switch (ability) {
            case SANDEVISTAN -> 3;
            case MANTIS_BLADES, MONOWIRE -> 2;
            case CYBERDECK -> 1;
        };
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Sneak + right-click installs or removes the implant in its body slot.
        if (player.isShiftKeyDown()) {
            if (!level.isClientSide()) {
                var data = player.getPersistentData();
                String current = data.getString(slotKey()).orElse("");
                if (current.equals(abilityId())) {
                    data.remove(slotKey());
                    notifyPlayer(player, slotName() + " // IMPLANT REMOVED", ChatFormatting.YELLOW);
                } else {
                    data.putString(slotKey(), abilityId());
                    notifyPlayer(player, slotName() + " // " + abilityId().toUpperCase() + " INSTALLED",
                            ChatFormatting.AQUA);
                }
            }
            return InteractionResult.SUCCESS;
        }

        if (!level.isClientSide()) {
            String installed = player.getPersistentData().getString(slotKey()).orElse("");
            if (!installed.equals(abilityId())) {
                notifyPlayer(player, "IMPLANT NOT INSTALLED // SNEAK + RIGHT-CLICK TO INSTALL",
                        ChatFormatting.RED);
                return InteractionResult.FAIL;
            }
        }

        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide()) {
            if (addNeuralStrain(player, level)) {
                player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
                player.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 100, 1));
                notifyPlayer(player, "CYBERPSYCHOSIS // NEURAL OVERLOAD", ChatFormatting.DARK_RED);
            }

            switch (ability) {
                case SANDEVISTAN -> {
                    player.addEffect(new MobEffectInstance(MobEffects.SPEED, 100, 2));
                    player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 60, 0));
                    player.getCooldowns().addCooldown(stack, 240);
                    notifyPlayer(player, "SANDEVISTAN // SYSTEM ONLINE", ChatFormatting.AQUA);
                }
                case MANTIS_BLADES -> {
                    int hits = strike(level, player, 2.8, 7.0f, 1.4);
                    player.getCooldowns().addCooldown(stack, 14);
                    notifyPlayer(player, "MANTIS BLADES // " + hits + " HIT(S)", ChatFormatting.RED);
                }
                case MONOWIRE -> {
                    int hits = strike(level, player, 4.5, 5.0f, 3.0);
                    player.getCooldowns().addCooldown(stack, 24);
                    notifyPlayer(player, "MONOWIRE // " + hits + " TARGET(S) CUT", ChatFormatting.LIGHT_PURPLE);
                }
                case CYBERDECK -> {
                    int hacked = 0;
                    for (LivingEntity target : level.getEntitiesOfClass(LivingEntity.class,
                            player.getBoundingBox().inflate(12.0),
                            entity -> entity instanceof Monster && entity.isAlive())) {
                        target.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
                        target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 60, 1));
                        hacked++;
                    }
                    player.getCooldowns().addCooldown(stack, 160);
                    notifyPlayer(player, "QUICKHACK // " + hacked + " TARGET(S)", ChatFormatting.GREEN);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * Tracks cumulative neural load. Overusing implants triggers a brief
     * cyberpsychosis episode and partially resets accumulated strain.
     */
    private boolean addNeuralStrain(Player player, Level level) {
        var data = player.getPersistentData();
        long now = level.getGameTime();
        long lastTime = data.getLong(STRAIN_TIME_KEY).orElse(now);
        int strain = data.getInt(STRAIN_KEY).orElse(0);

        // One strain point naturally dissipates every 30 seconds without implant use.
        int recovered = (int) Math.min(Integer.MAX_VALUE, Math.max(0L, now - lastTime) / 600L);
        strain = Math.max(0, strain - recovered) + strainCost();
        data.putLong(STRAIN_TIME_KEY, now);

        if (strain >= CYBERPSYCHOSIS_THRESHOLD) {
            data.putInt(STRAIN_KEY, 4);
            return true;
        }
        data.putInt(STRAIN_KEY, strain);
        return false;
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
                target.hurt(level.damageSources().playerAttack(player), damage);
                hits++;
            }
        }
        return hits;
    }

    private static void notifyPlayer(Player player, String message, ChatFormatting color) {
        player.sendSystemMessage(Component.literal(message).withStyle(color));
    }
}
