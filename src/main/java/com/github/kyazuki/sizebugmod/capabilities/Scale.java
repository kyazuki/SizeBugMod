package com.github.kyazuki.sizebugmod.capabilities;

public class Scale implements IScale {
  private float scale;
  private float height;

  public Scale() {
    this.scale = 1.0f;
    this.height = 1.0f;
  }

  @Override
  public void setScale(float value) {
    scale = value;
  }

  @Override
  public float getScale() {
    return scale;
  }

  @Override
  public void setHeight(float value) {
    height = value;
  }

  @Override
  public float getHeight() {
    return height;
  }

  @Override
  public void copy(IScale cap) {
    scale = cap.getScale();
    height = cap.getHeight();
  }
}