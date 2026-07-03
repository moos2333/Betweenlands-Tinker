package com.npstra.tinkerbetweenlands.content.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;

import java.util.List;

public class TraitValor extends AbstractTrait {
    public static final TraitValor INSTANCE = new TraitValor();
    private static final int MAX_VALOR = 3000;
    private static final int BONUS_INTERVAL = 100;
    private static final String KEY_VALOR = "valor_value";

    private TraitValor() {
        super("valor", 0x00AA00);
    }

    private int getValor(ItemStack tool) {
        return TagUtil.getTagSafe(tool).getInteger(KEY_VALOR);
    }

    private void setValor(ItemStack tool, int valor) {
        NBTTagCompound root = TagUtil.getTagSafe(tool);
        root.setInteger(KEY_VALOR, valor);
        tool.setTagCompound(root);
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (wasHit && !player.getEntityWorld().isRemote) {
            int gain = Math.max(1, Math.min(10, (int) (damageDealt / 5.0f)));
            int valor = Math.min(MAX_VALOR, getValor(tool) + gain);
            setValor(tool, valor);
        }
    }

    @Override
    public void afterBlockBreak(ItemStack tool, net.minecraft.world.World world, net.minecraft.block.state.IBlockState state, net.minecraft.util.math.BlockPos pos, EntityLivingBase player, boolean wasEffective) {
        if (!world.isRemote) {
            int valor = Math.min(MAX_VALOR, getValor(tool) + 1);
            setValor(tool, valor);
        }
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        int level = getValor(tool) / BONUS_INTERVAL;
        if (level > 0) {
            newDamage *= (1 + level * 0.01f);
        }
        return newDamage;
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        int level = getValor(tool) / BONUS_INTERVAL;
        if (level > 0) {
            event.setNewSpeed(event.getNewSpeed() * (1 + level * 0.01f));
        }
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        int level = getValor(tool) / BONUS_INTERVAL;
        if (level > 0 && entity.world.rand.nextFloat() < level * 0.01f) {
            return 0;
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        int level = getValor(tool) / BONUS_INTERVAL;
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, level));
    }
}