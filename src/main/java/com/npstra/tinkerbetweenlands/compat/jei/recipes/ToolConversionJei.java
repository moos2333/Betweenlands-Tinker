package com.npstra.tinkerbetweenlands.compat.jei.recipes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolCore;
import thebetweenlands.common.recipe.misc.AnimatorRecipe;
import com.npstra.tinkerbetweenlands.content.materials.MaterialRegister;
import com.npstra.tinkerbetweenlands.content.tools.ModTools;

import java.util.Collections;
import java.util.List;

public class ToolConversionJei extends AnimatorRecipe {
    private static final Item INPUT_ITEM = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct", "broadsword"));
    private static final ToolCore OUTPUT_ITEM = ModTools.BETWEEN_BROADSWORD;

    public ToolConversionJei() {
        super(withLore(createToolItem(INPUT_ITEM, getMaterial())), 16, 25, createToolItem(OUTPUT_ITEM, getMaterial()));
    }

    private static Material getMaterial() {
        return MaterialRegister.syrmorite != null ? MaterialRegister.syrmorite : Material.UNKNOWN;
    }

    private static ItemStack createToolItem(Item item, Material mat) {
        if (!(item instanceof ToolCore)) return ItemStack.EMPTY;
        int parts = ((ToolCore) item).getRequiredComponents().size();
        List<Material> mats = Collections.nCopies(parts, mat);
        return ((ToolCore) item).buildItem(mats);
    }

    private static ItemStack withLore(ItemStack stack) {
        NBTTagCompound display = stack.getOrCreateSubCompound("display");
        NBTTagList lore = new NBTTagList();
        lore.appendTag(new NBTTagString("§7Also works for:"));
        lore.appendTag(new NBTTagString("§7Pickaxe, Shovel, Hatchet"));
        display.setTag("Lore", lore);
        return stack;
    }

    @Override
    public boolean matchesInput(ItemStack stack) { return false; }

    @Override
    public ItemStack getResult(ItemStack stack) {
        ItemStack output = super.getResult(stack).copy();
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) output.setTagCompound(tag.copy());
        return output;
    }
}