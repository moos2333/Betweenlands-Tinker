package com.npstra.tinkerbetweenlands.common.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
    public static final ItemBetweenlandsTinkerBook BETWEENLANDS_TINKER_BOOK = new ItemBetweenlandsTinkerBook();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.register(BETWEENLANDS_TINKER_BOOK.setRegistryName("tinkerbetweenlands:betweenlands_tinker_book"));
    }
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(BETWEENLANDS_TINKER_BOOK, 0, new ModelResourceLocation("tinkerbetweenlands:betweenlands_tinker_book", "inventory"));
    }
}
