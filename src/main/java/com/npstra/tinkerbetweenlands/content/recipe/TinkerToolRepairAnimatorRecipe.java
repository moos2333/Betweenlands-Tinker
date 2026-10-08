package com.npstra.tinkerbetweenlands.content.recipe;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import thebetweenlands.api.recipes.IAnimatorRecipe;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;

public class TinkerToolRepairAnimatorRecipe implements IAnimatorRecipe {

    private static final int MIN_FUEL = 4;
    private static final int FULL_FUEL = 12;
    private static final int MIN_LIFE = 8;
    private static final int FULL_LIFE = 24;

    @Override
    public boolean matchesInput(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof IBetweenlandsTool)) return false;
        return stack.getItemDamage() > 0 || ToolHelper.isBroken(stack);
    }

    @Override
    public int getRequiredFuel(ItemStack stack) {
        int max = stack.getMaxDamage();
        if (max <= 0) return MIN_FUEL;
        int damage = Math.min(stack.getItemDamage(), max);
        return MIN_FUEL + MathHelper.ceil((float) (FULL_FUEL - MIN_FUEL) / (float) max * (float) damage);
    }

    @Override
    public int getRequiredLife(ItemStack stack) {
        int max = stack.getMaxDamage();
        if (max <= 0) return MIN_LIFE;
        int damage = Math.min(stack.getItemDamage(), max);
        return MIN_LIFE + MathHelper.ceil((float) (FULL_LIFE - MIN_LIFE) / (float) max * (float) damage);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Entity getRenderEntity(ItemStack stack) {
        return null;
    }

    @Override
    public ItemStack getResult(ItemStack stack) {
        ItemStack result = stack.copy();
        result.setItemDamage(0);
        NBTTagCompound toolTag = TagUtil.getToolTag(result);
        if (toolTag.getBoolean("Broken")) {
            toolTag.setBoolean("Broken", false);
            TagUtil.setToolTag(result, toolTag);
        }
        return result;
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