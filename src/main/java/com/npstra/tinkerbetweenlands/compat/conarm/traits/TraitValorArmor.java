package com.npstra.tinkerbetweenlands.compat.conarm.traits;

import c4.conarm.common.armor.utils.ArmorTagUtil;
import c4.conarm.lib.armor.ArmorModifications;
import c4.conarm.lib.armor.ArmorNBT;
import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

import java.util.List;

public class TraitValorArmor extends AbstractArmorTrait {
    private static final int MAX_VALOR = 2500;

    public TraitValorArmor() {
        super("valor", TextFormatting.GOLD);
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
    public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
        if (!player.world.isRemote && newDamage > 0) {
            Data data = getData(armor, true);
            int gain = Math.min(10, Math.max(1, (int) newDamage));
            data.valor = Math.min(MAX_VALOR, data.valor + gain);
            saveData(armor, data);
        }
        return newDamage;
    }

    @Override
    public ArmorModifications getModifications(EntityPlayer player, ArmorModifications mods, ItemStack armor, DamageSource source, double damage, int slot) {
        Data data = getData(armor, false);
        if (data != null && data.valor > 0) {
            ArmorNBT original = ArmorTagUtil.getOriginalArmorStats(armor);
            float bonusPercent = data.valor / 10000f; // valor/100 * 0.01
            float defenseBonus = original.defense * bonusPercent;
            float toughnessBonus = original.toughness * bonusPercent;
            mods.addArmor(defenseBonus);
            mods.addToughness(toughnessBonus);
        }
        return mods;
    }

    @Override
    public List<String> getExtraInfo(ItemStack armor, NBTTagCompound modifierTag) {
        int valor = 0;
        if (modifierTag != null) {
            Data data = new Data();
            data.read(modifierTag);
            valor = data.valor;
        }
        int level = valor / 100;
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return java.util.Collections.singletonList(Util.translateFormatted(loc, level));
    }

    public static class Data extends ModifierNBT {
        public int valor;

        @Override
        public void read(NBTTagCompound tag) {
            super.read(tag);
            valor = tag.getInteger("valor");
        }

        @Override
        public void write(NBTTagCompound tag) {
            super.write(tag);
            tag.setInteger("valor", valor);
        }
    }
}