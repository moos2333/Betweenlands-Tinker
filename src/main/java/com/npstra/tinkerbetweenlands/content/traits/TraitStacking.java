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
        if (newDamage <= 0) {
            return newDamage;
        }
        int accumulated = root.getInteger(KEY_ACCUM) + newDamage;
        while (accumulated >= ACCUMULATE_THRESHOLD && stack < MAX_STACK) {
            stack++;
            accumulated -= ACCUMULATE_THRESHOLD;
        }
        if (stack >= MAX_STACK) {
            accumulated = 0;
        }
        root.setInteger(KEY_STACK, stack);
        root.setInteger(KEY_ACCUM, accumulated);
        tool.setTagCompound(root);
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, TagUtil.getTagSafe(tool).getInteger(KEY_STACK)));
    }
}