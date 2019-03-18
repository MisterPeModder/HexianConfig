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

package com.misterpemodder.hexianconfig.api;

import static org.apiguardian.api.API.Status.MAINTAINED;
import com.misterpemodder.hexianconfig.api.annotation.ConfigValue;
import org.apiguardian.api.API;

/**
 * Represnents a config value.
 */
@API(status = MAINTAINED, since = "0.1.0")
public interface ConfigEntry<T> {
  /**
   * @return The value key set in {@link ConfigValue#key()}
   */
  String getKey();

  /**
   * @return The value type.
   */
  Class<T> getType();

  /**
  * @return The comments set in {@link ConfigValue#comments()}
  */
  String[] getComments();

  /**
   * @return the current value.
   */
  T getValue();

  /**
   * Sets the value. changes will affect the underlying field.
   * 
   * @param value The value.
   */
  void setValue(T value);
}
