package com.npstra.tinkerbetweenlands.content.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.fluid.FluidMolten;
import slimeknights.tconstruct.smeltery.block.BlockMolten;

public class FluidRegister {
    public static Fluid fluidOctine;
    public static Fluid fluidSyrmorite;

    public static void preInit() {
        fluidOctine = new FluidMolten("octine", 0xF5A615);
        fluidSyrmorite = new FluidMolten("syrmorite", 0x5660A5);
        FluidRegistry.registerFluid(fluidOctine);
        FluidRegistry.registerFluid(fluidSyrmorite);

        BlockMolten octineBlock = new BlockMolten(fluidOctine);
        octineBlock.setRegistryName("octine_fluid");
        octineBlock.setTranslationKey("octine_fluid");
        ForgeRegistries.BLOCKS.register(octineBlock);
        fluidOctine.setBlock(octineBlock);

        BlockMolten syrmoriteBlock = new BlockMolten(fluidSyrmorite);
        syrmoriteBlock.setRegistryName("syrmorite_fluid");
        syrmoriteBlock.setTranslationKey("syrmorite_fluid");
        ForgeRegistries.BLOCKS.register(syrmoriteBlock);
        fluidSyrmorite.setBlock(syrmoriteBlock);
    }
}