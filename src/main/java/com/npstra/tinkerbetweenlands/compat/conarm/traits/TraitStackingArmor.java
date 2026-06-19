package com.npstra.tinkerbetweenlands.compat.conarm.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

import java.util.List;

public class TraitStackingArmor extends AbstractArmorTrait {
    private static final int MAX_STACK = 100;
    private static final int ACCUMULATE_THRESHOLD = 300;

    public TraitStackingArmor() {
        super("stacking", TextFormatting.GOLD);
    }

    @Override
    public void updateNBT(NBTTagCompound modifierTag) {
        Data data = new Data();
        data.read(modifierTag);
        data.identifier = this.getModifierIdentifier();
        data.color = this.color;
        if (data.level == 0) data.level = 1;
        data.write(modifierTag);
    }

    private Data getData(ItemStack armor, boolean createIfMissing) {
        NBTTagCompound root = TagUtil.getTagSafe(armor);
        NBTTagList tagList = TagUtil.getModifiersTagList(root);
        String id = getModifierIdentifier();
        int index = TinkerUtil.getIndexInCompoundList(tagList, id);
        NBTTagCompound tag;
        if (index == -1) {
            if (!createIfMissing) return null;
            tag = new NBTTagCompound();
            tag.setString("identifier", id);
            tagList.appendTag(tag);
            TagUtil.setModifiersTagList(armor, tagList);
        } else {
            tag = tagList.getCompoundTagAt(index);
        }
        Data data = new Data();
        data.read(tag);
        return data;
    }

    private void saveData(ItemStack armor, Data data) {
        NBTTagCompound root = TagUtil.getTagSafe(armor);
        NBTTagList tagList = TagUtil.getModifiersTagList(root);
        String id = getModifierIdentifier();
        int index = TinkerUtil.getIndexInCompoundList(tagList, id);
        NBTTagCompound tag;
        if (index == -1) {
            tag = new NBTTagCompound();
            tag.setString("identifier", id);
            tagList.appendTag(tag);
        } else {
            tag = tagList.getCompoundTagAt(index);
        }
        data.write(tag);
        TagUtil.setModifiersTagList(armor, tagList);
    }

    @Override
    public int onArmorHeal(ItemStack armor, DamageSource source, int amount, int newAmount, EntityPlayer player, int slot) {
        return newAmount / 4;
    }

    @Override
    public int onArmorDamage(ItemStack armor, DamageSource source, int damage, int newDamage, EntityPlayer player, int slot) {
        Data data = getData(armor, true);
        if (data.stack > 0 && player.world.rand.nextFloat() < data.stack * 0.01f) {
            return 0;
        }
        if (newDamage > 0) {
            data.accumulated += newDamage;
            while (data.accumulated >= ACCUMULATE_THRESHOLD && data.stack < MAX_STACK) {
                data.stack++;
                data.accumulated -= ACCUMULATE_THRESHOLD;
            }
            saveData(armor, data);
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        int stackCount = 0;
        if (modifierTag != null && modifierTag.hasKey("stack")) {
            stackCount = modifierTag.getInteger("stack");
        }
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return java.util.Collections.singletonList(Util.translateFormatted(loc, stackCount));
    }

    public static class Data extends ModifierNBT {
        public int stack;
        public int accumulated;

        @Override
        public void read(NBTTagCompound tag) {
            super.read(tag);
            stack = tag.getInteger("stack");
            accumulated = tag.getInteger("accumulated");
        }

        @Override
        public void write(NBTTagCompound tag) {
            super.write(tag);
            tag.setInteger("stack", stack);
            tag.setInteger("accumulated", accumulated);
        }
    }
}