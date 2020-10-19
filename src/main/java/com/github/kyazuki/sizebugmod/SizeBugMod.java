package com.github.kyazuki.sizebugmod;

import com.github.kyazuki.sizebugmod.SizeBugModConfig;
import com.github.kyazuki.sizebugmod.capabilities.IScale;
import com.github.kyazuki.sizebugmod.capabilities.Scale;
import com.github.kyazuki.sizebugmod.capabilities.ScaleStorage;
import com.github.kyazuki.sizebugmod.network.PacketHandler;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SizeBugMod.MODID)
public class SizeBugMod {
  public static final String MODID = "sizebugmod";
  public static final Logger LOGGER = LogManager.getLogger(MODID);

  public SizeBugMod() {
    LOGGER.debug("SizeBugMod loaded!");
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SizeBugModConfig.COMMON_SPEC);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(SizeBugMod::onFMLCommonSetup);
    PacketHandler.register();
  }

  public static void onFMLCommonSetup(FMLCommonSetupEvent event) {
    CapabilityManager.INSTANCE.register(IScale.class, new ScaleStorage(), () -> new Scale());
  }
}
