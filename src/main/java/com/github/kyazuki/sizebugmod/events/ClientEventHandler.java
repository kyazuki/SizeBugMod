package com.github.kyazuki.sizebugmod.events;

import com.github.kyazuki.sizebugmod.SizeBugMod;
import com.github.kyazuki.sizebugmod.SizeBugModConfig;
import com.github.kyazuki.sizebugmod.capabilities.IScale;
import com.github.kyazuki.sizebugmod.capabilities.ScaleProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SizeBugMod.MODID, value = Dist.CLIENT)
public class ClientEventHandler {
  private static long tick = 0;

  @SubscribeEvent
  public static void setGameTime(TickEvent.PlayerTickEvent event) {
    if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END) {
      tick = event.player.getEntityWorld().getGameTime();
    }
  }

  @SubscribeEvent
  public static void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
    if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
      FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
      fontRenderer.drawStringWithShadow(new MatrixStack(), "Time: " + (SizeBugModConfig.timer - tick % (SizeBugModConfig.timer * 20) / 20), 0, 0, TextFormatting.WHITE.getColor());
    }
  }

  @SubscribeEvent
  public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
    IScale cap = event.getPlayer().getCapability(ScaleProvider.SCALE_CAP).orElseThrow(IllegalArgumentException::new);
    float scale = cap.getScale();
    float height = cap.getHeight();
    event.getMatrixStack().push();
    event.getMatrixStack().scale(scale, height, scale);
  }

  @SubscribeEvent
  public static void onRenderPlayerPost(RenderPlayerEvent.Post event) {
    event.getMatrixStack().pop();
  }
}
