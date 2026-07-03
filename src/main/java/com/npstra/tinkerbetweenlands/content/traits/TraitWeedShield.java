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
    private static final String TAG_LAST_TICK = "weed_last_tick";

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
        long lastTick = root.getLong(TAG_LAST_TICK);
        long currentTick = world.getTotalWorldTime();

        if (lastTick > currentTick) {
            lastTick = currentTick;
            root.setLong(TAG_LAST_TICK, lastTick);
            tool.setTagCompound(root);
        }

        if (shield >= MAX_SHIELD) {
            if (lastTick != currentTick) {
                root.setLong(TAG_LAST_TICK, currentTick);
                tool.setTagCompound(root);
            }
            return;
        }

        if (lastTick == 0) {
            root.setLong(TAG_LAST_TICK, currentTick);
            tool.setTagCompound(root);
            return;
        }

        int interval = (world.provider.getDimension() == ModConfig.dimensionId) ? BONUS_INTERVAL : NORMAL_INTERVAL;
        long intervalTicks = interval * 20L;
        long diff = currentTick - lastTick;

        if (diff >= intervalTicks) {
            int add = (int) (diff / intervalTicks);
            if (add > 0) {
                shield = Math.min(MAX_SHIELD, shield + add);
                long newLastTick = currentTick - (diff % intervalTicks);
                root.setInteger(TAG_SHIELD, shield);
                root.setLong(TAG_LAST_TICK, newLastTick);
                tool.setTagCompound(root);
            }
        }
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