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

    private Data getData(ItemStack armor) {
        NBTTagCompound root = TagUtil.getTagSafe(armor);
        Data data = new Data();
        data.stack = root.getInteger(KEY_STACK);
        data.accumulated = root.getInteger(KEY_ACCUM);
        return data;
    }

    private void saveData(ItemStack armor, Data data) {
        NBTTagCompound root = TagUtil.getTagSafe(armor);
        root.setInteger(KEY_STACK, data.stack);
        root.setInteger(KEY_ACCUM, data.accumulated);
        armor.setTagCompound(root);
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
        Data data = getData(armor);
        if (data.stack >= MAX_STACK) {
            return 0;
        }
        if (data.stack > 0 && player.world.rand.nextFloat() < data.stack * 0.01f) {
            return 0;
        }
        if (newDamage > 0) {
            data.accumulated += newDamage;
            while (data.accumulated >= ACCUMULATE_THRESHOLD && data.stack < MAX_STACK) {
                data.stack++;
                data.accumulated -= ACCUMULATE_THRESHOLD;
            }
            if (data.stack >= MAX_STACK) {
                data.accumulated = 0;
            }
            saveData(armor, data);
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack armor, NBTTagCompound modifierTag) {
        Data data = getData(armor);
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return Collections.singletonList(Util.translateFormatted(loc, data.stack));
    }

    private static class Data {
        int stack;
        int accumulated;
    }
}