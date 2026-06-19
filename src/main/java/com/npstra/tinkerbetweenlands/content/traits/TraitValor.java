package com.npstra.tinkerbetweenlands.content.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;

import java.util.List;

public class TraitValor extends AbstractTrait {
    public static final TraitValor INSTANCE = new TraitValor();
    private static final int MAX_VALOR = 3000;
    private static final int BONUS_INTERVAL = 100;

    private TraitValor() {
        super("valor", 0x00AA00);
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
        if (wasHit && !player.getEntityWorld().isRemote) {
            Data data = getData(tool, true);
            int gain = Math.max(1, Math.min(10, (int) (damageDealt / 5.0f)));
            data.valor = Math.min(MAX_VALOR, data.valor + gain);
            saveData(tool, data);
        }
    }

    @Override
    public void afterBlockBreak(ItemStack tool, net.minecraft.world.World world, net.minecraft.block.state.IBlockState state, net.minecraft.util.math.BlockPos pos, EntityLivingBase player, boolean wasEffective) {
        if (!world.isRemote) {
            Data data = getData(tool, true);
            data.valor = Math.min(MAX_VALOR, data.valor + 1);
            saveData(tool, data);
        }
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        Data data = getData(tool, false);
        int level = data == null ? 0 : data.valor / BONUS_INTERVAL;
        if (level > 0) {
            newDamage *= (1 + level * 0.01f);
        }
        return newDamage;
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        Data data = getData(tool, false);
        int level = data == null ? 0 : data.valor / BONUS_INTERVAL;
        if (level > 0) {
            event.setNewSpeed(event.getNewSpeed() * (1 + level * 0.01f));
        }
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        Data data = getData(tool, false);
        int level = data == null ? 0 : data.valor / BONUS_INTERVAL;
        if (level > 0 && entity.world.rand.nextFloat() < level * 0.01f) {
            return 0;
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        int level = 0;
        if (modifierTag != null) {
            Data data = new Data();
            data.read(modifierTag);
            level = data.valor / BONUS_INTERVAL;
        }
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