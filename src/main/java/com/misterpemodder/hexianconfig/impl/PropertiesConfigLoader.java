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

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import com.misterpemodder.hexianconfig.api.ConfigEntry;
import com.misterpemodder.hexianconfig.api.ConfigException;
import com.misterpemodder.hexianconfig.api.ConfigLoader;

/**
 * Loads java .properties files as config
 */
public class PropertiesConfigLoader implements ConfigLoader {
  public static final PropertiesConfigLoader INSTANCE = new PropertiesConfigLoader();

  @Override
  public void load(Map<String, ConfigEntry<?>> entries, Path path) throws ConfigException {
    if (!Files.exists(path))
      return;
    Properties properties = new Properties();
    try {
      properties.load(new BufferedReader(Files.newBufferedReader(path)));
    } catch (IOException e) {
      throw new ConfigException("Could not load config file " + path.toString(), e);
    }
    for (String key : entries.keySet()) {
      try {
        parseValue(entries.get(key), properties.getProperty(key));
      } catch (RuntimeException e) {
        throw new ConfigException(
            "Could not load key " + key + " in config file " + path.toString(), e);
      }
    }
  }

  @Override
  public void store(Map<String, ConfigEntry<?>> entries, Path path, String[] fileComments)
      throws ConfigException {
    try (PrintWriter pw =
        new PrintWriter(Files.newBufferedWriter(path, WRITE, TRUNCATE_EXISTING, CREATE))) {
      if (fileComments.length > 0) {
        for (String fileComment : fileComments)
          pw.println("# " + fileComment);
        pw.println();
      }

      ConfigEntry<?> entry = null;
      for (String key : entries.keySet()) {
        if (entry != null)
          pw.println();
        entry = entries.get(key);
        for (String comment : entry.getComments())
          pw.println("# " + comment);
        pw.println(key + "=" + escapeString(stringifyValue(entry)));
      }
    } catch (IOException e) {
      throw new ConfigException("Could not save config file " + path.toString(), e);
    }
  }

  @Override
  public String getFileExtension() {
    return ".properties";
  }

  @SuppressWarnings("unchecked")
  private <T> void parseValue(ConfigEntry<T> entry, String value) {
    Class<T> type = entry.getType();
    if (type == String.class)
      entry.setValue((T) value);
    else
      throw new RuntimeException("Cannot parse value of type " + type.getCanonicalName());
  }

  private <T> String stringifyValue(ConfigEntry<T> entry) {
    Class<T> type = entry.getType();
    T value = entry.getValue();
    if (type == String.class)
      return value == null ? "" : (String) entry.getValue();
    throw new RuntimeException("Cannot strigify value of type " + type.getCanonicalName());
  }

  private String escapeString(String str) {
    return str.replace("\\", "\\\\").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
  }
}
