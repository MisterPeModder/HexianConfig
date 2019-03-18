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
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import com.misterpemodder.hexianconfig.api.ConfigEntry;
import com.misterpemodder.hexianconfig.api.ConfigException;
import com.misterpemodder.hexianconfig.api.ConfigHandler;
import com.misterpemodder.hexianconfig.api.ConfigLoader;
import com.misterpemodder.hexianconfig.api.annotation.ConfigFile;
import com.misterpemodder.hexianconfig.api.annotation.ConfigValue;

public class ConfigHandlerImpl<C> implements ConfigHandler<C> {
  private final File configDirectory;
  private final ConfigLoader loader;
  private final C configObject;
  private String fileName;
  private File file;
  private String[] comments;
  private Map<String, ConfigEntry<?>> entries;

  public ConfigHandlerImpl(C configObject, File configDirectory, ConfigLoader loader)
      throws ConfigException {
    this.configDirectory = configDirectory;
    this.loader = loader;
    this.configObject = configObject;
    getConfigData();
    this.file = new File(this.configDirectory, this.fileName + this.loader.getFileExtension());
  }

  @Override
  public String getName() {
    return this.fileName;
  }

  @Override
  public File getFile() {
    return this.file;
  }

  @Override
  public void load() throws ConfigException {
    this.loader.load(this.entries, this.file);
  }

  @Override
  public void store() throws ConfigException {
    this.loader.store(this.entries, this.file, this.comments);
  }

  private void getConfigData() throws ConfigException {
    try {
      @SuppressWarnings("unchecked")
      Class<C> configClass = (Class<C>) configObject.getClass();
      ConfigFile configMetadata = configClass.getAnnotation(ConfigFile.class);

      if (configMetadata == null)
        throw new RuntimeException(
            "class " + configClass.getName() + "is lacking the @ConfigFile annotation");
      this.entries = getEntries(configClass, this.configObject);
      this.fileName = configMetadata.value();
      this.comments = configMetadata.comments();
    } catch (IllegalAccessException | RuntimeException e) {
      throw new ConfigException("Couldn't load config file", e);
    }
  }

  private static <C> Map<String, ConfigEntry<?>> getEntries(Class<C> configClass, C config)
      throws IllegalAccessException {
    Map<String, ConfigEntry<?>> ret = new HashMap<>();
    for (Field field : configClass.getDeclaredFields()) {
      ConfigValue value = field.getAnnotation(ConfigValue.class);
      if (value != null) {
        if ((field.getModifiers() & Modifier.STATIC) != 0)
          throw new RuntimeException("config value " + value.key() + " is static.");
        if ((field.getModifiers() & Modifier.PUBLIC) == 0)
          throw new RuntimeException("config value " + value.key() + " is not public.");
        ret.put(value.key(), new ConfigEntryImpl<>(value, field, config));
      }
    }
    return ret;
  }
}
