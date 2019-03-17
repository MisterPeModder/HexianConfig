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

import com.misterpemodder.hexianconfig.api.annotation.ConfigFile;
import com.misterpemodder.hexianconfig.api.annotation.ConfigValue;

@ConfigFile(value = "default-config",
    comments = {"A simple config file.", "Used for testing purposes."})
public class DefaultConfig {
  @ConfigValue(key = "test.name")
  public String testName = "yeet";

  @ConfigValue(key = "test.value", comments = "yeet")
  public String testValue = "some value";

  @ConfigValue(key = "test.empty", comments = "empty property")
  public String testEmpty = "";

  @ConfigValue(key = "test.null", comments = {"I am invisible...", "", "...or am I?"})
  public String testNull = null;
}

