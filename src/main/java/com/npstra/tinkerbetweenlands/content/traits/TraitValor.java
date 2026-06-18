package com.npstra.tinkerbetweenlands.content.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ModifierTagHolder;
import java.util.List;

public class TraitValor extends AbstractTrait {
    public static final TraitValor INSTANCE = new TraitValor();
    private static final int MAX_VALOR = 3000;
    private static final int BONUS_INTERVAL = 100;

    private TraitValor() {
        super("valor", 0x00AA00);
    }

    private static void addValor(ItemStack tool, int amount) {
        if (tool.isEmpty() || amount <= 0) return;
        ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, INSTANCE.getModifierIdentifier());
        Data data = modtag.getTagData(Data.class);
        data.valor = Math.min(MAX_VALOR, data.valor + amount);
        modtag.save();
    }

    private int getBonusLevel(ItemStack tool) {
        ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
        Data data = modtag.getTagData(Data.class);
        return data.valor / BONUS_INTERVAL;
    }

    @Override
    public void afterHit(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damageDealt, boolean wasCritical, boolean wasHit) {
        if (wasHit && !player.getEntityWorld().isRemote) {
            int gain = Math.max(1, Math.min(10, (int)(damageDealt / 5.0f)));
            addValor(tool, gain);
        }
        super.afterHit(tool, player, target, damageDealt, wasCritical, wasHit);
    }

    @Override
    public void afterBlockBreak(ItemStack tool, net.minecraft.world.World world, net.minecraft.block.state.IBlockState state, net.minecraft.util.math.BlockPos pos, EntityLivingBase player, boolean wasEffective) {
        if (!world.isRemote) {
            addValor(tool, 1);
        }
        super.afterBlockBreak(tool, world, state, pos, player, wasEffective);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        int level = getBonusLevel(tool);
        if (level > 0) {
            newDamage *= (1 + level * 0.01f);
        }
        return super.damage(tool, player, target, damage, newDamage, isCritical);
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        int level = getBonusLevel(tool);
        if (level > 0) {
            event.setNewSpeed(event.getNewSpeed() * (1 + level * 0.01f));
        }
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        int level = getBonusLevel(tool);
        if (level > 0 && entity.world.rand.nextFloat() < level * 0.01f) {
            return 0;
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        int level = getBonusLevel(tool);
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, level));
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