package com.npstra.tinkerbetweenlands.content.parts;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.tools.ToolPart;

@Mod.EventBusSubscriber
public class ModParts {
    public static final BetweenlandsPart BETWEEN_HEAD = new BetweenlandsPart(0, "head");
    public static final BetweenlandsPart BETWEEN_HANDLE = new BetweenlandsPart(0, "handle");
    public static final BetweenlandsPart BETWEEN_EXTRA = new BetweenlandsPart(0, "extra");
    public static final BetweenlandsPart BETWEENLANDS_LIMB = new BetweenlandsPart(0, "bow");
    public static final BetweenlandsPart BETWEENLANDS_BOWSTRING = new BetweenlandsPart(0, "bowstring");

    public static void register() {
        ForgeRegistries.ITEMS.register(BETWEEN_HEAD.setRegistryName("between_head").setTranslationKey("tinkerbetweenlands.between_head"));
        ForgeRegistries.ITEMS.register(BETWEEN_HANDLE.setRegistryName("between_handle").setTranslationKey("tinkerbetweenlands.between_handle"));
        ForgeRegistries.ITEMS.register(BETWEEN_EXTRA.setRegistryName("between_extra").setTranslationKey("tinkerbetweenlands.between_extra"));
        ForgeRegistries.ITEMS.register(BETWEENLANDS_LIMB.setRegistryName("between_limb").setTranslationKey("tinkerbetweenlands.between_limb"));
        ForgeRegistries.ITEMS.register(BETWEENLANDS_BOWSTRING.setRegistryName("between_bowstring").setTranslationKey("tinkerbetweenlands.between_bowstring"));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if (BETWEEN_HEAD != null) ModelRegisterUtil.registerPartModel(BETWEEN_HEAD);
        if (BETWEEN_HANDLE != null) ModelRegisterUtil.registerPartModel(BETWEEN_HANDLE);
        if (BETWEEN_EXTRA != null) ModelRegisterUtil.registerPartModel(BETWEEN_EXTRA);
        if (BETWEENLANDS_LIMB != null) ModelRegisterUtil.registerPartModel(BETWEENLANDS_LIMB);
        if (BETWEENLANDS_BOWSTRING != null) ModelRegisterUtil.registerPartModel(BETWEENLANDS_BOWSTRING);
    }
}