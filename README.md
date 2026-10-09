# Cybercraft: Night City

A Minecraft 26.2 / NeoForge cyberware mod prototype inspired by Cyberpunk 2077 and Edgerunners.

## Requirements
- Minecraft 26.2
- NeoForge 26.2.0.87
- Java 25
- Gradle (or import this repository into IntelliJ IDEA and use Gradle sync)

## Prototype features
- **Sandevistan** — right-click for a brief speed burst and resistance, with a 12-second cooldown.
- **Mantis Blades** — short-range focused melee attack.
- **Monowire** — wider melee sweep.
- **Cyberdeck** — quickhack pulse that highlights and slows nearby hostile mobs.
- Implant chips and neural processors are registered as crafting materials.
- Russian and English item names.
- GitHub Actions build workflow uploads the resulting JAR as an artifact.

## Build
Run:
```bash
gradle build
```
The JAR is created in `build/libs/`. On GitHub, open **Actions → Build mod** and download the `cybercraft-jar` artifact after a successful run.

## Current limitations
This is an early playable-mechanics prototype, not yet the full implant-management system. The four cyberware items currently act as ability test harnesses and can be activated from the inventory. A ripperdoc workstation, persistent implant slots, capacity limits, cyberpsychosis, dedicated 3D models/animations, recipes and balance/configuration are planned next. Build compatibility must be confirmed by the CI workflow; APIs can change between NeoForge builds.
