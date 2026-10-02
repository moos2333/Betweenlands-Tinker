package com.npstra.tinkerbetweenlands.content.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import com.npstra.tinkerbetweenlands.config.ModConfig;

import java.util.List;

public class TraitWeedShield extends AbstractTrait {
    public static final TraitWeedShield INSTANCE = new TraitWeedShield();
    private static final int MAX_SHIELD = 100;
    private static final int NORMAL_INTERVAL = 20;
    private static final int BONUS_INTERVAL = 10;
    private static final String TAG_SHIELD = "weed_shield";
    private static final String TAG_ACCUM = "weed_accum";
    private static final String TAG_PREV_TICK = "weed_prev_tick";

    private TraitWeedShield() {
        super("weedshield", 0x00AA00);
    }

    @Override
    public void onUpdate(ItemStack tool, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (world.isRemote) return;
        if (!(entity instanceof EntityLivingBase)) return;
        if (itemSlot < 0 || (itemSlot > 8 && itemSlot != 40)) return;

        NBTTagCompound tag = TinkerUtil.getModifierTag(tool, getModifierIdentifier());
        int shield = tag.getInteger(TAG_SHIELD);
        if (shield >= MAX_SHIELD) {
            if (tag.hasKey(TAG_ACCUM)) tag.removeTag(TAG_ACCUM);
            if (tag.hasKey(TAG_PREV_TICK)) tag.removeTag(TAG_PREV_TICK);
            return;
        }

        long currentTick = ((EntityLivingBase) entity).ticksExisted;
        long prevTick = tag.getLong(TAG_PREV_TICK);
        long accum = tag.getLong(TAG_ACCUM);

        if (prevTick == 0) {
            tag.setLong(TAG_PREV_TICK, currentTick);
            tag.setLong(TAG_ACCUM, accum);
            return;
        }

        long delta = currentTick - prevTick;
        if (delta < 0) {
            delta = 0;
            prevTick = currentTick;
        }
        long maxDelta = (getInterval(world) * 20L) * 2;
        if (delta > maxDelta) delta = maxDelta;

        accum += delta;
        long intervalTicks = getInterval(world) * 20L;
        while (accum >= intervalTicks) {
            shield++;
            accum -= intervalTicks;
            if (shield >= MAX_SHIELD) break;
        }
        if (shield > MAX_SHIELD) shield = MAX_SHIELD;

        tag.setInteger(TAG_SHIELD, shield);
        tag.setLong(TAG_ACCUM, accum);
        tag.setLong(TAG_PREV_TICK, currentTick);
    }

    private int getInterval(World world) {
        return world.provider.getDimension() == ModConfig.dimensionId ? BONUS_INTERVAL : NORMAL_INTERVAL;
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        NBTTagCompound tag = TinkerUtil.getModifierTag(tool, getModifierIdentifier());
        int shield = tag.getInteger(TAG_SHIELD);
        if (shield > 0) {
            int consumed = Math.min(shield, newDamage);
            tag.setInteger(TAG_SHIELD, shield - consumed);
            return newDamage - consumed;
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        int shield = modifierTag.getInteger(TAG_SHIELD);
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, shield, MAX_SHIELD));
    }
}