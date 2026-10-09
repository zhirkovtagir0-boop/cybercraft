# Cybercraft: Night City

A Minecraft 26.2 / NeoForge cyberware mod prototype inspired by Cyberpunk 2077 and Edgerunners.

## Requirements
- Minecraft 26.2
- NeoForge 26.2.0.87
- Java 25
- Gradle (or import this repository into IntelliJ IDEA and use Gradle sync)

## Current gameplay
- **Sandevistan** — brief speed burst and resistance, with a 12-second cooldown.
- **Mantis Blades** — focused melee strike.
- **Monowire** — wider melee sweep.
- **Cyberdeck** — quickhack pulse that highlights and slows nearby hostile mobs.
- **Cyberpsychosis** — neural strain accumulates when you activate cyberware; at 12 strain, neural overload briefly applies weakness and slowness. Sandevistan builds strain fastest; cyberdeck builds it slowest.
- Two mutually exclusive persistent implant slots: **Operating System** and **Arms**. Installed slot choices are stored on the player.
- Implant chips and neural processors have crafting recipes.
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
This is the first playable mechanics milestone, not yet a complete Cyberpunk-style implant suite. Next steps are a ripperdoc workstation and GUI, capacity/energy budgets, cyberpsychosis, custom 3D models and animations, sound/particles, more quickhacks, balance/configuration and an in-game test pass. The CI build is the source of truth for API compatibility; do not treat a JAR as verified until the workflow finishes successfully.
