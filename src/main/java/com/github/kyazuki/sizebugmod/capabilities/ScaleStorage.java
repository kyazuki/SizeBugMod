package com.github.kyazuki.sizebugmod.capabilities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class ScaleStorage implements Capability.IStorage<IScale> {
  @Override
  public INBT writeNBT(Capability<IScale> capability, IScale instance, Direction side) {
    CompoundNBT tag = new CompoundNBT();
    tag.putFloat("scale", instance.getScale());
    tag.putFloat("height", instance.getHeight());
    return tag;
  }

  @Override
  public void readNBT(Capability<IScale> capability, IScale instance, Direction side, INBT nbt) {
    CompoundNBT tag = (CompoundNBT) nbt;
    instance.setScale(tag.getFloat("scale"));
    instance.setHeight(tag.getFloat("height"));
  }
}
