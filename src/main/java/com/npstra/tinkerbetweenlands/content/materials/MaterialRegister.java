package com.npstra.tinkerbetweenlands.content.materials;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.*;
import slimeknights.tconstruct.tools.TinkerTraits;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;
import com.npstra.tinkerbetweenlands.content.fluid.FluidRegister;
import com.npstra.tinkerbetweenlands.content.traits.TraitBetween;
import com.npstra.tinkerbetweenlands.content.traits.TraitWeedShield;
import com.npstra.tinkerbetweenlands.content.traits.TraitValor;
import com.npstra.tinkerbetweenlands.content.traits.TraitIgnition;
import com.npstra.tinkerbetweenlands.content.traits.TraitStacking;

public class MaterialRegister {
    public static Material weedwood, slimy_bone, valonite, octine, syrmorite, reedRope, silkThread;
    private static boolean statsRegistered = false;

    public static void preInit() {
        weedwood = createMaterial("weedwood", 0x706738, false, 40, 2.0f, 2.0f, 0);
        slimy_bone = createMaterial("slimy_bone", 0x9e9e6a, false, 200, 4.0f, 2.0f, 1);
        valonite = createMaterial("valonite", 0xb69cb6, false, 1300, 8.0f, 6.0f, 3);
        octine = createMaterial("octine", 0xffc81f, true, 700, 6.0f, 4.0f, 2);
        syrmorite = createMaterial("syrmorite", 0x5660A5, true, 900, 8.0f, 3.0f, 2);

        octine.setFluid(FluidRegister.fluidOctine);
        syrmorite.setFluid(FluidRegister.fluidSyrmorite);

        reedRope = new Material("reed_rope", 0x0e4b0e);
        reedRope.setCraftable(true).setCastable(false);
        TinkerRegistry.addMaterial(reedRope);
        reedRope.setVisible();

        silkThread = new Material("silk_thread", 0xf0fce0);
        silkThread.setCraftable(true).setCastable(false);
        TinkerRegistry.addMaterial(silkThread);
        silkThread.setVisible();

        addStats();
    }

    private static Material createMaterial(String id, int color, boolean castable, int durability, float speed, float attack, int harvest) {
        Material mat = new Material(id, color, castable);
        mat.setCraftable(!castable);
        mat.setCastable(castable);
        mat.addTrait(TraitBetween.INSTANCE, "head");

        if (id.equals("weedwood")) {
            mat.addTrait(TraitWeedShield.INSTANCE, "head");
            mat.addTrait(TraitWeedShield.INSTANCE, null);
        } else if (id.equals("slimy_bone")) {
            mat.addTrait(TinkerTraits.splintering, "head");
            mat.addTrait(TinkerTraits.fractured, null);
        } else if (id.equals("valonite")) {
            mat.addTrait(TraitValor.INSTANCE, "head");
            mat.addTrait(TraitValor.INSTANCE, null);
        } else if (id.equals("octine")) {
            mat.addTrait(TraitIgnition.INSTANCE, "head");
            mat.addTrait(TraitIgnition.INSTANCE, null);
        } else if (id.equals("syrmorite")) {
            mat.addTrait(TraitStacking.INSTANCE, "head");
            mat.addTrait(TraitStacking.INSTANCE, null);
        }

        TinkerRegistry.addMaterial(mat);
        mat.setVisible();
        return mat;
    }

    private static void addStats() {
        if (statsRegistered) return;
        statsRegistered = true;

        if (weedwood != null) {
            TinkerRegistry.addMaterialStats(weedwood,
                    new HeadMaterialStats(40, 2.0f, 2.0f, 0),
                    new ExtraMaterialStats(25),
                    new HandleMaterialStats(1.0f, 25),
                    new BowMaterialStats(1.0f, 1.0f, 0.0f),
                    new ArrowShaftMaterialStats(1.0f, 0)
            );
        }
        if (slimy_bone != null) {
            TinkerRegistry.addMaterialStats(slimy_bone,
                    new HeadMaterialStats(200, 4.0f, 2.0f, 1),
                    new ExtraMaterialStats(50),
                    new HandleMaterialStats(1.1f, 0),
                    new BowMaterialStats(0.9f, 1.1f, 0.0f)
            );
        }
        if (valonite != null) {
            TinkerRegistry.addMaterialStats(valonite,
                    new HeadMaterialStats(1300, 8.0f, 6.0f, 3),
                    new ExtraMaterialStats(300),
                    new HandleMaterialStats(1.1f, 400),
                    new BowMaterialStats(0.8f, 1.2f, 5.0f)
            );
        }
        if (octine != null) {
            TinkerRegistry.addMaterialStats(octine,
                    new HeadMaterialStats(700, 6.0f, 4.0f, 2),
                    new ExtraMaterialStats(100),
                    new HandleMaterialStats(1.0f, 100),
                    new BowMaterialStats(0.5f, 1.5f, 7.0f)
            );
        }
        if (syrmorite != null) {
            TinkerRegistry.addMaterialStats(syrmorite,
                    new HeadMaterialStats(900, 8.0f, 3.0f, 2),
                    new ExtraMaterialStats(150),
                    new HandleMaterialStats(1.0f, 150),
                    new BowMaterialStats(0.9f, 1.1f, 3.0f)
            );
        }

        if (reedRope != null) {
            reedRope.addTrait(TraitBetween.INSTANCE, "bowstring");
            TinkerRegistry.addMaterialStats(reedRope,
                    new BowStringMaterialStats(0.75f));
        }
        if (silkThread != null) {
            silkThread.addTrait(TraitBetween.INSTANCE, "bowstring");
            TinkerRegistry.addMaterialStats(silkThread,
                    new BowStringMaterialStats(1.0f));
        }
    }

