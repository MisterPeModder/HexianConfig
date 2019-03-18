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

import java.nio.file.Path;
import java.util.Map;
import com.misterpemodder.hexianconfig.impl.PropertiesConfigLoader;

/**
 * Handles the serialization and deserialization of configuration values in a specific format.
 */
public interface ConfigLoader {
  /**
   * Stores the given entries.
   * 
   * @param entries      The entries to store.
   * @param path         The location of the config file.
   * @param fileComments Some comments about this config file.
   */
  void store(Map<String, ConfigEntry<?>> entries, Path path, String[] fileComments)
      throws ConfigException;

  /**
   * Loads entries into the given map.
   * 
   * @param entries Where the parsed entries should be put.
   * @param path    The location of the config file.
   */
  void load(Map<String, ConfigEntry<?>> entries, Path path) throws ConfigException;

  /**
   * @return The file extension used by this loader, dot '.' included.
   */
  String getFileExtension();

  /**
   * @return A {@link ConfigLoader} that can read/write java <code>.properties</code> files.
   */
  static ConfigLoader propertiesLoader() {
    return PropertiesConfigLoader.INSTANCE;
  }
}
