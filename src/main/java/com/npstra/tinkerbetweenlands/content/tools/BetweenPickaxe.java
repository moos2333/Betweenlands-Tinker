package com.npstra.tinkerbetweenlands.content.tools;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.AoeToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.capability.circlegem.CircleGemHelper;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;
import static com.npstra.tinkerbetweenlands.content.parts.ModParts.*;
import java.util.List;

public class BetweenPickaxe extends AoeToolCore implements ICorrodible, IBetweenlandsTool {
    public static final ImmutableSet<net.minecraft.block.material.Material> effective_materials =
            ImmutableSet.of(
                    net.minecraft.block.material.Material.IRON,
                    net.minecraft.block.material.Material.ANVIL,
                    net.minecraft.block.material.Material.ROCK,
                    net.minecraft.block.material.Material.ICE,
                    net.minecraft.block.material.Material.GLASS,
                    net.minecraft.block.material.Material.PACKED_ICE,
                    net.minecraft.block.material.Material.PISTON
            );

    public BetweenPickaxe() {
        super(
                PartMaterialType.handle(BETWEEN_HANDLE),
                PartMaterialType.head(BETWEEN_HEAD),
                PartMaterialType.extra(BETWEEN_EXTRA)
        );
        addCategory(Category.HARVEST);
        setHarvestLevel("pickaxe", 0);
        setTranslationKey("tinkerbetweenlands.between_pickaxe");
        CorrosionHelper.addCorrosionPropertyOverrides(this);
        CircleGemHelper.addGemPropertyOverrides(this);
    }

    @Override
    public boolean isEffective(IBlockState state) {
        return effective_materials.contains(state.getMaterial()) || ItemPickaxe.EFFECTIVE_ON.contains(state.getBlock());
    }

    @Override
    public float damagePotential() {
        return 1.0f;
    }

    @Override
    public double attackSpeed() {
        return 1.2f;
    }

    @Override
    protected ToolNBT buildTagData(List<Material> materials) {
        return buildDefaultTag(materials);
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack old, ItemStack newS) {
        return CorrosionHelper.shouldCauseBlockBreakReset(old, newS);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack old, ItemStack newS, boolean slotChanged) {
        return CorrosionHelper.shouldCauseReequipAnimation(old, newS, slotChanged);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return CorrosionHelper.getDestroySpeed(super.getDestroySpeed(stack, state), stack, state);
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity holder, int slot, boolean isHeld) {
        CorrosionHelper.updateCorrosion(stack, world, holder, slot, isHeld);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        CorrosionHelper.addCorrosionTooltips(stack, tooltip, flag.isAdvanced());
    }
}