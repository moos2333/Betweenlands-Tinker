package com.npstra.tinkerbetweenlands.content.render;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.fluid.FluidMolten;

@Mod.EventBusSubscriber
public class FluidRenderRegister {

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void registerFluidTextures(TextureStitchEvent.Pre event) {
        event.getMap().registerSprite(FluidMolten.ICON_MetalStill);
        event.getMap().registerSprite(FluidMolten.ICON_MetalFlowing);
    }
}