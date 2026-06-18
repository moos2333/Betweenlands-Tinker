package com.npstra.tinkerbetweenlands.content.tools;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.client.particle.Particles;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.AoeToolCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import slimeknights.tconstruct.tools.TinkerTools;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.capability.circlegem.CircleGemHelper;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;
import static com.npstra.tinkerbetweenlands.content.parts.ModParts.*;
import java.util.List;

public class BetweenHatchet extends AoeToolCore implements ICorrodible, IBetweenlandsTool {
    public static final ImmutableSet<net.minecraft.block.material.Material> effective_materials =
            ImmutableSet.of(
                    net.minecraft.block.material.Material.WOOD,
                    net.minecraft.block.material.Material.VINE,
                    net.minecraft.block.material.Material.PLANTS,
                    net.minecraft.block.material.Material.GOURD,
                    net.minecraft.block.material.Material.CACTUS
            );

    public BetweenHatchet() {
        super(
                PartMaterialType.handle(BETWEEN_HANDLE),
                PartMaterialType.head(BETWEEN_HEAD),
                PartMaterialType.extra(BETWEEN_EXTRA)
        );
        addCategory(Category.HARVEST);
        addCategory(Category.WEAPON);
        setHarvestLevel("axe", 0);
        setTranslationKey("tinkerbetweenlands.between_hatchet");
        CorrosionHelper.addCorrosionPropertyOverrides(this);
        CircleGemHelper.addGemPropertyOverrides(this);
    }

    @Override
    public boolean isEffective(IBlockState state) {
        return effective_materials.contains(state.getMaterial()) || ItemAxe.EFFECTIVE_ON.contains(state.getBlock());
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        float baseSpeed;
        if (state.getBlock().getMaterial(state) == net.minecraft.block.material.Material.LEAVES) {
            baseSpeed = ToolHelper.calcDigSpeed(stack, state);
        } else {
            baseSpeed = super.getDestroySpeed(stack, state);
        }
        return CorrosionHelper.getDestroySpeed(baseSpeed, stack, state);
    }

    @Override
    public void afterBlockBreak(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase player, int damage, boolean wasEffective) {
        if (state.getBlock().isLeaves(state, world, pos)) {
            damage = 0;
        }
        super.afterBlockBreak(stack, world, state, pos, player, damage, wasEffective);
    }

    @Override
    public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
        boolean hit = super.dealDamage(stack, player, entity, damage);
        if (hit && readyForSpecialAttack(player)) {
            TinkerTools.proxy.spawnAttackParticle(Particles.HATCHET_ATTACK, player, 0.8d);
        }
        return hit;
    }

    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, EntityLivingBase entity, EntityLivingBase attacker) {
        return true;
    }

    @Override
    public float damagePotential() {
        return 1.1f;
    }

    @Override
    public double attackSpeed() {
        return 1.1f;
    }

    @Override
    public float knockback() {
        return 1.3f;
    }

    @Override
    protected ToolNBT buildTagData(List<Material> materials) {
        ToolNBT data = buildDefaultTag(materials);
        data.attack += 0.5f;
        return data;
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