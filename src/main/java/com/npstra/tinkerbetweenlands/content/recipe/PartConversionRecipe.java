package com.npstra.tinkerbetweenlands.content.recipe;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.ToolPart;
import slimeknights.tconstruct.tools.TinkerTools;
import thebetweenlands.api.recipes.IAnimatorRecipe;
import com.npstra.tinkerbetweenlands.content.parts.BetweenlandsPart;
import com.npstra.tinkerbetweenlands.content.parts.ModParts;

public class PartConversionRecipe implements IAnimatorRecipe {
    @Override
    public boolean matchesInput(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (!(stack.getItem() instanceof IToolPart)) return false;
        if (stack.getItem() instanceof BetweenlandsPart) return false;
        Object item = stack.getItem();
        return item == TinkerTools.toolRod ||
                item == TinkerTools.binding ||
                item == TinkerTools.swordBlade ||
                item == TinkerTools.axeHead ||
                item == TinkerTools.shovelHead ||
                item == TinkerTools.pickHead ||
                item == TinkerTools.bowLimb ||
                item == TinkerTools.bowString;
    }

    @Override
    public int getRequiredFuel(ItemStack stack) {
        return 2;
    }

    @Override
    public int getRequiredLife(ItemStack stack) {
        return 5;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Entity getRenderEntity(ItemStack stack) {
        return null;
    }

    @Override
    public ItemStack getResult(ItemStack stack) {
        if (!matchesInput(stack)) return ItemStack.EMPTY;
        Material mat = ((ToolPart) stack.getItem()).getMaterial(stack);
        if (mat == Material.UNKNOWN) return ItemStack.EMPTY;
        Object item = stack.getItem();
        BetweenlandsPart outputPart;
        if (item == TinkerTools.toolRod) {
            outputPart = ModParts.BETWEEN_HANDLE;
        } else if (item == TinkerTools.binding) {
            outputPart = ModParts.BETWEEN_EXTRA;
        } else if (item == TinkerTools.bowLimb) {
            outputPart = ModParts.BETWEENLANDS_LIMB;
        } else if (item == TinkerTools.bowString) {
            outputPart = ModParts.BETWEENLANDS_BOWSTRING;
        } else {
            outputPart = ModParts.BETWEEN_HEAD;
        }
        return outputPart.getItemstackWithMaterial(mat);
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