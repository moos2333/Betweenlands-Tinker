package com.npstra.tinkerbetweenlands.compat.conarm.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.utils.TagUtil;

import java.util.Collections;
import java.util.List;

public class TraitStackingArmor extends AbstractArmorTrait {
    private static final int MAX_STACK = 100;
    private static final int ACCUMULATE_THRESHOLD = 300;
    private static final String KEY_STACK = "stacking_armor_stack";
    private static final String KEY_ACCUM = "stacking_armor_accum";

    public TraitStackingArmor() {
        super("stacking", TextFormatting.GOLD);
    }

    @Override
    public int onArmorHeal(ItemStack armor, DamageSource source, int amount, int newAmount, EntityPlayer player, int slot) {
        return newAmount / 4;
    }

    @Override
    public int onArmorDamage(ItemStack armor, DamageSource source, int damage, int newDamage, EntityPlayer player, int slot) {
        if (player == null) {
            return newDamage;
        }

        NBTTagCompound root = TagUtil.getTagSafe(armor);
        int stack = root.getInteger(KEY_STACK);

        if (stack >= MAX_STACK) {
            return 0;
        }

        if (stack > 0 && player.world.rand.nextFloat() < stack * 0.01f) {
            return 0;
        }

        if (newDamage > 0) {
            int accum = root.getInteger(KEY_ACCUM);
            int totalAccum = accum + newDamage;
            int addedStacks = totalAccum / ACCUMULATE_THRESHOLD;

            if (addedStacks > 0) {
                stack += addedStacks;
                if (stack >= MAX_STACK) {
                    stack = MAX_STACK;
                    accum = 0;
                } else {
                    accum = totalAccum % ACCUMULATE_THRESHOLD;
                }
            } else {
                accum = totalAccum;
            }

            root.setInteger(KEY_STACK, stack);
            root.setInteger(KEY_ACCUM, accum);
            armor.setTagCompound(root);
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack armor, NBTTagCompound modifierTag) {
        int stackCount = modifierTag.getInteger(KEY_STACK);
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return Collections.singletonList(Util.translateFormatted(loc, stackCount));
    }
}