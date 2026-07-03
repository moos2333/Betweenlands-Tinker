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

    private Data getData(ItemStack tool) {
        NBTTagCompound root = TagUtil.getTagSafe(tool);
        Data data = new Data();
        data.stack = root.getInteger(KEY_STACK);
        data.accumulated = root.getInteger(KEY_ACCUM);
        return data;
    }

    private void saveData(ItemStack tool, Data data) {
        NBTTagCompound root = TagUtil.getTagSafe(tool);
        root.setInteger(KEY_STACK, data.stack);
        root.setInteger(KEY_ACCUM, data.accumulated);
        tool.setTagCompound(root);
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
        Data data = getData(tool);
        if (data.stack > 0 && entity.world.rand.nextFloat() < data.stack * 0.01f) {
            return 0;
        }
        if (newDamage > 0) {
            data.accumulated += newDamage;
            while (data.accumulated >= ACCUMULATE_THRESHOLD && data.stack < MAX_STACK) {
                data.stack++;
                data.accumulated -= ACCUMULATE_THRESHOLD;
            }
            saveData(tool, data);
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        Data data = getData(tool);
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, data.stack));
    }

    private static class Data {
        int stack;
        int accumulated;
    }
}