package com.github.kyazuki.sizebugmod.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class SizeBugModEvents {
  public static class UpdatePlayerSizeEvent extends PlayerEvent {
    public UpdatePlayerSizeEvent(PlayerEntity player) {
      super(player);
    }
  }
}
