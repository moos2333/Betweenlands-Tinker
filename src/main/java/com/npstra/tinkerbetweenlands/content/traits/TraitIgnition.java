package com.npstra.tinkerbetweenlands.content.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ModifierTagHolder;

public class TraitIgnition extends AbstractTrait {
    public static final TraitIgnition INSTANCE = new TraitIgnition();

    private TraitIgnition() {
        super("ignition", 0x00AA00);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (!wasHit) return;
        ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
        Data data = modtag.getTagData(Data.class);
        data.count++;
        modtag.save();
        super.afterHit(tool, player, target, damageDealt, wasCritical, wasHit);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
        Data data = modtag.getTagData(Data.class);

        if (data.count >= 3) {
            if (!target.isBurning()) {
                target.setFire(5);
            } else {
                newDamage *= 1.25f;
            }
            data.count = 0;
            modtag.save();
        }
        return super.damage(tool, player, target, damage, newDamage, isCritical);
    }

    private static class Data extends ModifierNBT {
        int count;

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