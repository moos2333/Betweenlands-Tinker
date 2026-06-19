package com.npstra.tinkerbetweenlands.content.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.IForgeRegistryEntry;
import thebetweenlands.common.capability.circlegem.CircleGemHelper;
import thebetweenlands.common.capability.circlegem.CircleGemType;
import thebetweenlands.common.item.misc.ItemGem;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;

public class GemAttachmentRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        ItemStack tool = ItemStack.EMPTY;
        ItemStack gem = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemGem) {
                    if (!gem.isEmpty()) return false;
                    gem = stack;
                } else if (stack.getItem() instanceof IBetweenlandsTool) {
                    if (!tool.isEmpty()) return false;
                    tool = stack;
                } else {
                    return false;
                }
            }
        }
        return !tool.isEmpty() && !gem.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack tool = ItemStack.EMPTY;
        ItemStack gem = ItemStack.EMPTY;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty()) {
                if (stack.getItem() instanceof ItemGem) {
                    gem = stack;
                } else if (stack.getItem() instanceof IBetweenlandsTool) {
                    tool = stack;
                }
            }
        }
        if (!tool.isEmpty() && !gem.isEmpty()) {
            ItemStack result = tool.copy();
            CircleGemType applied = ((ItemGem) gem.getItem()).type;
            CircleGemType existing = CircleGemHelper.getGem(result);
            if (existing == applied) {
                CircleGemHelper.setGem(result, CircleGemType.NONE);
            } else {
                CircleGemHelper.setGem(result, applied);
            }
            return result;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < remaining.size(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            remaining.set(i, ForgeHooks.getContainerItem(stack));
        }
        return remaining;
    }
}