package com.npstra.tinkerbetweenlands.compat.conarm.materials;

import c4.conarm.lib.materials.ArmorMaterialType;
import c4.conarm.lib.materials.CoreMaterialStats;
import c4.conarm.lib.materials.PlatesMaterialStats;
import c4.conarm.lib.materials.TrimMaterialStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.TagUtil;
import com.npstra.tinkerbetweenlands.content.materials.MaterialRegister;
import com.npstra.tinkerbetweenlands.compat.conarm.traits.TraitStackingArmor;
import com.npstra.tinkerbetweenlands.compat.conarm.traits.TraitWeedShieldArmor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArmorMaterial {

    private static final TraitWeedShieldArmor TRAIT_WEED_SHIELD = new TraitWeedShieldArmor();
    private static final TraitStackingArmor TRAIT_STACKING = new TraitStackingArmor();
    private static final Set<String> OUR_MATERIALS = new HashSet<>();

    public static void init(FMLInitializationEvent event) {
        if (!Loader.isModLoaded("conarm")) {
            return;
        }
        registerArmorStats();
        registerArmorTraits();
        collectOurMaterials();
        if (FMLCommonHandler.instance().getSide().isClient()) {
            registerArmorColors();
        }
    }

    private static void collectOurMaterials() {
        if (MaterialRegister.weedwood != null) OUR_MATERIALS.add(MaterialRegister.weedwood.identifier);
        if (MaterialRegister.slimy_bone != null) OUR_MATERIALS.add(MaterialRegister.slimy_bone.identifier);
        if (MaterialRegister.valonite != null) OUR_MATERIALS.add(MaterialRegister.valonite.identifier);
        if (MaterialRegister.octine != null) OUR_MATERIALS.add(MaterialRegister.octine.identifier);
        if (MaterialRegister.syrmorite != null) OUR_MATERIALS.add(MaterialRegister.syrmorite.identifier);
    }

    private static void registerArmorStats() {
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

    private static void registerArmorTraits() {
        Material weedwood = MaterialRegister.weedwood;
        Material syrmorite = MaterialRegister.syrmorite;

        if (weedwood != null) {
            addArmorTrait(weedwood, TRAIT_WEED_SHIELD);
        }
        if (syrmorite != null) {
            addArmorTrait(syrmorite, TRAIT_STACKING);
        }
    }

    private static void addArmorTrait(Material material, c4.conarm.lib.traits.AbstractArmorTrait trait) {
        material.addTrait(trait, ArmorMaterialType.CORE);
        material.addTrait(trait, ArmorMaterialType.PLATES);
        material.addTrait(trait, ArmorMaterialType.TRIM);
    }

    @SideOnly(Side.CLIENT)
    private static void registerArmorColors() {
        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        for (Item item : Item.REGISTRY) {
            if (item instanceof c4.conarm.lib.armor.ArmorCore) {
                itemColors.registerItemColorHandler((stack, tintIndex) -> {
                    List<Material> materials = TinkerUtil.getMaterialsFromTagList(
                            TagUtil.getBaseMaterialsTagList(stack)
                    );
                    if (tintIndex < materials.size()) {
                        Material mat = materials.get(tintIndex);
                        if (OUR_MATERIALS.contains(mat.identifier)) {
                            return mat.materialTextColor;
                        }
                    }
                    return -1;
                }, item);
            }
        }
    }
}