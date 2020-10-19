package com.github.kyazuki.sizebugmod.capabilities;

public interface IScale {
  void setScale(float value);

  float getScale();

  void setHeight(float value);

  float getHeight();

  void copy(IScale cap);
}
