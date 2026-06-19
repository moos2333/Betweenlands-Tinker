package com.npstra.tinkerbetweenlands.compat.jei.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import thebetweenlands.common.recipe.misc.AnimatorRecipe;
import com.npstra.tinkerbetweenlands.content.tools.ModTools;

public class ToolConversionJei extends AnimatorRecipe {
    public ToolConversionJei() {
        super(withLore(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct", "broadsword")))), 16, 25, new ItemStack(ModTools.BETWEEN_BROADSWORD));
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