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

import java.io.File;
import com.misterpemodder.hexianconfig.impl.ConfigHandlerImpl;

/**
 * Loads and saves config object using a {@link ConfigLoader}.
 */
public interface ConfigHandler {
  /**
   * Loads a config file from disc.
   * 
   * @param type A class annotated with {@link ConfigFile}. must have a constructor with no
   *             parameters.
   * 
   * @return An instance of {@code type}.
   */
  <T> T load(Class<T> type) throws ConfigException;

  /**
   * Stores a config file to disc.
   * 
   * @param config An instance of a class annotated with {@link ConfigFile}.
   */
  void store(Object config) throws ConfigException;

  /**
   * Creates a {@link ConfigHandler}.
   * 
   * @param configDirectory The root directory for config files.
   * @param loader The loader that will be used to parse/store config files.
   */
  public static ConfigHandler create(File configDirectory, ConfigLoader loader) {
    return new ConfigHandlerImpl(configDirectory, loader);
  }
}
