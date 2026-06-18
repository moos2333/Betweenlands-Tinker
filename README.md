# Betweenlands’ Tinker

**A Tinkers’ Construct addon that brings Betweenlands materials, tools, and mechanics to your forge.**

---

## Overview
Betweenlands’ Tinker integrates the dark swamps of The Betweenlands into Tinkers’ Construct. It adds 5 new materials, 4 craftable tools, 5 unique traits, and full smeltery support—all while preserving the dimension’s core mechanics: weakness and corrosion.

**Minecraft 1.12.2 · Forge 14.23.5.2847+**

---

## Dependencies
- Mantle (1.12-1.3.3+)
- Tinkers’ Construct (1.12.2-2.13.0+)
- The Betweenlands (3.9.6+)

---

## New Materials

| Material | Mining Level | Castable | Durability | Speed | Attack | Traits |
|----------|--------------|----------|------------|-------|--------|--------|
| **Weedwood** | 0 | ❌ | 40 | 2.0 | 2.0 | Between, Weed Shield |
| **Slimy Bone** | 1 | ❌ | 100 | 4.0 | 2.0 | Between, Splintering (Head), Splinters (All) |
| **Octine** | 2 | ✅ | 700 | 6.0 | 4.0 | Between, Ignition |
| **Syrmorite** | 2 | ✅ | 900 | 8.0 | 3.0 | Between, Stacking |
| **Valonite** | 3 | ❌ | 1300 | 8.0 | 6.0 | Between, Valor |

Octine and Syrmorite are fully castable: ore → 288 mB, ingot → 144 mB, nugget → 16 mB, block → 1296 mB. Use casting table (with casts) or basin (no cast for blocks).

---

## New Tools

Four Betweenlands tools are created by converting existing Tinkers’ tools in the Animator (recipe: Tool + Life Crystal + Sulfur):

- Betweenlands Pickaxe (from Pickaxe)
- Betweenlands Shovel (from Shovel)
- Betweenlands Hatchet (from Hatchet)
- Betweenlands Broadsword (from Broadsword)

All stats, modifiers, and durability are preserved. Tools are immune to the Betweenlands weakness but suffer corrosion.

Part conversion is also available: Tool Rod → Betweenlands Handle, Binding → Betweenlands Binding, Blade/Head → Betweenlands Tool Head.

---

## New Traits

| Trait | Effect |
|-------|--------|
| **Between** | Non-Interloper tools bypass weakness but lose 25% damage/speed; Interloper tools unaffected. |
| **Weed Shield** | Generates a shield (max 100) that absorbs durability loss. Regens every 20s (10s in Betweenlands). |
| **Valor** | Gain valor from mining (1) or dealing damage (1 per 5 damage). Every 100 valor gives +1% damage, +1% speed, -1% durability consumption (max 3000). |
| **Ignition** | Every third hit ignites target for 5s, or deals +25% damage if already burning. |
| **Stacking** | Repairs are 50% less effective; repairs over 1000 durability grant 1 stacking point (max 100), each -1% durability consumption. |

---

## Core Mechanics

- **Weakness Immunity**: All Betweenlands tools ignore the Betweenlands weakness effect. Non-Interloper tools can gain this via the Between trait at a 25% penalty.
- **Corrosion**: Betweenlands tools accumulate corrosion (0–255) over time, reducing damage and speed (max 70% penalty). Tooltips show corrosion stage.
- **Animator Conversion**: Convert tools or parts (see above). Excludes Hammer and Excavator.

---

## Smeltery Integration

Full smelting and casting support for Octine and Syrmorite:

- Octine/Syrmorite Ore → 288 mB Molten Metal
- Ingot/Nugget/Block → melts to 144/16/1296 mB
- Casting Table (with cast) → Ingot or Nugget
- Casting Basin (no cast) → Block

---

## Localization & Resources
Full localization for English and Chinese Simplified. All modifier textures are registered.

---

## License
MIT

---

## Credits

- **Tinkers’ Construct** – Created by SlimeKnights
- **The Betweenlands** – Created by Angry Pixel
- **Mantle** – Created by SlimeKnights

Special thanks to the modding community for their support and contributions.

---

**Note:**  
Portions of this code were generated with the assistance of an AI language model.