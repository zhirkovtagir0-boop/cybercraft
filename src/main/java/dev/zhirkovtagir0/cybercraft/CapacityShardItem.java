package dev.zhirkovtagir0.cybercraft;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class CapacityShardItem extends Item {
    private static final String CAPACITY_KEY = "cybercraft_energy_capacity_upgrades";
    private static final int MAX_UPGRADES = 5;

    public CapacityShardItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        var data = player.getPersistentData();
        int upgrades = data.getInt(CAPACITY_KEY).orElse(0);
        if (upgrades >= MAX_UPGRADES) {
            player.sendSystemMessage(Component.literal("CAPACITY ALREADY MAXED // 200 ENERGY")
                    .withStyle(ChatFormatting.YELLOW));
            return InteractionResult.FAIL;
        }

        upgrades++;
        data.putInt(CAPACITY_KEY, upgrades);
        stack.shrink(1);
        player.sendSystemMessage(Component.literal("CYBERWARE CAPACITY +20 // " + (100 + upgrades * 20) + " MAX ENERGY")
                .withStyle(ChatFormatting.AQUA));
        return InteractionResult.SUCCESS;
    }
}
