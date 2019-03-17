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

package com.misterpemodder.hexianconfig.impl;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import com.misterpemodder.hexianconfig.api.ConfigEntry;
import com.misterpemodder.hexianconfig.api.ConfigException;
import com.misterpemodder.hexianconfig.api.ConfigHandler;
import com.misterpemodder.hexianconfig.api.ConfigLoader;
import com.misterpemodder.hexianconfig.api.annotation.ConfigFile;
import com.misterpemodder.hexianconfig.api.annotation.ConfigValue;

public class ConfigHandlerImpl implements ConfigHandler {
  private final File configDirectory;
  private final ConfigLoader loader;

  public ConfigHandlerImpl(File configDirectory, ConfigLoader loader) {
    this.configDirectory = configDirectory;
    this.loader = loader;
  }


  public <T> T load(Class<T> type) throws ConfigException {
    try {
      ConfigFile configFile = type.getAnnotation(ConfigFile.class);
      if (configFile == null)
        throw new RuntimeException(
            "class " + type.getName() + "is lacking the @ConfigFile annotation");
      T config = type.newInstance();
      this.loader.load(getConfigData(config),
          new File(this.configDirectory, configFile.value() + this.loader.getFileExtension()));
      return config;
    } catch (IllegalAccessException | InstantiationException e) {
      throw new ConfigException("Couldn't load config file", e);
    }
  }

  public void store(Object config) throws ConfigException {
    try {
      Class<?> type = config.getClass();
      ConfigFile configFile = type.getAnnotation(ConfigFile.class);
      if (configFile == null)
        throw new RuntimeException(
            "class " + type.getName() + "is lacking the @ConfigFile annotation");
      this.loader.store(getConfigData(config),
          new File(this.configDirectory, configFile.value() + this.loader.getFileExtension()),
          configFile.comments());
    } catch (IllegalAccessException e) {
      throw new ConfigException("Couldn't load config file", e);
    }
  }

  private Map<String, ConfigEntry<?>> getConfigData(Object config) throws IllegalAccessException {
    Map<String, ConfigEntry<?>> entries = new HashMap<>();
    for (Field field : config.getClass().getDeclaredFields()) {
      ConfigValue value = field.getAnnotation(ConfigValue.class);
      if (value == null)
        continue;
      entries.put(value.key(),
          ConfigEntryImpl.create(field.getType(), field.get(config), value.comments()));
    }
    return entries;
  }
}
