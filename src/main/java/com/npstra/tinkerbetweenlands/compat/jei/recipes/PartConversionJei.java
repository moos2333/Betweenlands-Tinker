package com.npstra.tinkerbetweenlands.compat.jei.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerTools;
import thebetweenlands.common.recipe.misc.AnimatorRecipe;
import com.npstra.tinkerbetweenlands.content.materials.MaterialRegister;
import com.npstra.tinkerbetweenlands.content.parts.BetweenlandsPart;
import com.npstra.tinkerbetweenlands.content.parts.ModParts;

public class PartConversionJei extends AnimatorRecipe {
    public PartConversionJei() {
        super(withLore(TinkerTools.toolRod.getItemstackWithMaterial(getMaterial())), 2, 5, ModParts.BETWEEN_HANDLE.getItemstackWithMaterial(getMaterial()));
    }

    private static Material getMaterial() {
        return MaterialRegister.syrmorite != null ? MaterialRegister.syrmorite : Material.UNKNOWN;
    }

    private static ItemStack withLore(ItemStack stack) {
        NBTTagCompound display = stack.getOrCreateSubCompound("display");
        NBTTagList lore = new NBTTagList();
        lore.appendTag(new NBTTagString("§7Also works for:"));
        lore.appendTag(new NBTTagString("§7Binding, Sword Blade, Axe Head, Shovel Head, Pick Head"));
        display.setTag("Lore", lore);
        return stack;
    }

    @Override
    public boolean matchesInput(ItemStack stack) { return false; }

    @Override
    public ItemStack getResult(ItemStack stack) {
        ItemStack output = super.getResult(stack).copy();
        Material mat = ((ToolPart) stack.getItem()).getMaterial(stack);
        if (mat != Material.UNKNOWN) {
            output = ((BetweenlandsPart) output.getItem()).getItemstackWithMaterial(mat);
        }
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) output.setTagCompound(tag.copy());
        return output;
    }
}