package com.npstra.tinkerbetweenlands.content.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

public class TraitIgnition extends AbstractTrait {
    public static final TraitIgnition INSTANCE = new TraitIgnition();
    private static final String KEY_COUNT = "count";
    private static final int STRIKES = 3;

    private TraitIgnition() {
        super("ignition", 0x00AA00);
    }

    private NBTTagCompound getTag(ItemStack tool, boolean createIfMissing) {
        NBTTagList tagList = TagUtil.getModifiersTagList(tool);
        int index = TinkerUtil.getIndexInCompoundList(tagList, getModifierIdentifier());
        if (index >= 0) {
            return tagList.getCompoundTagAt(index);
        }
        if (!createIfMissing) {
            return null;
        }
        NBTTagCompound tag = new NBTTagCompound();
        updateNBTforTrait(tag, color);
        tagList.appendTag(tag);
        TagUtil.setModifiersTagList(tool, tagList);
        return tag;
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (!wasHit) return;
        NBTTagCompound tag = getTag(tool, true);
        tag.setInteger(KEY_COUNT, (tag.getInteger(KEY_COUNT) + 1) % STRIKES);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        NBTTagCompound tag = getTag(tool, false);
        if (tag != null && tag.getInteger(KEY_COUNT) == STRIKES - 1) {
            if (!target.isBurning()) {
                target.setFire(5);
            } else {
                newDamage += damage * 0.5f;
            }
        }
        return newDamage;
    }
}
