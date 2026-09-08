package com.npstra.tinkerbetweenlands.content.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.TagUtil;
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

        NBTTagCompound root = TagUtil.getTagSafe(tool);
        int shield = root.getInteger(TAG_SHIELD);
        if (shield >= MAX_SHIELD) {
            if (root.hasKey(TAG_ACCUM)) root.removeTag(TAG_ACCUM);
            if (root.hasKey(TAG_PREV_TICK)) root.removeTag(TAG_PREV_TICK);
            tool.setTagCompound(root);
            return;
        }

        long currentTick = ((EntityLivingBase) entity).ticksExisted;
        long prevTick = root.getLong(TAG_PREV_TICK);
        long accum = root.getLong(TAG_ACCUM);

        if (prevTick == 0) {
            root.setLong(TAG_PREV_TICK, currentTick);
            root.setLong(TAG_ACCUM, accum);
            tool.setTagCompound(root);
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

        root.setInteger(TAG_SHIELD, shield);
        root.setLong(TAG_ACCUM, accum);
        root.setLong(TAG_PREV_TICK, currentTick);
        tool.setTagCompound(root);
    }

    private int getInterval(World world) {
        return world.provider.getDimension() == ModConfig.dimensionId ? BONUS_INTERVAL : NORMAL_INTERVAL;
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        NBTTagCompound root = TagUtil.getTagSafe(tool);
        int shield = root.getInteger(TAG_SHIELD);
        if (shield > 0) {
            int consumed = Math.min(shield, newDamage);
            shield -= consumed;
            newDamage -= consumed;
            root.setInteger(TAG_SHIELD, shield);
            tool.setTagCompound(root);
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        NBTTagCompound root = TagUtil.getTagSafe(tool);
        int shield = root.getInteger(TAG_SHIELD);
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, shield, MAX_SHIELD));
    }
}