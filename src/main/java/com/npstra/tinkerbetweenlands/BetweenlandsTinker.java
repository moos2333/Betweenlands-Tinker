package com.npstra.tinkerbetweenlands;

import com.npstra.tinkerbetweenlands.common.item.ItemBetweenlandsTinkerBook;
import com.npstra.tinkerbetweenlands.common.item.ModItems;
import com.npstra.tinkerbetweenlands.compat.conarm.materials.ArmorMaterial;
import com.npstra.tinkerbetweenlands.content.event.SulfurLeachEventHandler;
import com.npstra.tinkerbetweenlands.content.fluid.FluidRegister;
import com.npstra.tinkerbetweenlands.content.modifiers.ModifierRegister;
import com.npstra.tinkerbetweenlands.content.recipe.GemAttachmentRecipe;
import com.npstra.tinkerbetweenlands.content.recipe.SmelteryRecipeRegister;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.mantle.client.book.BookLoader;
import slimeknights.mantle.client.book.repository.FileRepository;
import slimeknights.tconstruct.library.book.content.ContentTool;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import thebetweenlands.common.handler.OverworldItemHandler;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;
import com.npstra.tinkerbetweenlands.content.event.BetweenlandsEventHandler;
import com.npstra.tinkerbetweenlands.content.materials.MaterialRegister;
import com.npstra.tinkerbetweenlands.content.parts.ModParts;
import com.npstra.tinkerbetweenlands.content.recipe.PartConversionRecipe;
import com.npstra.tinkerbetweenlands.content.recipe.ToolConversionRecipe;
import com.npstra.tinkerbetweenlands.content.tools.ModTools;
import com.npstra.tinkerbetweenlands.config.ModConfig;
import thebetweenlands.common.recipe.misc.AnimatorRecipe;

@Mod(modid = Tags.MOD_ID, name = Tags.MOD_NAME, version = Tags.VERSION, dependencies = "required-after:mantle@[1.12-1.3.3.55,);required-after:tconstruct@[1.12.2-2.13.0,);required-after:thebetweenlands@[1.12.2-3.9.0,)")
public class BetweenlandsTinker {
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModConfig.init(event);
        ModParts.register();
        ModTools.register();
        FluidRegister.preInit();
        MaterialRegister.preInit();
        if (Loader.isModLoaded("conarm")) {
            ArmorMaterial.registerArmorStats();
        }
        if (ModConfig.enableToolWeakening) {
            OverworldItemHandler.TOOL_BLACKLIST.put(
                    new ResourceLocation("tinkerbetweenlands", "tinkers_tools"),
                    stack -> {
                        if (stack.getItem() instanceof ToolCore) {
                            if (stack.getItem() instanceof IBetweenlandsTool) return false;
                            if (TinkerUtil.hasTrait(TagUtil.getTagSafe(stack), "between")) return false;
                            return true;
                        }
                        return false;
                    }
            );
        }
        OverworldItemHandler.TOOL_WHITELIST.put(
                new ResourceLocation(Tags.MOD_ID, "betweenlands_tools"),
                stack -> {
                    if (stack.getItem() instanceof ToolCore) {
                        if (stack.getItem() instanceof IBetweenlandsTool) return true;
                        if (TinkerUtil.hasTrait(TagUtil.getTagSafe(stack), "between")) return true;
                    }
                    return false;
                }
        );
        MinecraftForge.EVENT_BUS.register(ModItems.class);
        ItemBetweenlandsTinkerBook.BOOK_DATA = BookLoader.registerBook(
                "tinkerbetweenlands:betweenlands_tinker_book",
                new FileRepository("tinkerbetweenlands:book"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MaterialRegister.init();
        ModifierRegister.init();
        SmelteryRecipeRegister.init(event);
        AnimatorRecipe.addRecipe(new ToolConversionRecipe());
        AnimatorRecipe.addRecipe(new PartConversionRecipe());

        if (Loader.isModLoaded("jei")) {
            AnimatorRecipe.addRecipe(new com.npstra.tinkerbetweenlands.compat.jei.recipes.ToolConversionJei());
            AnimatorRecipe.addRecipe(new com.npstra.tinkerbetweenlands.compat.jei.recipes.PartConversionJei());
        }

        MinecraftForge.EVENT_BUS.register(new BetweenlandsEventHandler());
        MinecraftForge.EVENT_BUS.register(new SulfurLeachEventHandler());
        ForgeRegistries.RECIPES.register(new GemAttachmentRecipe().setRegistryName(Tags.MOD_ID, "gem_attachment"));
        if (Loader.isModLoaded("conarm")) {
            ArmorMaterial.registerArmorTraits();
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }
}