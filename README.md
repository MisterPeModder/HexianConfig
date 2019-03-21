HexianConfig
[![Maven Repository](https://img.shields.io/maven-metadata/v/https/maven.misterpemodder.com/list/libs-snapshot/com/misterpemodder/hexian-config/maven-metadata.xml.svg)](https://maven.misterpemodder.com/libs-snapshot/com/misterpemodder/hexian-config)
========

A annotation-based configuration file handling library

## Compiling

Gradle:
```gradle
repositories {
  maven {
    url "https://maven.misterpemodder.com/libs-snapshot/"
  }
}

dependencies {
  compile "com.misterpemodder.hexian-config:core:[VERSION]"

  // replace by the loader you want to use
  compile "com.misterpemodder.hexian-config:properties:[VERSION]"
}
```

Maven:
```xml
<repositories>
    <repository>
        <id>sponge</id>
        <url>https://maven.misterpemodder.com/libs-snapshot/</url>
    </repository>
</repositories>

<dependencies>
  <dependency>
      <groupId>com.misterpemodder.hexian-config</groupId>
      <artifactId>core</artifactId>
      <version>[VERSION]</version>
  </dependency>
  <dependency>
      <groupId>com.misterpemodder.hexian-config</groupId>
      <!-- replace by the loader you want -->
      <artifactId>properties</artifactId>
      <version>[VERSION]</version>
  </dependency>
</dependencies>
```

## Using HexianConfig
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

// Creating a handler, using the properties loader.
ConfigHandler<MyConfigFile> handler = ConfigHandler.create(cfg, Paths.get("."), new PropertiesConfigLoader());

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
