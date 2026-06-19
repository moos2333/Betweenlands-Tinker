package com.npstra.tinkerbetweenlands.content.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

import java.util.List;

public class TraitStacking extends AbstractTrait {
    public static final TraitStacking INSTANCE = new TraitStacking();
    private static final int MAX_STACK = 100;
    private static final int ACCUMULATE_THRESHOLD = 1000;

    private TraitStacking() {
        super("stacking", 0x00AA00);
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

    private Data getData(ItemStack tool, boolean createIfMissing) {
        NBTTagCompound root = TagUtil.getTagSafe(tool);
        NBTTagList tagList = TagUtil.getModifiersTagList(root);
        String id = getModifierIdentifier();
        int index = TinkerUtil.getIndexInCompoundList(tagList, id);
        NBTTagCompound tag;
        if (index == -1) {
            if (!createIfMissing) return null;
            tag = new NBTTagCompound();
            tag.setString("identifier", id);
            tagList.appendTag(tag);
            TagUtil.setModifiersTagList(tool, tagList);
        } else {
            tag = tagList.getCompoundTagAt(index);
        }
        Data data = new Data();
        data.read(tag);
        return data;
    }

    private void saveData(ItemStack tool, Data data) {
        NBTTagCompound root = TagUtil.getTagSafe(tool);
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
        TagUtil.setModifiersTagList(tool, tagList);
    }

    @Override
    public int onToolHeal(ItemStack tool, int amount, int newAmount, EntityLivingBase entity) {
        return newAmount / 2;
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        if (entity == null) {
            return newDamage;
        }
        Data data = getData(tool, true);
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
        int stackCount = 0;
        if (modifierTag != null && modifierTag.hasKey("stack")) {
            stackCount = modifierTag.getInteger("stack");
        }
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, stackCount));
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