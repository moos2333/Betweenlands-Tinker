package com.npstra.tinkerbetweenlands.content.recipe;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.tools.melee.item.BroadSword;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.api.recipes.IAnimatorRecipe;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;
import com.npstra.tinkerbetweenlands.content.tools.ModTools;

public class ToolConversionRecipe implements IAnimatorRecipe {

    private static final Item PICKAXE = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct", "pickaxe"));
    private static final Item SHOVEL = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct", "shovel"));
    private static final Item HATCHET = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct", "hatchet"));
    private static final Item SHORTBOW = ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct", "shortbow"));

    @Override
    public boolean matchesInput(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Item item = stack.getItem();
        if (!(item instanceof ToolCore)) return false;
        if (item instanceof IBetweenlandsTool) return false;
        return item == PICKAXE || item == SHOVEL || item == HATCHET || item instanceof BroadSword || item == SHORTBOW;
    }

    @Override
    public int getRequiredFuel(ItemStack stack) {
        return 16;
    }

    @Override
    public int getRequiredLife(ItemStack stack) {
        return 32;
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
        if (item == PICKAXE) return new ItemStack(ModTools.BETWEEN_PICKAXE);
        if (item == SHOVEL) return new ItemStack(ModTools.BETWEEN_SHOVEL);
        if (item == HATCHET) return new ItemStack(ModTools.BETWEEN_HATCHET);
        if (item instanceof BroadSword) return new ItemStack(ModTools.BETWEEN_BROADSWORD);
        if (item == SHORTBOW) return new ItemStack(ModTools.BETWEEN_SHORTBOW);
        return ItemStack.EMPTY;
    }

    @Override
    public Class<? extends Entity> getSpawnEntityClass(ItemStack stack) {
        return null;
    }

    @Override
    public ItemStack onAnimated(World world, BlockPos pos, ItemStack stack) {
        return ItemStack.EMPTY;
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