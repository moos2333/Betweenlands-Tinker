package com.npstra.tinkerbetweenlands.content.tools;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.common.ModelRegisterUtil;
import slimeknights.tconstruct.library.TinkerRegistry;

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
        if (BETWEEN_PICKAXE != null) {
            ModelRegisterUtil.registerToolModel(BETWEEN_PICKAXE);
        }
        if (BETWEEN_SHOVEL != null) {
            ModelRegisterUtil.registerToolModel(BETWEEN_SHOVEL);
        }
        if (BETWEEN_HATCHET != null) {
            ModelRegisterUtil.registerToolModel(BETWEEN_HATCHET);
        }
        if (BETWEEN_BROADSWORD != null) {
            ModelRegisterUtil.registerToolModel(BETWEEN_BROADSWORD);
        }
    }
}