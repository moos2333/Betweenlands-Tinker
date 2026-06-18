package com.npstra.tinkerbetweenlands.compat.conarm.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import slimeknights.tconstruct.library.utils.TagUtil;

public class TraitStackingArmor extends AbstractArmorTrait {

    private static final int MAX_STACK = 100;
    private static final int REPAIR_THRESHOLD = 250;

    public TraitStackingArmor() {
        super("stacking", TextFormatting.GOLD);
    }

    @Override
    public int onArmorHeal(ItemStack armor, DamageSource source, int amount, int newAmount, EntityPlayer player, int slot) {
        if (newAmount <= 1) {
            return 0;
        }
        int halved = newAmount / 2;
        if (amount > REPAIR_THRESHOLD) {
            NBTTagCompound root = TagUtil.getTagSafe(armor);
            int stack = root.getInteger("stack");
            if (stack < MAX_STACK) {
                stack++;
                root.setInteger("stack", stack);
                armor.setTagCompound(root);
            }
        }
        return halved;
    }

    @Override
    public int onArmorDamage(ItemStack armor, DamageSource source, int damage, int newDamage, EntityPlayer player, int slot) {
        NBTTagCompound root = TagUtil.getTagSafe(armor);
        int stack = root.getInteger("stack");
        if (stack > 0 && player.world.rand.nextFloat() < stack * 0.01f) {
            return 0;
        }
        return newDamage;
    }
}