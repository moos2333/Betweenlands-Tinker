package com.npstra.tinkerbetweenlands.compat.conarm.materials;

import c4.conarm.common.armor.traits.ArmorTraits;
import c4.conarm.lib.materials.ArmorMaterialType;
import c4.conarm.lib.materials.CoreMaterialStats;
import c4.conarm.lib.materials.PlatesMaterialStats;
import c4.conarm.lib.materials.TrimMaterialStats;
import net.minecraftforge.fml.common.Loader;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import com.npstra.tinkerbetweenlands.content.materials.MaterialRegister;

public class ArmorMaterial {

    public static void registerArmorStats() {
        if (!Loader.isModLoaded("conarm")) return;
        if (Material.UNKNOWN.getStats(ArmorMaterialType.CORE) == null) {
            Material.UNKNOWN.addStats(new CoreMaterialStats(0.0f, 0.0f));
            Material.UNKNOWN.addStats(new PlatesMaterialStats(1.0f, 0.0f, 0.0f));
            Material.UNKNOWN.addStats(new TrimMaterialStats(0.0f));
        }
        Material weedwood = MaterialRegister.weedwood;
        Material slimyBone = MaterialRegister.slimy_bone;
        Material octine = MaterialRegister.octine;
        Material syrmorite = MaterialRegister.syrmorite;
        Material valonite = MaterialRegister.valonite;

        if (weedwood != null) {
            TinkerRegistry.addMaterialStats(weedwood,
                    new CoreMaterialStats(3.0f, 3.5f),
                    new PlatesMaterialStats(1.0f, 1.5f, 0.0f),
                    new TrimMaterialStats(1.0f)
            );
        }
        if (slimyBone != null) {
            TinkerRegistry.addMaterialStats(slimyBone,
                    new CoreMaterialStats(13.0f, 4.5f),
                    new PlatesMaterialStats(1.15f, 4.0f, 1.25f),
                    new TrimMaterialStats(6.0f)
            );
        }
        if (octine != null) {
            TinkerRegistry.addMaterialStats(octine,
                    new CoreMaterialStats(12.5f, 15.5f),
                    new PlatesMaterialStats(0.9f, 5.5f, 0.0f),
                    new TrimMaterialStats(4.0f)
            );
        }
        if (syrmorite != null) {
            TinkerRegistry.addMaterialStats(syrmorite,
                    new CoreMaterialStats(16.0f, 17.5f),
                    new PlatesMaterialStats(1.2f, 1.0f, 1.0f),
                    new TrimMaterialStats(11.0f)
            );
        }
        if (valonite != null) {
            TinkerRegistry.addMaterialStats(valonite,
                    new CoreMaterialStats(19.0f, 19.5f),
                    new PlatesMaterialStats(1.05f, 8.0f, 3.0f),
                    new TrimMaterialStats(5.0f)
            );
        }
    }

    public static void registerArmorTraits() {
        if (!Loader.isModLoaded("conarm")) return;
        com.npstra.tinkerbetweenlands.compat.conarm.traits.TraitWeedShieldArmor traitWeedShield = new com.npstra.tinkerbetweenlands.compat.conarm.traits.TraitWeedShieldArmor();
        com.npstra.tinkerbetweenlands.compat.conarm.traits.TraitStackingArmor traitStacking = new com.npstra.tinkerbetweenlands.compat.conarm.traits.TraitStackingArmor();
        com.npstra.tinkerbetweenlands.compat.conarm.traits.TraitValorArmor traitValor = new com.npstra.tinkerbetweenlands.compat.conarm.traits.TraitValorArmor();

        Material weedwood = MaterialRegister.weedwood;
        Material syrmorite = MaterialRegister.syrmorite;
        Material slimyBone = MaterialRegister.slimy_bone;
        Material valonite = MaterialRegister.valonite;

        if (weedwood != null) {
            addArmorTrait(weedwood, traitWeedShield);
        }
        if (syrmorite != null) {
            addArmorTrait(syrmorite, traitStacking);
        }
        if (valonite != null) {
            addArmorTrait(valonite, traitValor);
        }
        if (slimyBone != null) {
            slimyBone.addTrait(ArmorTraits.calcic, ArmorMaterialType.CORE);
            slimyBone.addTrait(ArmorTraits.skeletal, ArmorMaterialType.PLATES);
            slimyBone.addTrait(ArmorTraits.skeletal, ArmorMaterialType.TRIM);
        }
    }

    public static void reintegrateMaterials() {
        if (!Loader.isModLoaded("conarm")) return;
        reintegrate(MaterialRegister.weedwood);
        reintegrate(MaterialRegister.slimy_bone);
        reintegrate(MaterialRegister.octine);
        reintegrate(MaterialRegister.syrmorite);
        reintegrate(MaterialRegister.valonite);
    }

    private static void reintegrate(Material material) {
        if (material != null) TinkerRegistry.integrate(material);
    }

    private static void addArmorTrait(Material material, c4.conarm.lib.traits.AbstractArmorTrait trait) {
        material.addTrait(trait, ArmorMaterialType.CORE);
        material.addTrait(trait, ArmorMaterialType.PLATES);
        material.addTrait(trait, ArmorMaterialType.TRIM);
    }
}