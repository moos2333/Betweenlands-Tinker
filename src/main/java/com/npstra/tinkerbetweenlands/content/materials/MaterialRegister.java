package com.npstra.tinkerbetweenlands.content.materials;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.tools.TinkerTraits;
import com.npstra.tinkerbetweenlands.content.fluid.FluidRegister;
import com.npstra.tinkerbetweenlands.content.traits.TraitBetween;
import com.npstra.tinkerbetweenlands.content.traits.TraitWeedShield;
import com.npstra.tinkerbetweenlands.content.traits.TraitValor;
import com.npstra.tinkerbetweenlands.content.traits.TraitIgnition;
import com.npstra.tinkerbetweenlands.content.traits.TraitStacking;

public class MaterialRegister {
    public static Material weedwood, slimy_bone, valonite, octine, syrmorite;
    private static boolean statsRegistered = false;

    public static void preInit() {
        weedwood = createMaterial("weedwood", 0x706738, false, 40, 2.0f, 2.0f, 0);
        slimy_bone = createMaterial("slimy_bone", 0x9e9e6a, false, 200, 4.0f, 2.0f, 1);
        valonite = createMaterial("valonite", 0xb69cb6, false, 1300, 8.0f, 6.0f, 3);
        octine = createMaterial("octine", 0xffc81f, true, 700, 6.0f, 4.0f, 2);
        syrmorite = createMaterial("syrmorite", 0x5660A5, true, 900, 8.0f, 3.0f, 2);

        octine.setFluid(FluidRegister.fluidOctine);
        syrmorite.setFluid(FluidRegister.fluidSyrmorite);

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
                    new BowMaterialStats(1.0f, 1.0f, 0.0f)
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
                    new BowMaterialStats(0.9f, 1.1f, 3.0f)
            );
        }
        if (syrmorite != null) {
            TinkerRegistry.addMaterialStats(syrmorite,
                    new HeadMaterialStats(900, 8.0f, 3.0f, 2),
                    new ExtraMaterialStats(150),
                    new HandleMaterialStats(1.0f, 150),
                    new BowMaterialStats(0.9f, 1.1f, 2.0f)
            );
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
        addDirectItem(weedwood, "thebetweenlands:weedwood_planks", 0, Material.VALUE_Ingot);
        addDirectItem(weedwood, "thebetweenlands:log_weedwood", OreDictionary.WILDCARD_VALUE, 4 * Material.VALUE_Ingot);
        addDirectItem(weedwood, "thebetweenlands:weedwood", 0, 4 * Material.VALUE_Ingot);
        addDirectItem(weedwood, "thebetweenlands:items_misc", 20, 72);
        weedwood.setRepresentativeItem(new ItemStack(Item.getByNameOrId("thebetweenlands:weedwood_planks")));

        addDirectItem(slimy_bone, "thebetweenlands:items_misc", 14, Material.VALUE_Ingot);
        slimy_bone.setRepresentativeItem(new ItemStack(Item.getByNameOrId("thebetweenlands:items_misc"), 1, 14));

        addDirectItem(valonite, "thebetweenlands:items_misc", 19, Material.VALUE_Ingot);
        addDirectItem(valonite, "thebetweenlands:items_misc", 43, Material.VALUE_Nugget);
        addDirectItem(valonite, "thebetweenlands:valonite_block", 0, Material.VALUE_Block);
        valonite.setRepresentativeItem(new ItemStack(Item.getByNameOrId("thebetweenlands:items_misc"), 1, 19));

        addDirectItem(octine, "thebetweenlands:octine_ingot", 0, Material.VALUE_Ingot);
        addDirectItem(octine, "thebetweenlands:items_misc", 42, Material.VALUE_Nugget);
        addDirectItem(octine, "thebetweenlands:octine_block", 0, Material.VALUE_Block);
        octine.setRepresentativeItem(new ItemStack(Item.getByNameOrId("thebetweenlands:octine_ingot")));

        addDirectItem(syrmorite, "thebetweenlands:items_misc", 11, Material.VALUE_Ingot);
        addDirectItem(syrmorite, "thebetweenlands:items_misc", 41, Material.VALUE_Nugget);
        addDirectItem(syrmorite, "thebetweenlands:syrmorite_block", 0, Material.VALUE_Block);
        syrmorite.setRepresentativeItem(new ItemStack(Item.getByNameOrId("thebetweenlands:items_misc"), 1, 11));

        addOreDictOnly(weedwood, "plankWeedwood", Material.VALUE_Ingot);
        addOreDictOnly(weedwood, "logWeedwood", 4 * Material.VALUE_Ingot);
        addOreDictOnly(weedwood, "stickWeedwood", 72);

        addOreDictOnly(slimy_bone, "boneSlimy", Material.VALUE_Ingot);
        addOreDictOnly(valonite, "gemValonite", Material.VALUE_Ingot);
        addOreDictOnly(valonite, "nuggetValonite", Material.VALUE_Nugget);
        addOreDictOnly(valonite, "blockValonite", Material.VALUE_Block);
        addOreDictOnly(octine, "ingotOctine", Material.VALUE_Ingot);
        addOreDictOnly(octine, "nuggetOctine", Material.VALUE_Nugget);
        addOreDictOnly(octine, "blockOctine", Material.VALUE_Block);
        addOreDictOnly(syrmorite, "ingotSyrmorite", Material.VALUE_Ingot);
        addOreDictOnly(syrmorite, "nuggetSyrmorite", Material.VALUE_Nugget);
        addOreDictOnly(syrmorite, "blockSyrmorite", Material.VALUE_Block);
    }

    private static void addDirectItem(Material mat, String itemId, int meta, int value) {
        Item item = Item.getByNameOrId(itemId);
        if (item != null) {
            mat.addItem(new ItemStack(item, 1, meta), 1, value);
        }
    }

    private static void addOreDictOnly(Material mat, String oreName, int value) {
        mat.addItem(oreName, 1, value);
    }
}