package com.npstra.tinkerbetweenlands.content.materials;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import com.npstra.tinkerbetweenlands.content.traits.TraitBetween;
import com.npstra.tinkerbetweenlands.content.traits.TraitWeedShield;
import com.npstra.tinkerbetweenlands.content.traits.TraitValor;
import com.npstra.tinkerbetweenlands.content.traits.TraitIgnition;
import com.npstra.tinkerbetweenlands.content.traits.TraitStacking;
import slimeknights.tconstruct.tools.traits.TraitSplintering;
import slimeknights.tconstruct.tools.traits.TraitSplinters;

public class MaterialRegister {
    public static Material weedwood, slimy_bone, valonite, octine, syrmorite;
    public static Fluid fluidOctine, fluidSyrmorite;
    private static boolean statsRegistered = false;

    public static void preInit() {
        fluidOctine = new Fluid("octine", null, null).setTemperature(900);
        fluidSyrmorite = new Fluid("syrmorite", null, null).setTemperature(800);
        FluidRegistry.registerFluid(fluidOctine);
        FluidRegistry.registerFluid(fluidSyrmorite);

        weedwood = createMaterial("weedwood", 0x706738, false, 40, 2.0f, 2.0f, 0);
        slimy_bone = createMaterial("slimy_bone", 0x9e9e6a, false, 100, 4.0f, 2.0f, 1);
        valonite = createMaterial("valonite", 0xb69cb6, false, 1300, 8.0f, 6.0f, 4);
        octine = createMaterial("octine", 0xF5A615, true, 700, 6.0f, 4.0f, 2);
        syrmorite = createMaterial("syrmorite", 0x5660A5, true, 900, 8.0f, 3.0f, 2);

        octine.setFluid(fluidOctine);
        syrmorite.setFluid(fluidSyrmorite);

        addStats();
    }

    private static Material createMaterial(String id, int color, boolean castable, int durability, float speed, float attack, int harvest) {
        Material mat = new Material(id, color, castable);
        mat.setCraftable(true).setCastable(castable);
        mat.addTrait(TraitBetween.INSTANCE, "head");
        if (id.equals("weedwood")) {
            mat.addTrait(TraitWeedShield.INSTANCE, "head");
            mat.addTrait(TraitWeedShield.INSTANCE, null);
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

        if (weedwood != null)
            TinkerRegistry.addMaterialStats(weedwood,
                    new HeadMaterialStats(40, 2.0f, 2.0f, 0),
                    new ExtraMaterialStats(25),
                    new HandleMaterialStats(1.0f, 25),
                    new BowMaterialStats(1.0f, 1.0f, 0.0f),
                    new ArrowShaftMaterialStats(1.0f, 0)
            );
        if (slimy_bone != null)
            TinkerRegistry.addMaterialStats(slimy_bone,
                    new HeadMaterialStats(100, 4.0f, 2.0f, 1),
                    new ExtraMaterialStats(50),
                    new HandleMaterialStats(1.1f, 0),
                    new BowMaterialStats(1.0f, 1.0f, 0.0f)
            );
        if (valonite != null)
            TinkerRegistry.addMaterialStats(valonite,
                    new HeadMaterialStats(1300, 8.0f, 6.0f, 4),
                    new ExtraMaterialStats(300),
                    new HandleMaterialStats(1.1f, 400),
                    new BowMaterialStats(0.8f, 1.2f, 4.0f)
            );
        if (octine != null)
            TinkerRegistry.addMaterialStats(octine,
                    new HeadMaterialStats(700, 6.0f, 4.0f, 2),
                    new ExtraMaterialStats(200),
                    new HandleMaterialStats(1.0f, 200),
                    new BowMaterialStats(0.9f, 1.1f, 3.0f)
            );
        if (syrmorite != null)
            TinkerRegistry.addMaterialStats(syrmorite,
                    new HeadMaterialStats(900, 8.0f, 3.0f, 2),
                    new ExtraMaterialStats(300),
                    new HandleMaterialStats(1.0f, 150),
                    new BowMaterialStats(0.9f, 1.1f, 2.0f)
            );
    }

    public static void init() {
        setMaterialItems();
        TinkerRegistry.integrate(weedwood);
        TinkerRegistry.integrate(slimy_bone);
        TinkerRegistry.integrate(valonite);
        TinkerRegistry.integrate(octine);
        TinkerRegistry.integrate(syrmorite);
        registerMeltingRecipes();
    }

    private static void setMaterialItems() {
        setItem(weedwood, "thebetweenlands:weedwood_planks", 0);
        setItem(slimy_bone, "thebetweenlands:items_misc", 14);
        setItem(valonite, "thebetweenlands:items_misc", 19);
        setItem(octine, "thebetweenlands:octine_ingot", 0);
        setItem(syrmorite, "thebetweenlands:items_misc", 11);
    }

    private static void setItem(Material mat, String itemId, int meta) {
        Item item = Item.getByNameOrId(itemId);
        if (item != null) {
            ItemStack stack = new ItemStack(item, 1, meta);
            mat.addItem(stack, 1, Material.VALUE_Ingot);
            mat.setRepresentativeItem(stack);
        }
    }

    private static void registerMeltingRecipes() {
        Item octineIngot = Item.getByNameOrId("thebetweenlands:octine_ingot");
        if (octineIngot != null)
            TinkerRegistry.registerMelting(new ItemStack(octineIngot), fluidOctine, Material.VALUE_Ingot);

        Item syrmoriteIngot = Item.getByNameOrId("thebetweenlands:items_misc");
        if (syrmoriteIngot != null)
            TinkerRegistry.registerMelting(new ItemStack(syrmoriteIngot, 1, 11), fluidSyrmorite, Material.VALUE_Ingot);
    }
}