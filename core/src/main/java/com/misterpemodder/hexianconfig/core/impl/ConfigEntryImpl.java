/*
 * MIT License
 *
 * Copyright (c) 2019 MisterPeModder (Yanis Guaye)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.misterpemodder.hexianconfig.core.impl;

import java.lang.reflect.Field;
import com.misterpemodder.hexianconfig.core.api.ConfigEntry;
import com.misterpemodder.hexianconfig.core.api.annotation.ConfigValue;

public class ConfigEntryImpl<T> implements ConfigEntry<T> {
  private final String key;
  private final Class<T> type;
  private final String[] comments;

  private final Field field;
  private final Object owner;

  @SuppressWarnings("unchecked")
  protected ConfigEntryImpl(ConfigValue annotation, Field field, Object owner) {
    this.key = annotation.key();
    this.type = (Class<T>) field.getType();
    this.comments = annotation.comments();
    this.field = field;
    this.field.setAccessible(true);
    this.owner = owner;
  }

  @Override
  public String getKey() {
    return this.key;
  }

  @Override
  public Class<T> getType() {
    return this.type;
  }

  @Override
  public String[] getComments() {
    return this.comments;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T getValue() {
    try {
      return (T) this.field.get(this.owner);
    } catch (IllegalAccessException | IllegalArgumentException e) {
      return null;
    }
  }

  @Override
  public void setValue(T value) {
    try {
      this.field.set(this.owner, value);
    } catch (IllegalAccessException e) {
    }
  }
  /*
  @SuppressWarnings({"unchecked", "rawtypes"})
  public static ConfigEntry<?> create(Class<?> type, Object value, String[] comments) {
    return new ConfigEntryImpl(type, value, comments);
  }*/
}
