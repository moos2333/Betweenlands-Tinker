package com.npstra.tinkerbetweenlands.content.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.tconstruct.library.fluid.FluidMolten;

public class FluidRegister {
    public static Fluid fluidOctine;
    public static Fluid fluidSyrmorite;
    public static Fluid fluidMoltenSulfur;

    public static void preInit() {
        fluidOctine = new FluidMolten("octine", 0xF5A615);
        fluidSyrmorite = new FluidMolten("syrmorite", 0x5660A5);
        fluidMoltenSulfur = new FluidMolten("molten_sulfur", 0xE8C92D);
        fluidMoltenSulfur.setTemperature(1050);
        FluidRegistry.registerFluid(fluidOctine);
        FluidRegistry.registerFluid(fluidSyrmorite);
        FluidRegistry.registerFluid(fluidMoltenSulfur);
    }
}