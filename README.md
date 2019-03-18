HexianConfig
[![Maven Repository](https://img.shields.io/maven-metadata/v/https/maven.misterpemodder.com/list/libs-release/com/misterpemodder/hexian-config/maven-metadata.xml.svg)](https://maven.misterpemodder.com/libs-release/com/misterpemodder/hexian-config)
========

A annotation-based configuration file handling library

## Compiling
To use this library in your workspace, add the following to your `build.gradle`:
```gradle
repositories {
  maven {
    url "https://maven.misterpemodder.com/libs-release/"
  }
}

dependencies {
  compile "com.misterpemodder.customgamerules:hexian-config:[VERSION]"
}
```

## Using
A config file is created by adding the `@ConfigFile` annotation to a class
and marking public (non-static) fields with `@ConfigValue`.  

declaring the config file:
```java
@ConfigFile(value = "myconfig", comments = {"This is a config file.", "comments can be on multiple lines..."})
public class MyConfigFile {
  @ConfigValue(key = "misc.enableThings", comments = "Enables stuff.")
  public boolean enableThings;

  @ConfigValue(key = "game.maxHealth")
  public float maxHealth = 20.0F; // sets the default value to 20
}
```

using it:
```java
// Config object
MyConfigFile cfg = new MyConfigFile();

// Creating a handler
ConfigHandler<MyConfigFile> handler = ConfigHandler.create(cfg, Paths.get("."), ConfigLoader.propertiesLoader());

// Loading the file, if not present default values are kept
handler.load();

// values can be accessed through the config object
System.out.println("stuff enabled? " + cfg.enableThings);
cfg.maxHealth /= 2.0F;

// Storing the file
handler.store();
```

result (using java `properties` format):
```properties
# This is a config file.
# comments can be on multiple lines...

# Enables stuff.
misc.enableThings=false

game.maxHealth=10.0f
```

Read the in-sources javadoc for more information.
