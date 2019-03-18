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
import com.misterpemodder.hexianconfig.api.annotation.ConfigFile;
import com.misterpemodder.hexianconfig.impl.ConfigHandlerImpl;

/**
 * Loads and saves config object using a {@link ConfigLoader}.
 */
public interface ConfigHandler<C> {
  String getName();

  File getFile();

  /**
   * Loads a config file from disc.
   */
  void load() throws ConfigException;

  /**
   * Stores a config file to disc.
   */
  void store() throws ConfigException;

  /**
   * Creates a {@link ConfigHandler}.
   * 
   * @param configFire      the class annotated with {@link ConfigFile}. Must have a contructor with
   *                        no parameters.
   * @param configDirectory The root directory for config files.
   * @param loader          The loader that will be used to parse/store config files.
   * 
   * @return The config handler.
   */
  public static <C> ConfigHandler<C> create(C configObject, File configDirectory,
      ConfigLoader loader) throws ConfigException {
    return new ConfigHandlerImpl<>(configObject, configDirectory, loader);
  }
}
