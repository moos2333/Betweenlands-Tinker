package com.npstra.tinkerbetweenlands.compat.conarm.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.Util;

import java.util.List;

public class TraitValorArmor extends AbstractArmorTrait {

    private static final float DAMAGE_REDUCTION_PER_PIECE = 0.08f;

    public TraitValorArmor() {
        super("valor", TextFormatting.GOLD);
    }

    @Override
    public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
        if (!player.world.isRemote && isHatedTarget(source, player) && newDamage > 0) {
            return newDamage * (1.0f - DAMAGE_REDUCTION_PER_PIECE);
        }
        return newDamage;
    }

    @Override
    public int onArmorDamage(ItemStack armor, DamageSource source, int damage, int newDamage, EntityPlayer player, int slot) {
        if (isHatedTarget(source, player) && newDamage > 1) {
            return newDamage / 2;
        }
        return newDamage;
    }

    private boolean isHatedTarget(DamageSource source, EntityPlayer player) {
        if (source == null) {
            return false;
        }
        Entity trueSource = source.getTrueSource();
        if (trueSource instanceof EntityLiving) {
            EntityLiving attacker = (EntityLiving) trueSource;
            return attacker.getAttackTarget() == player;
        }
        return false;
    }

    @Override
    public List<String> getExtraInfo(ItemStack armor, NBTTagCompound modifierTag) {
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return java.util.Collections.singletonList(Util.translateFormatted(loc, 8));
    }
}