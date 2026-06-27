package com.npstra.tinkerbetweenlands.content.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;
import com.npstra.tinkerbetweenlands.content.fluid.FluidRegister;

public class SmelteryRecipeRegister {

    public static void init(FMLInitializationEvent event) {
        registerMeltingRecipes();
        registerCastingRecipes();
        registerFuel();
    }

    private static void registerMeltingRecipes() {
        TinkerRegistry.registerMelting(new ItemStack(ItemRegistry.OCTINE_INGOT), FluidRegister.fluidOctine, Material.VALUE_Ingot);
        TinkerRegistry.registerMelting(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 11), FluidRegister.fluidSyrmorite, Material.VALUE_Ingot);
        TinkerRegistry.registerMelting(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 42), FluidRegister.fluidOctine, Material.VALUE_Nugget);
        TinkerRegistry.registerMelting(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 41), FluidRegister.fluidSyrmorite, Material.VALUE_Nugget);
        TinkerRegistry.registerMelting(new ItemStack(BlockRegistry.OCTINE_BLOCK), FluidRegister.fluidOctine, Material.VALUE_Block);
        TinkerRegistry.registerMelting(new ItemStack(BlockRegistry.SYRMORITE_BLOCK), FluidRegister.fluidSyrmorite, Material.VALUE_Block);
        TinkerRegistry.registerMelting(new ItemStack(BlockRegistry.OCTINE_ORE), FluidRegister.fluidOctine, 288);
        TinkerRegistry.registerMelting(new ItemStack(BlockRegistry.SYRMORITE_ORE), FluidRegister.fluidSyrmorite, 288);
        TinkerRegistry.registerMelting(new ItemStack(ItemRegistry.ITEMS_MISC, 1, 18), FluidRegister.fluidMoltenSulfur, 50);
        TinkerRegistry.registerMelting(new ItemStack(BlockRegistry.SULFUR_BLOCK), FluidRegister.fluidMoltenSulfur, 450);
    }

    private static void registerCastingRecipes() {
        if (FluidRegister.fluidSyrmorite != null) {
            TinkerSmeltery.castCreationFluids.add(new FluidStack(FluidRegister.fluidSyrmorite, 288));
        }

        ItemStack octineNugget = new ItemStack(ItemRegistry.ITEMS_MISC, 1, 42);
        ItemStack octineIngot = new ItemStack(ItemRegistry.OCTINE_INGOT);
        ItemStack octineBlock = new ItemStack(BlockRegistry.OCTINE_BLOCK);
        TinkerRegistry.registerTableCasting(octineNugget, new ItemStack(TinkerSmeltery.castNugget.getItem()), FluidRegister.fluidOctine, Material.VALUE_Nugget);
        TinkerRegistry.registerTableCasting(octineIngot, new ItemStack(TinkerSmeltery.castIngot.getItem()), FluidRegister.fluidOctine, Material.VALUE_Ingot);
        TinkerRegistry.registerBasinCasting(octineBlock, ItemStack.EMPTY, FluidRegister.fluidOctine, Material.VALUE_Block);

        ItemStack syrmoriteNugget = new ItemStack(ItemRegistry.ITEMS_MISC, 1, 41);
        ItemStack syrmoriteIngot = new ItemStack(ItemRegistry.ITEMS_MISC, 1, 11);
        ItemStack syrmoriteBlock = new ItemStack(BlockRegistry.SYRMORITE_BLOCK);
        TinkerRegistry.registerTableCasting(syrmoriteNugget, new ItemStack(TinkerSmeltery.castNugget.getItem()), FluidRegister.fluidSyrmorite, Material.VALUE_Nugget);
        TinkerRegistry.registerTableCasting(syrmoriteIngot, new ItemStack(TinkerSmeltery.castIngot.getItem()), FluidRegister.fluidSyrmorite, Material.VALUE_Ingot);
        TinkerRegistry.registerBasinCasting(syrmoriteBlock, ItemStack.EMPTY, FluidRegister.fluidSyrmorite, Material.VALUE_Block);

        if (FluidRegister.fluidMoltenSulfur != null) {
            ItemStack sulfurItem = new ItemStack(ItemRegistry.ITEMS_MISC, 1, 18);
            ItemStack sulfurBlock = new ItemStack(BlockRegistry.SULFUR_BLOCK);
            TinkerRegistry.registerTableCasting(sulfurItem, TinkerSmeltery.castGem, FluidRegister.fluidMoltenSulfur, 50);
            TinkerRegistry.registerBasinCasting(sulfurBlock, ItemStack.EMPTY, FluidRegister.fluidMoltenSulfur, 450);
        }
    }

    private static void registerFuel() {
        if (FluidRegister.fluidMoltenSulfur != null) {
            TinkerRegistry.registerSmelteryFuel(new FluidStack(FluidRegister.fluidMoltenSulfur, 50), 100);
        }
        if (FluidRegister.fluidOctine != null) {
            TinkerRegistry.registerSmelteryFuel(new FluidStack(FluidRegister.fluidOctine, 50), 100);
        }
    }
}