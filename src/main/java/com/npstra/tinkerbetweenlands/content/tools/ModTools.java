package com.npstra.tinkerbetweenlands.content.tools;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.tools.TinkerModifiers;

import static slimeknights.tconstruct.common.ModelRegisterUtil.registerModifierModel;

@Mod.EventBusSubscriber
public class ModTools {
    public static final BetweenPickaxe BETWEEN_PICKAXE = new BetweenPickaxe();
    public static final BetweenShovel BETWEEN_SHOVEL = new BetweenShovel();
    public static final BetweenHatchet BETWEEN_HATCHET = new BetweenHatchet();
    public static final BetweenBroadSword BETWEEN_BROADSWORD = new BetweenBroadSword();

    public static void register() {
        ForgeRegistries.ITEMS.register(BETWEEN_PICKAXE.setRegistryName("between_pickaxe"));
        ForgeRegistries.ITEMS.register(BETWEEN_SHOVEL.setRegistryName("between_shovel"));
        ForgeRegistries.ITEMS.register(BETWEEN_HATCHET.setRegistryName("between_hatchet"));
        ForgeRegistries.ITEMS.register(BETWEEN_BROADSWORD.setRegistryName("between_broadsword"));
        TinkerRegistry.registerTool(BETWEEN_PICKAXE);
        TinkerRegistry.registerTool(BETWEEN_SHOVEL);
        TinkerRegistry.registerTool(BETWEEN_HATCHET);
        TinkerRegistry.registerTool(BETWEEN_BROADSWORD);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if (BETWEEN_PICKAXE != null) ModelRegisterUtil.registerToolModel(BETWEEN_PICKAXE);
        if (BETWEEN_SHOVEL != null) ModelRegisterUtil.registerToolModel(BETWEEN_SHOVEL);
        if (BETWEEN_HATCHET != null) ModelRegisterUtil.registerToolModel(BETWEEN_HATCHET);
        if (BETWEEN_BROADSWORD != null) ModelRegisterUtil.registerToolModel(BETWEEN_BROADSWORD);
        registerModifierModels();
    }

    @SideOnly(Side.CLIENT)
    private static void registerModifierModels() {
        registerModifierModel(TinkerModifiers.modSharpness, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/sharpness"));
        registerModifierModel(TinkerModifiers.modDiamond, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/diamond"));
        registerModifierModel(TinkerModifiers.modEmerald, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/emerald"));
        registerModifierModel(TinkerModifiers.modHaste, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/haste"));
        registerModifierModel(TinkerModifiers.modFiery, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/fiery"));
        registerModifierModel(TinkerModifiers.modKnockback, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/knockback"));
        registerModifierModel(TinkerModifiers.modBaneOfArthopods, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/bane_of_arthopods"));
        registerModifierModel(TinkerModifiers.modBeheading, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/beheading"));
        registerModifierModel(TinkerModifiers.modGlowing, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/glowing"));
        registerModifierModel(TinkerModifiers.modLuck, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/luck"));
        registerModifierModel(TinkerModifiers.modMendingMoss, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/mending_moss"));
        registerModifierModel(TinkerModifiers.modNecrotic, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/necrotic"));
        registerModifierModel(TinkerModifiers.modReinforced, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/reinforced"));
        registerModifierModel(TinkerModifiers.modShulking, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/shulking"));
        registerModifierModel(TinkerModifiers.modSilktouch, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/silktouch"));
        registerModifierModel(TinkerModifiers.modSmite, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/smite"));
        registerModifierModel(TinkerModifiers.modSoulbound, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/soulbound"));
        registerModifierModel(TinkerModifiers.modWebbed, new ResourceLocation("tinkerbetweenlands:models/item/modifiers/webbed"));
    }
}