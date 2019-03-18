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

package com.misterpemodder.hexianconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.BufferedInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Properties;
import com.misterpemodder.hexianconfig.api.ConfigHandler;
import com.misterpemodder.hexianconfig.api.ConfigLoader;
import com.misterpemodder.hexianconfig.api.annotation.ConfigFile;
import com.misterpemodder.hexianconfig.api.annotation.ConfigValue;
import org.junit.jupiter.api.Test;

public class BasicTests {
  @Test
  public void defaultConfigReadWrite() throws Exception {
    File directory = new File(BasicTests.class.getResource("/").getPath());
    ConfigHandler<DefaultConfig> handler =
        ConfigHandler.create(new DefaultConfig(), directory, ConfigLoader.propertiesLoader());
    handler.load();
    handler.store();

    assertTrue(handler.getFile().exists(), "saved in correct location check");
  }

  @Test
  public void defaultConfigModification() throws Exception {
    File directory = new File(BasicTests.class.getResource("/").getPath());
    DefaultConfig config = new DefaultConfig();

    final String value1 = "lorem ipsum";
    final String value2 = "this is a test";
    final String value3 = "this is a test";

    ConfigHandler<DefaultConfig> handler =
        ConfigHandler.create(config, directory, ConfigLoader.propertiesLoader());
    config.testName = value1;
    config.testValue = value2;
    config.testEmpty = value3;
    handler.store();

    assertTrue(handler.getFile().exists(), "saved in correct location check");

    Properties properties = new Properties();
    properties.load(new BufferedInputStream(
        Files.newInputStream(handler.getFile().toPath(), StandardOpenOption.READ)));

    assertEquals(config.testName, properties.getProperty("test.name"));
    assertEquals(config.testValue, properties.getProperty("test.value"));
    assertEquals(config.testEmpty, properties.getProperty("test.empty"));
    assertEquals("", properties.getProperty("test.null"), "DefaultConfig.testNull");
  }

  @ConfigFile(value = "default-config",
      comments = {"A simple config file.", "Used for testing purposes."})
  private static class DefaultConfig {
    @ConfigValue(key = "test.name")
    public String testName = "yeet";

    @ConfigValue(key = "test.value", comments = "yeet")
    public String testValue = "some value";

    @ConfigValue(key = "test.empty", comments = "empty property")
    public String testEmpty = "";

    @ConfigValue(key = "test.null", comments = {"I am invisible...", "", "...or am I?"})
    public String testNull = null;
  }
}
