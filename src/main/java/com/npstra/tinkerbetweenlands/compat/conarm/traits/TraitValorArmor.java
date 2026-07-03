package com.npstra.tinkerbetweenlands.compat.conarm.traits;

import c4.conarm.common.armor.utils.ArmorTagUtil;
import c4.conarm.lib.armor.ArmorModifications;
import c4.conarm.lib.armor.ArmorNBT;
import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.utils.TagUtil;

import java.util.List;

public class TraitValorArmor extends AbstractArmorTrait {
    private static final int MAX_VALOR = 2500;
    private static final String KEY_VALOR = "valor_armor_value";

    public TraitValorArmor() {
        super("valor", TextFormatting.GOLD);
    }

    private int getValor(ItemStack armor) {
        return TagUtil.getTagSafe(armor).getInteger(KEY_VALOR);
    }

    private void setValor(ItemStack armor, int valor) {
        NBTTagCompound root = TagUtil.getTagSafe(armor);
        root.setInteger(KEY_VALOR, valor);
        armor.setTagCompound(root);
    }

    @Override
    public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
        if (!player.world.isRemote && newDamage > 0) {
            int gain = Math.min(10, Math.max(1, (int) newDamage));
            int valor = Math.min(MAX_VALOR, getValor(armor) + gain);
            setValor(armor, valor);
        }
        return newDamage;
    }

    @Override
    public ArmorModifications getModifications(EntityPlayer player, ArmorModifications mods, ItemStack armor, DamageSource source, double damage, int slot) {
        int valor = getValor(armor);
        if (valor > 0) {
            ArmorNBT original = ArmorTagUtil.getOriginalArmorStats(armor);
            float bonusPercent = valor / 10000f;
            float defenseBonus = original.defense * bonusPercent;
            float toughnessBonus = original.toughness * bonusPercent;
            mods.addArmor(defenseBonus);
            mods.addToughness(toughnessBonus);
        }
        return mods;
    }

    @Override
    public List<String> getExtraInfo(ItemStack armor, NBTTagCompound modifierTag) {
        int level = getValor(armor) / 100;
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return java.util.Collections.singletonList(Util.translateFormatted(loc, level));
    }
}