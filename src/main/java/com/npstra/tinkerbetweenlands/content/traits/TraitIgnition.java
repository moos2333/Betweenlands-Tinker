package com.npstra.tinkerbetweenlands.content.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class TraitIgnition extends AbstractTrait {
    public static final TraitIgnition INSTANCE = new TraitIgnition();

    private TraitIgnition() {
        super("ignition", 0x00AA00);
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
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (!wasHit) return;
        Data data = getData(tool, true);
        data.count++;
        saveData(tool, data);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        Data data = getData(tool, false);
        if (data != null && data.count >= 3) {
            if (!target.isBurning()) {
                target.setFire(5);
            } else {
                newDamage *= 1.5f;
            }
            data.count = 0;
            saveData(tool, data);
        }
        return newDamage;
    }

    public static class Data extends ModifierNBT {
        public int count;

        @Override
        public void read(NBTTagCompound tag) {
            super.read(tag);
            count = tag.getInteger("count");
        }

        @Override
        public void write(NBTTagCompound tag) {
            super.write(tag);
            tag.setInteger("count", count);
        }
    }
}