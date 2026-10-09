package dev.zhirkovtagir0.cybercraft;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(Cybercraft.MOD_ID)
public final class Cybercraft {
    public static final String MOD_ID = "cybercraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Cybercraft(IEventBus modBus) {
        ModItems.ITEMS.register(modBus);
        LOGGER.info("Cybercraft: Night City starting up");
    }
}
