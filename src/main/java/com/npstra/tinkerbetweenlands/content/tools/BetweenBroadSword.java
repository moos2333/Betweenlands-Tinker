package com.npstra.tinkerbetweenlands.content.tools;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.SwordCore;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.ToolHelper;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.capability.circlegem.CircleGemHelper;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;
import static com.npstra.tinkerbetweenlands.content.parts.ModParts.*;
import java.util.List;

public class BetweenBroadSword extends SwordCore implements ICorrodible, IBetweenlandsTool {
    public BetweenBroadSword() {
        super(
                PartMaterialType.handle(BETWEEN_HANDLE),
                PartMaterialType.head(BETWEEN_HEAD),
                PartMaterialType.extra(BETWEEN_EXTRA)
        );
        setTranslationKey("tinkerbetweenlands.between_broadsword");
        CorrosionHelper.addCorrosionPropertyOverrides(this);
        CircleGemHelper.addGemPropertyOverrides(this);
    }

    @Override
    public float damagePotential() {
        return 1.0f;
    }

    @Override
    public double attackSpeed() {
        return 1.6d;
    }

    @Override
    public boolean dealDamage(ItemStack stack, EntityLivingBase player, Entity entity, float damage) {
        boolean hit = super.dealDamage(stack, player, entity, damage);
        if (hit && !ToolHelper.isBroken(stack)) {
            double d0 = player.distanceWalkedModified - player.prevDistanceWalkedModified;
            boolean flag = true;
            if (player instanceof EntityPlayer) {
                flag = ((EntityPlayer) player).getCooledAttackStrength(0.5F) > 0.9f;
            }
            boolean flag2 = player.fallDistance > 0.0F && !player.onGround && !player.isOnLadder() && !player.isInWater() && !player.isPotionActive(MobEffects.BLINDNESS) && !player.isRiding();
            if (flag && !player.isSprinting() && !flag2 && player.onGround && d0 < player.getAIMoveSpeed()) {
                for (EntityLivingBase entitylivingbase : player.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().expand(1.0D, 0.25D, 1.0D))) {
                    if (entitylivingbase != player && entitylivingbase != entity && !player.isOnSameTeam(entitylivingbase) && player.getDistanceSq(entitylivingbase) < 9.0D) {
                        entitylivingbase.knockBack(player, 0.4F, MathHelper.sin(player.rotationYaw * 0.017453292F), -MathHelper.cos(player.rotationYaw * 0.017453292F));
                        super.dealDamage(stack, player, entitylivingbase, 1f);
                    }
                }
                player.getEntityWorld().playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, player.getSoundCategory(), 1.0F, 1.0F);
                if (player instanceof EntityPlayer) {
                    ((EntityPlayer) player).spawnSweepParticles();
                }
            }
        }
        return hit;
    }

    @Override
    public float getRepairModifierForPart(int index) {
        return 1.1f;
    }

    @Override
    public ToolNBT buildTagData(List<Material> materials) {
        ToolNBT data = buildDefaultTag(materials);
        data.attack += 1f;
        data.durability *= 1.1f;
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