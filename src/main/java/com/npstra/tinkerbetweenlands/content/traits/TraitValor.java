package com.npstra.tinkerbetweenlands.content.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;

import java.util.List;

public class TraitValor extends AbstractTrait {
    public static final TraitValor INSTANCE = new TraitValor();
    private static final float BONUS_DAMAGE = 0.1f;
    private static final int EFFECT_DURATION = 60;
    private static final int EFFECT_AMPLIFIER = 0;

    private TraitValor() {
        super("valor", 0x00AA00);
    }

    private static boolean isHatedBy(EntityLivingBase target, EntityLivingBase player) {
        return player != null
                && target instanceof EntityCreature
                && ((EntityCreature) target).getAttackTarget() == player;
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        if (isHatedBy(target, player)) {
            newDamage += damage * BONUS_DAMAGE;
        }
        return newDamage;
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (wasHit && !player.getEntityWorld().isRemote && isHatedBy(target, player)) {
            target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, EFFECT_DURATION, EFFECT_AMPLIFIER));
            target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, EFFECT_DURATION, EFFECT_AMPLIFIER));
        }
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, (int) (BONUS_DAMAGE * 100)));
    }
}