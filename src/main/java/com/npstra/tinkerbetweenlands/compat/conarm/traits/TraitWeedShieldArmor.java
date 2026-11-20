package com.npstra.tinkerbetweenlands.compat.conarm.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import thebetweenlands.common.world.WorldProviderBetweenlands;

public class TraitWeedShieldArmor extends AbstractArmorTrait {

    private static final int MAX_SHIELD = 100;
    private static final int NORMAL_INTERVAL = 20;
    private static final int BONUS_INTERVAL = 10;
    private static final String TAG_SHIELD = "weed_shield";
    private static final String TAG_ACCUM = "weed_accum";
    private static final String TAG_PREV_TICK = "weed_prev_tick";

    public TraitWeedShieldArmor() {
        super("weedshield", TextFormatting.GREEN);
    }

    @Override
    public void onArmorTick(ItemStack armor, World world, EntityPlayer player) {
        if (world.isRemote) return;

        NBTTagCompound tag = TinkerUtil.getModifierTag(armor, getModifierIdentifier());
        int shield = tag.getInteger(TAG_SHIELD);
        long prevTick = tag.getLong(TAG_PREV_TICK);

        if (shield >= MAX_SHIELD) {
            if (prevTick != 0) {
                tag.removeTag(TAG_ACCUM);
                tag.removeTag(TAG_PREV_TICK);
            }
            return;
        }

        long currentTick = player.ticksExisted;
        if (prevTick == 0) {
            tag.setLong(TAG_PREV_TICK, currentTick);
            return;
        }

        long delta = currentTick - prevTick;
        if (delta < 0) {
            tag.setLong(TAG_PREV_TICK, currentTick);
            return;
        }

        long intervalTicks = getInterval(world) * 20L;
        long maxDelta = intervalTicks * 2;
        if (delta > maxDelta) delta = maxDelta;

        long accum = tag.getLong(TAG_ACCUM) + delta;
        if (accum < intervalTicks) return;

        int gained = (int) Math.min(accum / intervalTicks, MAX_SHIELD - shield);
        shield += gained;
        accum -= gained * intervalTicks;

        tag.setInteger(TAG_SHIELD, shield);
        if (shield >= MAX_SHIELD) {
            tag.removeTag(TAG_ACCUM);
            tag.removeTag(TAG_PREV_TICK);
        } else {
            tag.setLong(TAG_ACCUM, accum);
            tag.setLong(TAG_PREV_TICK, currentTick);
        }
    }

    private int getInterval(World world) {
        return world.provider instanceof WorldProviderBetweenlands ? BONUS_INTERVAL : NORMAL_INTERVAL;
    }

    @Override
    public int onArmorDamage(ItemStack armor, DamageSource source, int damage, int newDamage, EntityPlayer player, int slot) {
        NBTTagCompound tag = TinkerUtil.getModifierTag(armor, getModifierIdentifier());
        int shield = tag.getInteger(TAG_SHIELD);
        if (shield > 0) {
            int consumed = Math.min(shield, newDamage);
            tag.setInteger(TAG_SHIELD, shield - consumed);
            return newDamage - consumed;
        }
        return newDamage;
    }
}
