package dev.zhirkovtagir0.cybercraft;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Cybercraft.MOD_ID);

    public static final java.util.function.Supplier<Item> SANDEVISTAN =
            ITEMS.register("sandevistan", () -> new CyberwareItem(
                    new Item.Properties().stacksTo(1), CyberwareItem.Ability.SANDEVISTAN));
    public static final java.util.function.Supplier<Item> MANTIS_BLADES =
            ITEMS.register("mantis_blades", () -> new CyberwareItem(
                    new Item.Properties().stacksTo(1), CyberwareItem.Ability.MANTIS_BLADES));
    public static final java.util.function.Supplier<Item> MONOWIRE =
            ITEMS.register("monowire", () -> new CyberwareItem(
                    new Item.Properties().stacksTo(1), CyberwareItem.Ability.MONOWIRE));
    public static final java.util.function.Supplier<Item> CYBERDECK =
            ITEMS.register("cyberdeck", () -> new CyberwareItem(
                    new Item.Properties().stacksTo(1), CyberwareItem.Ability.CYBERDECK));
    public static final java.util.function.Supplier<Item> IMPLANT_CHIP =
            ITEMS.register("implant_chip", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final java.util.function.Supplier<Item> NEURAL_PROCESSOR =
            ITEMS.register("neural_processor", () -> new Item(new Item.Properties().stacksTo(16)));
    public static final java.util.function.Supplier<Item> CAPACITY_SHARD =
            ITEMS.register("capacity_shard", () -> new CapacityShardItem(new Item.Properties().stacksTo(16)));
}
