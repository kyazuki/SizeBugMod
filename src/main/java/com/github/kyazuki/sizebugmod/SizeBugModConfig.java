package com.github.kyazuki.sizebugmod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = SizeBugMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SizeBugModConfig {
  public static final CommonConfig COMMON;
  public static final ForgeConfigSpec COMMON_SPEC;

  static {
    final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
    COMMON_SPEC = specPair.getRight();
    COMMON = specPair.getLeft();
  }

  public static int timer;
  public static double max_width;
  public static double max_height;
  public static boolean change_eyeheight;
  public static boolean change_hitbox;

  @SubscribeEvent
  public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
    if (configEvent.getConfig().getSpec() == SizeBugModConfig.COMMON_SPEC) {
      bakeConfig();
    }
  }

  public static void bakeConfig() {
    timer = COMMON.timer.get();
    max_width = COMMON.max_width.get();
    max_height = COMMON.max_height.get();
    change_eyeheight = COMMON.change_eyeheight.get();
    change_hitbox = COMMON.change_hitbox.get();
  }

  public static class CommonConfig {
    public final ForgeConfigSpec.IntValue timer;
    public final ForgeConfigSpec.DoubleValue max_width;
    public final ForgeConfigSpec.DoubleValue max_height;
    public final ForgeConfigSpec.BooleanValue change_eyeheight;
    public final ForgeConfigSpec.BooleanValue change_hitbox;

    public CommonConfig(ForgeConfigSpec.Builder builder) {
      builder.push("SizeBugMod Config");
      timer = builder
          .comment("Interval of scale change.")
          .translation(SizeBugMod.MODID + ".config." + "timer")
          .defineInRange("timer", 10, 1, 1000);
      max_width = builder
              .comment("Max width scale.")
              .translation(SizeBugMod.MODID + ".config" + "max_width")
              .defineInRange("max_width", 10.0d, 0.0d, 1000.0d);
      max_height = builder
          .comment("Max height scale.")
          .translation(SizeBugMod.MODID + ".config" + "max_height")
          .defineInRange("max_height", 10.0d, 0.0d, 1000.0d);
      change_eyeheight = builder
              .comment("Whether Player's eyeheight is changed.")
              .translation(SizeBugMod.MODID + ".config" + "change_eyeheight")
              .define("change_eyeheight", true);
      change_hitbox = builder
              .comment("Whether Player's hitbox is changed.")
              .translation(SizeBugMod.MODID + ".config" + "change_hitbox")
              .define("change_hitbox", true);
      builder.pop();
    }
  }
}