    public static void init() {
        setMaterialItems();
        TinkerRegistry.integrate(weedwood);
        TinkerRegistry.integrate(slimy_bone);
        TinkerRegistry.integrate(valonite);
        TinkerRegistry.integrate(octine);
        TinkerRegistry.integrate(syrmorite);
    }

    private static void setMaterialItems() {
        weedwood.addItem(new ItemStack(BlockRegistry.WEEDWOOD_PLANKS), 1, Material.VALUE_Ingot);
        weedwood.addItem(new ItemStack(BlockRegistry.LOG_WEEDWOOD, 1, OreDictionary.WILDCARD_VALUE), 1, 4 * Material.VALUE_Ingot);
        weedwood.addItem(new ItemStack(BlockRegistry.WEEDWOOD), 1, 4 * Material.VALUE_Ingot);
        weedwood.addItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 20), 1, 72);
        weedwood.setRepresentativeItem(new ItemStack(BlockRegistry.WEEDWOOD_PLANKS));

        slimy_bone.addItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 14), 1, Material.VALUE_Ingot);
        slimy_bone.addItem(new ItemStack(BlockRegistry.SLIMY_BONE_BLOCK), 1, Material.VALUE_Block);
        slimy_bone.setRepresentativeItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 14));

        valonite.addItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 19), 1, Material.VALUE_Ingot);
        valonite.addItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 43), 1, Material.VALUE_Nugget);
        valonite.addItem(new ItemStack(BlockRegistry.VALONITE_BLOCK), 1, Material.VALUE_Block);
        valonite.setRepresentativeItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 19));

        octine.addItem(new ItemStack(ItemRegistry.OCTINE_INGOT), 1, Material.VALUE_Ingot);
        octine.addItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 42), 1, Material.VALUE_Nugget);
        octine.addItem(new ItemStack(BlockRegistry.OCTINE_BLOCK), 1, Material.VALUE_Block);
        octine.setRepresentativeItem(new ItemStack(ItemRegistry.OCTINE_INGOT));

        syrmorite.addItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 11), 1, Material.VALUE_Ingot);
        syrmorite.addItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 41), 1, Material.VALUE_Nugget);
        syrmorite.addItem(new ItemStack(BlockRegistry.SYRMORITE_BLOCK), 1, Material.VALUE_Block);
        syrmorite.setRepresentativeItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 11));

        valonite.addItem("gemValonite", 1, Material.VALUE_Ingot);
        valonite.addItem("nuggetValonite", 1, Material.VALUE_Nugget);
        valonite.addItem("blockValonite", 1, Material.VALUE_Block);
        octine.addItem("ingotOctine", 1, Material.VALUE_Ingot);
        octine.addItem("nuggetOctine", 1, Material.VALUE_Nugget);
        octine.addItem("blockOctine", 1, Material.VALUE_Block);
        syrmorite.addItem("ingotSyrmorite", 1, Material.VALUE_Ingot);
        syrmorite.addItem("nuggetSyrmorite", 1, Material.VALUE_Nugget);
        syrmorite.addItem("blockSyrmorite", 1, Material.VALUE_Block);

        reedRope.addItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 7), 1, Material.VALUE_Nugget);
        reedRope.setRepresentativeItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 7));

        silkThread.addItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 63), 1, Material.VALUE_Shard);
        silkThread.setRepresentativeItem(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 63));
    }
}