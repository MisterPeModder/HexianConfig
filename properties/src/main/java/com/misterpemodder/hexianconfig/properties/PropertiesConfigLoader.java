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

package com.misterpemodder.hexianconfig.properties;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;
import com.misterpemodder.hexianconfig.core.api.ConfigEntry;
import com.misterpemodder.hexianconfig.core.api.ConfigException;
import com.misterpemodder.hexianconfig.core.api.ConfigLoader;

/**
 * Loads java .properties files as config
 */
public class PropertiesConfigLoader implements ConfigLoader {
  private static final Map<Class<?>, Function<String, ?>> STRING_TO_VALUE_MAP;
  public static final PropertiesConfigLoader INSTANCE = new PropertiesConfigLoader();

  public PropertiesConfigLoader() {
  }

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
    for (String key : entries.keySet())
      parseValue(entries.get(key), properties.getProperty(key));
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
        pw.println(escapeString(key, true) + "=" + escapeString(stringifyValue(entry), false));
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
  private <T> void parseValue(ConfigEntry<T> entry, String value) throws ConfigException {
    Class<T> type = entry.getType();
    Function<String, T> f = (Function<String, T>) STRING_TO_VALUE_MAP.get(entry.getType());
    if (f == null)
      throw new ConfigException("Cannot parse value for key '" + entry.getKey() + "', type "
          + type.getSimpleName() + " is not supported.");
    try {
      entry.setValue(f.apply(value));
    } catch (IllegalArgumentException e) {
      throw new ConfigException(
          "Cannot parse value for key '" + entry.getKey() + "' expected " + type.getSimpleName(),
          e);
    }
  }

  private <T> String stringifyValue(ConfigEntry<T> entry) {
    Class<T> type = entry.getType();
    if (!STRING_TO_VALUE_MAP.keySet().contains(type))
      throw new RuntimeException("Cannot strigify value of type " + type.getSimpleName());
    T value = entry.getValue();
    return value == null ? "" : value.toString();
  }

  private String escapeString(String str, boolean isKey) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0, s = str.length(); i < s; ++i) {
      char c = str.charAt(i);
      switch (c) {
        case '\\':
          builder.append("\\\\");
          break;
        case '=':
          builder.append(isKey ? "\\=" : "=");
          break;
        case ':':
          builder.append(isKey ? "\\:" : ":");
          break;
        case '\n':
          builder.append("\\n");
          break;
        case '\r':
          builder.append("\\r");
          break;
        case '\t':
          builder.append("\\t");
          break;
        default:
          builder.append(c);
      }
    }
    return builder.toString();
  }

  private static boolean parseBooleanStrict(String value) {
    if (value.equalsIgnoreCase("true"))
      return true;
    else if (value.equalsIgnoreCase("false"))
      return false;
    throw new IllegalArgumentException();
  }

  static {
    STRING_TO_VALUE_MAP = new HashMap<>();
    STRING_TO_VALUE_MAP.put(String.class, Function.identity());

    STRING_TO_VALUE_MAP.put(Boolean.class, PropertiesConfigLoader::parseBooleanStrict);
    STRING_TO_VALUE_MAP.put(Byte.class, Byte::valueOf);
    STRING_TO_VALUE_MAP.put(Short.class, Short::valueOf);
    STRING_TO_VALUE_MAP.put(Integer.class, Integer::valueOf);
    STRING_TO_VALUE_MAP.put(Long.class, Long::valueOf);
    STRING_TO_VALUE_MAP.put(Float.class, Float::valueOf);
    STRING_TO_VALUE_MAP.put(Double.class, Double::valueOf);

    STRING_TO_VALUE_MAP.put(boolean.class, PropertiesConfigLoader::parseBooleanStrict);
    STRING_TO_VALUE_MAP.put(byte.class, Byte::parseByte);
    STRING_TO_VALUE_MAP.put(short.class, Short::parseShort);
    STRING_TO_VALUE_MAP.put(int.class, Integer::parseInt);
    STRING_TO_VALUE_MAP.put(long.class, Long::parseLong);
    STRING_TO_VALUE_MAP.put(float.class, Float::parseFloat);
    STRING_TO_VALUE_MAP.put(double.class, Double::parseDouble);
  }
}
