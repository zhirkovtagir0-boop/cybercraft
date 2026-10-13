# Cybercraft: Night City

A Minecraft 26.2 / NeoForge cyberware mod prototype inspired by Cyberpunk 2077 and Edgerunners.

## Requirements
- Minecraft 26.2
- NeoForge 26.2.0.87
- Java 25
- Gradle (or import this repository into IntelliJ IDEA and use Gradle sync)

## Current gameplay
- **Sandevistan** — 5-second time-dilation burst: the player gains Speed and Resistance while hostile mobs within 14 blocks are heavily slowed and weakened. 12-second cooldown. This is a server-safe approximation; it does not alter global game tick rate.
- **Mantis Blades** — focused melee strike.
- **Monowire** — wider melee sweep.
- **Cyberdeck** — press **G** while holding it to open a native quickhack console, then choose Ping (reveal), Cripple Movement (strong slow + weakness), or System Shock (magic damage). Right-click the installed Cyberdeck to apply the selected hack to hostile mobs within 12 blocks. System Shock has a longer cooldown.
- **Shared implant battery** — starts at 100 energy, regenerates 1 point per 4 ticks, and spends 12–30 energy per activation. Failed activations do not trigger the ability.
- **Capacity upgrades** — craft and consume Cyberware Capacity Shards to permanently add +20 maximum energy, up to 200. The shard is consumed on use and cannot be applied past the cap.
- **Cyberpsychosis** — neural strain accumulates when you activate cyberware and decays by one point every 30 seconds without implant use. At 12 strain, neural overload briefly applies weakness and slowness. Sandevistan builds strain fastest; cyberdeck builds it slowest.
- Two mutually exclusive persistent implant slots: **Operating System** and **Arms**. Installed slot choices are stored on the player.
- All four cyberware implants can now be crafted from vanilla resources, implant chips and neural processors; those components also have recipes.
- Russian and English item names, dedicated creative tab, item models using vanilla icons.
- GitHub Actions compiles the mod and uploads the JAR artifact.

## Controls
1. Get an implant from the **Cybercraft: Night City** creative tab.
2. **Sneak + right-click** the implant item to install it. This occupies its body slot; installing a different implant in that slot replaces the previous one.
3. **Sneak + right-click the same implant again** to remove it.
4. Hold the installed implant and **right-click** to activate it.

Sandevistan and Cyberdeck share the Operating System slot. Mantis Blades and Monowire share the Arms slot. You can install one from each category simultaneously.

## Build
Run:
```bash
gradle build
```
The JAR is created in `build/libs/`. On GitHub, open **Actions → Build mod** and download the `cybercraft-jar` artifact after a successful run.

## Roadmap / limitations
This is an early mechanics milestone, not yet a complete Cyberpunk-style implant suite. Next steps are a target-feedback pass for Cyberdeck quickhacks, a ripperdoc workstation and GUI, custom 3D models and animations, sound/particles, balance/configuration and an in-game test pass. The CI build is the source of truth for API compatibility; do not treat a JAR as verified until the workflow finishes successfully.
