package com.github.kyazuki.sizebugmod.events;

import com.github.kyazuki.sizebugmod.SizeBugMod;
import com.github.kyazuki.sizebugmod.SizeBugModConfig;
import com.github.kyazuki.sizebugmod.capabilities.IScale;
import com.github.kyazuki.sizebugmod.capabilities.ScaleProvider;
import com.github.kyazuki.sizebugmod.network.CapabilityPacket;
import com.github.kyazuki.sizebugmod.network.PacketHandler;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = SizeBugMod.MODID)
public class CommonEventHandler {
  public static final ResourceLocation SCALE_CAP_RESOURCE = new ResourceLocation(SizeBugMod.MODID, "capabilities");
  public static final UUID ReachDistance = UUID.fromString("cd481079-3b45-7d93-999d-2fbca3e2a8c7");
  private static final EntitySize DEFAULT_STANDING_SIZE = EntitySize.flexible(0.6F, 1.8F);
  private static final Map<Pose, EntitySize> DEFAULT_SIZE_BY_POSE = ImmutableMap.<Pose, EntitySize>builder().put(Pose.STANDING, PlayerEntity.STANDING_SIZE).put(Pose.SLEEPING, EntitySize.fixed(0.2F, 0.2F)).put(Pose.FALL_FLYING, EntitySize.flexible(0.6F, 0.6F)).put(Pose.SWIMMING, EntitySize.flexible(0.6F, 0.6F)).put(Pose.SPIN_ATTACK, EntitySize.flexible(0.6F, 0.6F)).put(Pose.CROUCHING, EntitySize.flexible(0.6F, 1.5F)).put(Pose.DYING, EntitySize.fixed(0.2F, 0.2F)).build();

  // Utils

  public static IScale getCap(PlayerEntity player) {
    return player.getCapability(ScaleProvider.SCALE_CAP).orElseThrow(IllegalArgumentException::new);
  }

  public static EntitySize getScaledPlayerSize(PlayerEntity player, Pose poseIn) {
    if (SizeBugModConfig.change_hitbox) {
      float scale, height;
      try {
        IScale cap = getCap(player);
        scale = cap.getScale();
        height = cap.getHeight();
      } catch (IllegalArgumentException e) {
        scale = 1.0f;
        height = 1.0f;
      } catch (Exception e) {
        throw e;
      }

      switch (poseIn) {
        case STANDING:
          return EntitySize.flexible(0.6F * scale, 1.8F * height);
        case SLEEPING:
          return EntitySize.fixed(0.2F, 0.2F);
        case CROUCHING:
          return EntitySize.flexible(0.6F * scale, 1.5F * height);
        case DYING:
          return EntitySize.flexible(0.2F, 0.2F);
        default:
          return EntitySize.flexible(0.6F, 0.6F);
      }
    }
    return DEFAULT_SIZE_BY_POSE.getOrDefault(poseIn, DEFAULT_STANDING_SIZE);
  }

  public static float getScaledStandingEyeHeight(PlayerEntity player, Pose poseIn) {
    float height;
    try {
      height = getCap(player).getHeight();
    } catch (IllegalArgumentException e) {
      height = 1.0f;
    } catch (Exception e) {
      throw e;
    }

    switch (poseIn) {
      case SWIMMING:
      case FALL_FLYING:
      case SPIN_ATTACK:
        return 0.4F;
      case CROUCHING:
        return 1.27F * height;
      default:
        return 1.62F * height;
    }
  }

  // Server Only

  @SubscribeEvent
  public static void time(TickEvent.PlayerTickEvent event) {
    if (event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END) {
      if (event.player.getEntityWorld().getGameTime() % (SizeBugModConfig.timer * 20) == 0) {
        PlayerEntity player = event.player;
        IScale cap = getCap(player);
        float scale = new Random().nextFloat() * (float) (SizeBugModConfig.max_width);
        float height = new Random().nextFloat() * (float) (SizeBugModConfig.max_height);
        cap.setScale(scale);
        cap.setHeight(height);
        ModifiableAttributeInstance player_reach_distance = player.getAttribute(ForgeMod.REACH_DISTANCE.get());
        player_reach_distance.removeModifier(ReachDistance);
        player_reach_distance.applyNonPersistentModifier(new AttributeModifier(ReachDistance, "TallReachDistance", scale - 1.0f, AttributeModifier.Operation.MULTIPLY_TOTAL));
        MinecraftForge.EVENT_BUS.post(new SizeBugModEvents.UpdatePlayerSizeEvent(player));
        PacketHandler.sendToTrackersAndSelf(new CapabilityPacket(player.getEntityId(), scale, height), player);
      }
    }
  }

  @SubscribeEvent
  public static void onLoggedInPlayer(PlayerEvent.PlayerLoggedInEvent event) {
    PlayerEntity player = event.getPlayer();
    IScale cap = getCap(player);
    PacketHandler.sendTo(new CapabilityPacket(player.getEntityId(), cap.getScale(), cap.getHeight()), player);
  }

  @SubscribeEvent
  public static void onChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
    PlayerEntity player = event.getPlayer();
    IScale cap = getCap(player);
    PacketHandler.sendTo(new CapabilityPacket(player.getEntityId(), cap.getScale(), cap.getHeight()), player);
  }

  @SubscribeEvent
  public static void onRespawnPlayer(PlayerEvent.PlayerRespawnEvent event) {
    PlayerEntity player = event.getPlayer();
    IScale cap = getCap(player);
    PacketHandler.sendTo(new CapabilityPacket(player.getEntityId(), cap.getScale(), cap.getHeight()), player);
  }

  @SubscribeEvent
  public static void onStartTracking(PlayerEvent.StartTracking event) {
    if (!(event.getTarget() instanceof PlayerEntity)) return;

    PlayerEntity trackedPlayer = (PlayerEntity) event.getTarget();
    IScale cap = getCap(trackedPlayer);
    PacketHandler.sendTo(new CapabilityPacket(trackedPlayer.getEntityId(), cap.getScale(), cap.getHeight()), event.getPlayer());
  }

  @SubscribeEvent
  public static void onClonePlayer(PlayerEvent.Clone event) {
    PlayerEntity newPlayer = event.getPlayer();
    PlayerEntity oldPlayer = event.getOriginal();
    IScale newCap = getCap(newPlayer);
    IScale oldCap = getCap(oldPlayer);
    newCap.copy(oldCap);
  }

  // Server & Client

  @SubscribeEvent
  public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
    if (!(event.getObject() instanceof PlayerEntity)) return;

    event.addCapability(SCALE_CAP_RESOURCE, new ScaleProvider());
  }

  @SubscribeEvent
  public static void setPlayerHitbox(SizeBugModEvents.UpdatePlayerSizeEvent event) {
    if (SizeBugModConfig.change_hitbox)
      event.getPlayer().recalculateSize();
  }
}
