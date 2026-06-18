package com.npstra.tinkerbetweenlands.content.recipe;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.api.recipes.IAnimatorRecipe;
import slimeknights.tconstruct.library.tools.ToolCore;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;
import com.npstra.tinkerbetweenlands.content.tools.ModTools;

public class ToolConversionRecipe implements IAnimatorRecipe {
    @Override
    public boolean matchesInput(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (!(item instanceof ToolCore)) return false;
        if (item instanceof IBetweenlandsTool) return false;
        return item instanceof slimeknights.tconstruct.tools.tools.Pickaxe ||
                item instanceof slimeknights.tconstruct.tools.tools.Shovel ||
                item instanceof slimeknights.tconstruct.tools.tools.Hatchet ||
                item instanceof slimeknights.tconstruct.tools.melee.item.BroadSword;
    }

    @Override
    public int getRequiredFuel(ItemStack stack) {
        return 10;
    }

    @Override
    public int getRequiredLife(ItemStack stack) {
        return 25;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Entity getRenderEntity(ItemStack stack) {
        return null;
    }

    @Override
    public ItemStack getResult(ItemStack stack) {
        if (!matchesInput(stack)) return ItemStack.EMPTY;
        ItemStack output = getOutputTool(stack);
        if (output.isEmpty()) return ItemStack.EMPTY;
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) output.setTagCompound(tag.copy());
        if (output.getItem() instanceof ICorrodible) {
            ((ICorrodible) output.getItem()).setCorrosion(output, 0);
        }
        return output;
    }

    private ItemStack getOutputTool(ItemStack input) {
        Item item = input.getItem();
        if (item instanceof slimeknights.tconstruct.tools.tools.Pickaxe)
            return new ItemStack(ModTools.BETWEEN_PICKAXE);
        if (item instanceof slimeknights.tconstruct.tools.tools.Shovel)
            return new ItemStack(ModTools.BETWEEN_SHOVEL);
        if (item instanceof slimeknights.tconstruct.tools.tools.Hatchet)
            return new ItemStack(ModTools.BETWEEN_HATCHET);
        if (item instanceof slimeknights.tconstruct.tools.melee.item.BroadSword)
            return new ItemStack(ModTools.BETWEEN_BROADSWORD);
        return ItemStack.EMPTY;
    }

    @Override
    public Class<? extends Entity> getSpawnEntityClass(ItemStack stack) {
        return null;
    }

    @Override
    public ItemStack onAnimated(World world, BlockPos pos, ItemStack stack) {
        return getResult(stack);
    }

    @Override
    public boolean onRetrieved(World world, BlockPos pos, ItemStack stack) {
        return true;
    }

    @Override
    public boolean getCloseOnFinish(ItemStack stack) {
        return false;
    }
}