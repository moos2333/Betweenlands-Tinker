package com.npstra.tinkerbetweenlands.content.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ModifierTagHolder;
import com.npstra.tinkerbetweenlands.config.ModConfig;
import java.util.List;

public class TraitWeedShield extends AbstractTrait {
    public static final TraitWeedShield INSTANCE = new TraitWeedShield();
    private static final int MAX_SHIELD = 100;
    private static final int NORMAL_INTERVAL = 20;
    private static final int BONUS_INTERVAL = 10;

    private TraitWeedShield() {
        super("weedshield", 0x00AA00);
    }

    @Override
    public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isRemote) return;
        if (!(entity instanceof EntityLivingBase)) return;
        if (itemSlot < 0 || (itemSlot > 8 && itemSlot != 40)) return;

        ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
        Data data = modtag.getTagData(Data.class);

        if (data.shield >= MAX_SHIELD) return;

        long currentTick = world.getTotalWorldTime();
        if (data.lastTick == 0) {
            data.lastTick = currentTick;
            modtag.save();
            return;
        }

        int interval = (world.provider.getDimension() == ModConfig.dimensionId) ? BONUS_INTERVAL : NORMAL_INTERVAL;
        long diff = currentTick - data.lastTick;

        if (diff >= interval * 20L) {
            int add = (int) (diff / (interval * 20L));
            data.shield = Math.min(MAX_SHIELD, data.shield + add);
            data.lastTick = currentTick - (diff % (interval * 20L));
            modtag.save();
        }
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
        Data data = modtag.getTagData(Data.class);

        if (data.shield > 0) {
            int consumed = Math.min(data.shield, newDamage);
            data.shield -= consumed;
            newDamage -= consumed;
            modtag.save();
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
        Data data = modtag.getTagData(Data.class);
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, data.shield, MAX_SHIELD));
    }

    public static class Data extends ModifierNBT {
        public int shield;
        public long lastTick;

        @Override
        public void read(NBTTagCompound tag) {
            super.read(tag);
            shield = tag.getInteger("shield");
            lastTick = tag.getLong("lastTick");
        }

        @Override
        public void write(NBTTagCompound tag) {
            super.write(tag);
            tag.setInteger("shield", shield);
            tag.setLong("lastTick", lastTick);
        }
    }
}