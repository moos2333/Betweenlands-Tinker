# Betweenlands’ Tinker

**A Tinkers’ Construct addon bringing the dark swamps of The Betweenlands to your smeltery and tool forge.**

---

## Overview
Betweenlands’ Tinker integrates **5 new materials**, **4 craftable tools**, **5 unique traits**, and full smeltery support while preserving the dimension's core mechanics—**weakness immunity** and **corrosion**.  
All tools are obtained via **Animator conversion** from vanilla Tinkers’ tools, retaining stats, modifiers, and durability.

---

## Important Note

If you encounter issues with **Weedwood** or **Slimy Bone** materials not working correctly, please consider using **[Tinkers’ Antique](https://www.curseforge.com/minecraft/mc-mods/tinkers-antique)** – an actively maintained fork of Tinkers’ Construct 2 that resolves many compatibility and stability issues.

---

## New Materials

| Material | Tool Stats (Durability / Speed / Attack) | Castable | Traits |
|----------|-------------------------------------------|----------|--------|
| **Weedwood**    | 40 / 2.0 / 2.0      | ❌ | Between, Weed Shield |
| **Slimy Bone**  | 100 / 4.0 / 2.0     | ❌ | Between, Splintering/Splinters |
| **Octine**      | 700 / 6.0 / 4.0     | ✅ | Between, Ignition |
| **Syrmorite**   | 900 / 8.0 / 3.0     | ✅ | Between, Stacking |
| **Valonite**    | 1300 / 8.0 / 6.0    | ❌ | Between, Valor |

---

## New Tools

Convert **any** existing Tinkers’ tool in the Animator (input: tool + Life Crystal + Sulfur):

- **Pickaxe** → Betweenlands Pickaxe
- **Shovel** → Betweenlands Shovel
- **Hatchet** → Betweenlands Hatchet
- **Broadsword** → Betweenlands Broadsword

All stats, modifiers, and durability transfer exactly. Tools are **immune to the Betweenlands weakness** and support the dimension's **corrosion** system.

**Parts can also be converted**: Tool Rod → Betweenlands Handle, Binding → Betweenlands Binding, Blade/Head → Betweenlands Tool Head.  
*Parts are only for replacing components in Betweenlands tools—they cannot be used to craft tools directly.*

---

## New Traits

| Trait | Effect |
|-------|--------|
| **Between** | Bypasses weakness; non-Between tools suffer -25% damage/speed. |
| **Weed Shield** | Absorbs durability loss (max 100); regenerates every 20s (10s in Betweenlands). |
| **Valor** | Gain valor from mining (1) or damage (1 per 5 damage). Every 100 valor = +1% damage, +1% speed, -1% durability consumption (max 3000). |
| **Ignition** | Every third hit ignites target (5s) or deals +25% damage if already burning. |
| **Stacking** | Repairs are 80% less effective (only 20% of repair amount applied). Every 1000 durability consumed grants +1 stacking point (max 100). Each point reduces future durability consumption by 1% (cumulative, up to 100% chance to avoid durability loss). |

---

## Core Mechanics

- **Weakness Immunity**: All Betweenlands tools ignore the dimension's weakness. Non-Interloper tools can gain immunity via the *Between* trait at a 25% penalty.
- **Corrosion**: Tools accumulate corrosion (0–255) over use, reducing damage and speed (max 70% penalty). Tooltips show the current stage.
- **Animator Conversion**: Convert tools or parts as described above (excludes Hammer and Excavator).

---


## Dependencies
- Mantle (1.12-1.3.3+)
- Tinkers’ Construct (1.12.2-2.13.0+)
- The Betweenlands (3.9.6+)
> **Minecraft 1.12.2 · Forge 14.23.5.2847+**

---

## Compatibilities

- **Construct’s Armory** – Adds Betweenlands armor materials and traits.
- **JEI** – Adds Animator conversion recipe viewer support.

---

## License
MIT

---

## Credits

- **Tinkers’ Construct** – SlimeKnights
- **The Betweenlands** – Angry Pixel
- **Mantle** – SlimeKnights

---

*Note: Portions of this code were generated with the assistance of AI Tools.*