package com.npstra.tinkerbetweenlands.content.parts;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.common.ModelRegisterUtil;

@Mod.EventBusSubscriber
public class ModParts {
    public static final BetweenlandsPart BETWEEN_HEAD = new BetweenlandsPart(0, "head");
    public static final BetweenlandsPart BETWEEN_HANDLE = new BetweenlandsPart(0, "handle");
    public static final BetweenlandsPart BETWEEN_EXTRA = new BetweenlandsPart(0, "extra");

    public static void register() {
        ForgeRegistries.ITEMS.register(BETWEEN_HEAD.setRegistryName("between_head").setTranslationKey("tinkerbetweenlands.between_head"));
        ForgeRegistries.ITEMS.register(BETWEEN_HANDLE.setRegistryName("between_handle").setTranslationKey("tinkerbetweenlands.between_handle"));
        ForgeRegistries.ITEMS.register(BETWEEN_EXTRA.setRegistryName("between_extra").setTranslationKey("tinkerbetweenlands.between_extra"));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        if (BETWEEN_HEAD != null) {
            ModelRegisterUtil.registerPartModel(BETWEEN_HEAD);
        }
        if (BETWEEN_HANDLE != null) {
            ModelRegisterUtil.registerPartModel(BETWEEN_HANDLE);
        }
        if (BETWEEN_EXTRA != null) {
            ModelRegisterUtil.registerPartModel(BETWEEN_EXTRA);
        }
    }
}