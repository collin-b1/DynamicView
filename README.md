<img src="common/src/main/resources/icon.png" alt="DynamicView Icon" width="128" align="center">

# Dynamic View

Dynamic View is a Fabric and NeoForge mod for Minecraft that automatically switches the player's
camera perspective based on context. Supported contexts:

- Swimming
- Crawling
- Flying
- Riding

Each context can be toggled independently and set to first person, third person back, or third
person front. Perspective changes can also be animated instead of snapping instantly. Config is
available in-game via [Mod Menu](https://modrinth.com/mod/modmenu) and
[Cloth Config](https://modrinth.com/mod/cloth-config).

## Requirements

- Minecraft 26.2.x
- Fabric Loader or NeoForge

## Building

Requires JDK 25.

```
./gradlew build
```

Output jars are written to `fabric/build/libs` and `neoforge/build/libs`.

## Project layout

- `common` - shared logic used by both loaders
- `fabric` - Fabric-specific entry point and platform implementation
- `neoforge` - NeoForge-specific entry point and platform implementation

## License

GPL-3.0
