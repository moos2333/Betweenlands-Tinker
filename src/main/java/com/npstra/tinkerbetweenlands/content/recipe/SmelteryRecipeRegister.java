package com.npstra.tinkerbetweenlands.content.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import com.npstra.tinkerbetweenlands.content.fluid.FluidRegister;

public class SmelteryRecipeRegister {

    public static void init(FMLInitializationEvent event) {
        registerMeltingRecipes();
        registerCastingRecipes();
        registerFuel();
    }

    private static void registerMeltingRecipes() {
        Item octineIngot = Item.getByNameOrId("thebetweenlands:octine_ingot");
        if (octineIngot != null)
            TinkerRegistry.registerMelting(new ItemStack(octineIngot), FluidRegister.fluidOctine, Material.VALUE_Ingot);

        Item syrmoriteIngot = Item.getByNameOrId("thebetweenlands:items_misc");
        if (syrmoriteIngot != null)
            TinkerRegistry.registerMelting(new ItemStack(syrmoriteIngot, 1, 11), FluidRegister.fluidSyrmorite, Material.VALUE_Ingot);

        Item octineNugget = Item.getByNameOrId("thebetweenlands:items_misc");
        if (octineNugget != null)
            TinkerRegistry.registerMelting(new ItemStack(octineNugget, 1, 42), FluidRegister.fluidOctine, Material.VALUE_Nugget);

        Item syrmoriteNugget = Item.getByNameOrId("thebetweenlands:items_misc");
        if (syrmoriteNugget != null)
            TinkerRegistry.registerMelting(new ItemStack(syrmoriteNugget, 1, 41), FluidRegister.fluidSyrmorite, Material.VALUE_Nugget);

        Item octineBlock = Item.getByNameOrId("thebetweenlands:octine_block");
        if (octineBlock != null)
            TinkerRegistry.registerMelting(new ItemStack(octineBlock), FluidRegister.fluidOctine, Material.VALUE_Block);

        Item syrmoriteBlock = Item.getByNameOrId("thebetweenlands:syrmorite_block");
        if (syrmoriteBlock != null)
            TinkerRegistry.registerMelting(new ItemStack(syrmoriteBlock), FluidRegister.fluidSyrmorite, Material.VALUE_Block);

        Item octineOre = Item.getByNameOrId("thebetweenlands:octine_ore");
        if (octineOre != null)
            TinkerRegistry.registerMelting(new ItemStack(octineOre), FluidRegister.fluidOctine, 288);

        Item syrmoriteOre = Item.getByNameOrId("thebetweenlands:syrmorite_ore");
        if (syrmoriteOre != null)
            TinkerRegistry.registerMelting(new ItemStack(syrmoriteOre), FluidRegister.fluidSyrmorite, 288);

        Item sulfurItem = Item.getByNameOrId("thebetweenlands:items_misc");
        if (sulfurItem != null)
            TinkerRegistry.registerMelting(new ItemStack(sulfurItem, 1, 18), FluidRegister.fluidMoltenSulfur, 50);

        Item sulfurBlock = Item.getByNameOrId("thebetweenlands:sulfur_block");
        if (sulfurBlock != null)
            TinkerRegistry.registerMelting(new ItemStack(sulfurBlock), FluidRegister.fluidMoltenSulfur, 450);
    }

    private static void registerCastingRecipes() {
        Fluid fluidOctine = FluidRegister.fluidOctine;
        Fluid fluidSyrmorite = FluidRegister.fluidSyrmorite;

        if (fluidOctine == null || fluidSyrmorite == null) {
            return;
        }

        ItemStack octineNugget = new ItemStack(Item.getByNameOrId("thebetweenlands:items_misc"), 1, 42);
        ItemStack octineIngot = new ItemStack(Item.getByNameOrId("thebetweenlands:octine_ingot"));
        ItemStack octineBlock = new ItemStack(Item.getByNameOrId("thebetweenlands:octine_block"));

        TinkerRegistry.registerTableCasting(octineNugget, new ItemStack(TinkerSmeltery.castNugget.getItem()), fluidOctine, Material.VALUE_Nugget);
        TinkerRegistry.registerTableCasting(octineIngot, new ItemStack(TinkerSmeltery.castIngot.getItem()), fluidOctine, Material.VALUE_Ingot);
        TinkerRegistry.registerBasinCasting(octineBlock, ItemStack.EMPTY, fluidOctine, Material.VALUE_Block);

        ItemStack syrmoriteNugget = new ItemStack(Item.getByNameOrId("thebetweenlands:items_misc"), 1, 41);
        ItemStack syrmoriteIngot = new ItemStack(Item.getByNameOrId("thebetweenlands:items_misc"), 1, 11);
        ItemStack syrmoriteBlock = new ItemStack(Item.getByNameOrId("thebetweenlands:syrmorite_block"));

        TinkerRegistry.registerTableCasting(syrmoriteNugget, new ItemStack(TinkerSmeltery.castNugget.getItem()), fluidSyrmorite, Material.VALUE_Nugget);
        TinkerRegistry.registerTableCasting(syrmoriteIngot, new ItemStack(TinkerSmeltery.castIngot.getItem()), fluidSyrmorite, Material.VALUE_Ingot);
        TinkerRegistry.registerBasinCasting(syrmoriteBlock, ItemStack.EMPTY, fluidSyrmorite, Material.VALUE_Block);

        Fluid fluidSulfur = FluidRegister.fluidMoltenSulfur;
        if (fluidSulfur != null) {
            ItemStack sulfurItem = new ItemStack(Item.getByNameOrId("thebetweenlands:items_misc"), 1, 18);
            ItemStack sulfurBlock = new ItemStack(Item.getByNameOrId("thebetweenlands:sulfur_block"));
            if (!sulfurItem.isEmpty()) {
                TinkerRegistry.registerTableCasting(sulfurItem, new ItemStack(TinkerSmeltery.castGem.getItem()), fluidSulfur, 50);
            }
            if (!sulfurBlock.isEmpty()) {
                TinkerRegistry.registerBasinCasting(sulfurBlock, ItemStack.EMPTY, fluidSulfur, 450);
            }
        }
    }

    private static void registerFuel() {
        if (FluidRegister.fluidMoltenSulfur != null) {
            TinkerRegistry.registerSmelteryFuel(new FluidStack(FluidRegister.fluidMoltenSulfur, 50), 100);
        }
    }
}