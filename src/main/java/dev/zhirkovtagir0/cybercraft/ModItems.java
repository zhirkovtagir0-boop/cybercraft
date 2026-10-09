package dev.zhirkovtagir0.cybercraft;

import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private ModItems() {}

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(Cybercraft.MOD_ID);

    // registerItem supplies the registry ResourceKey to Item.Properties. Using
    // ITEMS.register(name, () -> new Item(new Item.Properties())) leaves the
    // Item without an id on newer Minecraft versions and crashes during mod load.
    public static final java.util.function.Supplier<Item> SANDEVISTAN =
            ITEMS.registerItem("sandevistan", props -> new CyberwareItem(
                    props.stacksTo(1), CyberwareItem.Ability.SANDEVISTAN));
    public static final java.util.function.Supplier<Item> MANTIS_BLADES =
            ITEMS.registerItem("mantis_blades", props -> new CyberwareItem(
                    props.stacksTo(1), CyberwareItem.Ability.MANTIS_BLADES));
    public static final java.util.function.Supplier<Item> MONOWIRE =
            ITEMS.registerItem("monowire", props -> new CyberwareItem(
                    props.stacksTo(1), CyberwareItem.Ability.MONOWIRE));
    public static final java.util.function.Supplier<Item> CYBERDECK =
            ITEMS.registerItem("cyberdeck", props -> new CyberwareItem(
                    props.stacksTo(1), CyberwareItem.Ability.CYBERDECK));
    public static final java.util.function.Supplier<Item> IMPLANT_CHIP =
            ITEMS.registerItem("implant_chip", props -> new Item(props.stacksTo(16)));
    public static final java.util.function.Supplier<Item> NEURAL_PROCESSOR =
            ITEMS.registerItem("neural_processor", props -> new Item(props.stacksTo(16)));
    public static final java.util.function.Supplier<Item> CAPACITY_SHARD =
            ITEMS.registerItem("capacity_shard", props -> new CapacityShardItem(props.stacksTo(16)));
}
