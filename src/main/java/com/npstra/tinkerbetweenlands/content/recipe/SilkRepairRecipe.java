package com.npstra.tinkerbetweenlands.content.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import thebetweenlands.common.registries.ItemRegistry;

public class SilkRepairRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {

    private static final int MAX_REPAIR_PERCENT = 75;
    private static final int REPAIR_PER_SILK = 5;

    public SilkRepairRecipe() {
        setRegistryName(Util.getResource("silk_repair"));
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        return !getRepairedTool(inv, true).isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return getRepairedTool(inv, false);
    }

    private ItemStack getRepairedTool(InventoryCrafting inv, boolean simulate) {
        ItemStack tool = null;
        int silkCount = 0;

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack slot = inv.getStackInSlot(i);
            if (slot.isEmpty()) continue;

            if (slot.getItem() instanceof ToolCore) {
                if (tool != null) return ItemStack.EMPTY;
                tool = slot.copy();
                continue;
            }

            if (slot.getItem() == ItemRegistry.ITEMS_MISC && slot.getMetadata() == 63) {
                silkCount += slot.getCount();
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (tool == null || silkCount == 0) return ItemStack.EMPTY;
        if (!TinkerUtil.hasModifier(TagUtil.getTagSafe(tool), "silk_thread")) return ItemStack.EMPTY;

        int maxDamage = tool.getMaxDamage();
        int currentDamage = tool.getItemDamage();
        int maxRepairable = maxDamage * (100 - MAX_REPAIR_PERCENT) / 100;
        if (currentDamage <= maxRepairable) return ItemStack.EMPTY;

        int maxSilk = Math.min(silkCount, 8);
        int repairAmount = (int) (maxDamage * REPAIR_PER_SILK / 100.0f * maxSilk);
        int newDamage = Math.max(maxRepairable, currentDamage - repairAmount);

        if (!simulate) {
            ToolHelper.repairTool(tool, currentDamage - newDamage);
        } else {
            tool.setItemDamage(newDamage);
        }
        return tool;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @Override
    public boolean canFit(int width, int height) {
        return width >= 3 && height >= 3;
    }
}