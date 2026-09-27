package com.npstra.tinkerbetweenlands.content.recipe;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.tools.TinkerTools;

@Mod.EventBusSubscriber
public class ToolForgeRegister {

    @SubscribeEvent
    public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
        TinkerTools.registerToolForgeBlock(event.getRegistry(), "blockValonite");
        TinkerTools.registerToolForgeBlock(event.getRegistry(), "blockOctine");
        TinkerTools.registerToolForgeBlock(event.getRegistry(), "blockSyrmorite");
    }
}