package dev.zhirkovtagir0.cybercraft;

import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

@Mod(Cybercraft.MOD_ID)
public final class Cybercraft {
    public static final String MOD_ID = "cybercraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CYBERCRAFT_TAB =
            CREATIVE_TABS.register("cyberware", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.cybercraft"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.SANDEVISTAN.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SANDEVISTAN.get());
                        output.accept(ModItems.MANTIS_BLADES.get());
                        output.accept(ModItems.MONOWIRE.get());
                        output.accept(ModItems.CYBERDECK.get());
                        output.accept(ModItems.IMPLANT_CHIP.get());
                        output.accept(ModItems.NEURAL_PROCESSOR.get());
                        output.accept(ModItems.CAPACITY_SHARD.get());
                    })
                    .build());

    public Cybercraft(IEventBus modBus) {
        ModItems.ITEMS.register(modBus);
        CREATIVE_TABS.register(modBus);
        LOGGER.info("Cybercraft: Night City starting up");
    }
}
