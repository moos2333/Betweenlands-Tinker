package com.npstra.tinkerbetweenlands.content.tools;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.events.TinkerToolEvent;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.AoeToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.capability.circlegem.CircleGemHelper;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;
import static com.npstra.tinkerbetweenlands.content.parts.ModParts.*;
import java.util.List;

public class BetweenShovel extends AoeToolCore implements ICorrodible, IBetweenlandsTool {
    public static final ImmutableSet<net.minecraft.block.material.Material> effective_materials =
            ImmutableSet.of(
                    net.minecraft.block.material.Material.GRASS,
                    net.minecraft.block.material.Material.GROUND,
                    net.minecraft.block.material.Material.SAND,
                    net.minecraft.block.material.Material.CRAFTED_SNOW,
                    net.minecraft.block.material.Material.SNOW,
                    net.minecraft.block.material.Material.CLAY,
                    net.minecraft.block.material.Material.CAKE
            );

    public BetweenShovel() {
        super(
                PartMaterialType.handle(BETWEEN_HANDLE),
                PartMaterialType.head(BETWEEN_HEAD),
                PartMaterialType.extra(BETWEEN_EXTRA)
        );
        addCategory(Category.HARVEST);
        setHarvestLevel("shovel", 0);
        setTranslationKey("tinkerbetweenlands.between_shovel");
        CorrosionHelper.addCorrosionPropertyOverrides(this);
        CircleGemHelper.addGemPropertyOverrides(this);
    }

    @Override
    public boolean isEffective(IBlockState state) {
        return effective_materials.contains(state.getMaterial()) || ItemSpade.EFFECTIVE_ON.contains(state.getBlock());
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (ToolHelper.isBroken(stack)) {
            return EnumActionResult.FAIL;
        }

        EnumActionResult result = Items.DIAMOND_SHOVEL.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        if (result == EnumActionResult.SUCCESS) {
            TinkerToolEvent.OnShovelMakePath.fireEvent(stack, player, world, pos);
        }

        Block block = world.getBlockState(pos).getBlock();
        if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
            for (BlockPos aoePos : getAOEBlocks(stack, world, player, pos)) {
                if (ToolHelper.isBroken(stack)) {
                    break;
                }
                EnumActionResult aoeResult = Items.DIAMOND_SHOVEL.onItemUse(player, world, aoePos, hand, facing, hitX, hitY, hitZ);
                if (result != EnumActionResult.SUCCESS) {
                    result = aoeResult;
                }
                if (aoeResult == EnumActionResult.SUCCESS) {
                    TinkerToolEvent.OnShovelMakePath.fireEvent(stack, player, world, aoePos);
                }
            }
        }
        return result;
    }

    @Override
    public double attackSpeed() {
        return 1.0f;
    }

    @Override
    public float damagePotential() {
        return 0.9f;
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