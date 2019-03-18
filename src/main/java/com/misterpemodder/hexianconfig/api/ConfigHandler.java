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
import java.nio.file.Path;
import java.util.Collection;
import javax.annotation.Nullable;
import com.misterpemodder.hexianconfig.impl.ConfigHandlerImpl;
import org.apiguardian.api.API;

/**
 * Loads and saves config object using a {@link ConfigLoader}.
 */
@API(status = MAINTAINED, since = "0.1.0")
public interface ConfigHandler<C> {
  /**
   * @return The name of the config file.
   */
  String getName();

  /**
   * @return The path of the config file.
   */
  Path getPath();

  /**
   * Provides a way to get entries without using the configuration object.
   * 
   * @param key The entry key.
   * 
   * @return The entry at {@code key}, null if not found.
   */
  @Nullable
  ConfigEntry<?> getEntry(String key);

  /**
  * Provides a way to get entries keys without using the configuration object.
  * 
  * @return The keys.
  */
  Collection<String> getKeys();

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
   * @param configFire      the class annotated with {@code @ConfigFile}.
   * @param configDirectory The directory of the config file.
   * @param loader          The loader that will be used to parse/store config files.
   * 
   * @return The config handler.
   */
  public static <C> ConfigHandler<C> create(C configObject, Path configDirectory,
      ConfigLoader loader) throws ConfigException {
    return new ConfigHandlerImpl<>(configObject, configDirectory, loader);
  }
}
