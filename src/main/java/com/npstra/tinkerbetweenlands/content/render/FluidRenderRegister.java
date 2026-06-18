package com.npstra.tinkerbetweenlands.content.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import slimeknights.tconstruct.library.fluid.FluidMolten;

@Mod.EventBusSubscriber
public class FluidRenderRegister {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerFluidTextures(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(FluidMolten.ICON_MetalStill);
        event.getMap().registerSprite(FluidMolten.ICON_MetalFlowing);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerColors(ModelRegistryEvent event) {
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();
        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();

        Fluid octine = FluidRegistry.getFluid("octine");
        if (octine != null && octine.getBlock() != null) {
            blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> octine.getColor(), octine.getBlock());
            Item octineItem = Item.getItemFromBlock(octine.getBlock());
            if (octineItem != null) {
                itemColors.registerItemColorHandler((stack, tintIndex) -> octine.getColor(), octineItem);
            }
        }

        Fluid syrmorite = FluidRegistry.getFluid("syrmorite");
        if (syrmorite != null && syrmorite.getBlock() != null) {
            blockColors.registerBlockColorHandler((state, world, pos, tintIndex) -> syrmorite.getColor(), syrmorite.getBlock());
            Item syrmoriteItem = Item.getItemFromBlock(syrmorite.getBlock());
            if (syrmoriteItem != null) {
                itemColors.registerItemColorHandler((stack, tintIndex) -> syrmorite.getColor(), syrmoriteItem);
            }
        }
    }
}