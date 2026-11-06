package com.npstra.tinkerbetweenlands.content.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;

import java.util.List;

public class TraitStacking extends AbstractTrait {
    public static final TraitStacking INSTANCE = new TraitStacking();
    private static final int MAX_STACK = 100;
    private static final int ACCUMULATE_THRESHOLD = 1000;
    private static final String KEY_STACK = "stacking_stack";
    private static final String KEY_ACCUM = "stacking_accum";

    private TraitStacking() {
        super("stacking", 0x00AA00);
    }

    @Override
    public int onToolHeal(ItemStack tool, int amount, int newAmount, EntityLivingBase entity) {
        return newAmount / 5;
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        if (entity == null) {
            return newDamage;
        }

        NBTTagCompound root = TagUtil.getTagSafe(tool);
        int stack = root.getInteger(KEY_STACK);

        if (stack >= MAX_STACK) {
            return 0;
        }

        if (stack > 0 && entity.world.rand.nextFloat() < stack * 0.01f) {
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
            tool.setTagCompound(root);
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        int stack = modifierTag.getInteger(KEY_STACK);
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, stack));
    }
}