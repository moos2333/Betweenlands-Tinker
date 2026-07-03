package com.npstra.tinkerbetweenlands.compat.conarm.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.utils.TagUtil;

import java.util.List;

public class TraitStackingArmor extends AbstractArmorTrait {
    private static final int MAX_STACK = 100;
    private static final int ACCUMULATE_THRESHOLD = 300;
    private static final String KEY_STACK = "stacking_armor_stack";
    private static final String KEY_ACCUM = "stacking_armor_accum";

    public TraitStackingArmor() {
        super("stacking", TextFormatting.GOLD);
    }

    private int getStack(ItemStack armor) {
        return TagUtil.getTagSafe(armor).getInteger(KEY_STACK);
    }

    private int getAccum(ItemStack armor) {
        return TagUtil.getTagSafe(armor).getInteger(KEY_ACCUM);
    }

    private void setData(ItemStack armor, int stack, int accum) {
        NBTTagCompound root = TagUtil.getTagSafe(armor);
        root.setInteger(KEY_STACK, stack);
        root.setInteger(KEY_ACCUM, accum);
        armor.setTagCompound(root);
    }

    @Override
    public int onArmorHeal(ItemStack armor, DamageSource source, int amount, int newAmount, EntityPlayer player, int slot) {
        return newAmount / 4;
    }

    @Override
    public int onArmorDamage(ItemStack armor, DamageSource source, int damage, int newDamage, EntityPlayer player, int slot) {
        int stack = getStack(armor);
        int accum = getAccum(armor);
        if (stack > 0 && player.world.rand.nextFloat() < stack * 0.01f) {
            return 0;
        }
        if (newDamage > 0) {
            accum += newDamage;
            while (accum >= ACCUMULATE_THRESHOLD && stack < MAX_STACK) {
                stack++;
                accum -= ACCUMULATE_THRESHOLD;
            }
            setData(armor, stack, accum);
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        int stackCount = getStack(tool);
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return java.util.Collections.singletonList(Util.translateFormatted(loc, stackCount));
    }
}