package com.npstra.tinkerbetweenlands.compat.conarm.traits;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.utils.TagUtil;
import com.npstra.tinkerbetweenlands.config.ModConfig;

public class TraitWeedShieldArmor extends AbstractArmorTrait {

    private static final int MAX_SHIELD = 100;
    private static final int NORMAL_INTERVAL = 20;
    private static final int BONUS_INTERVAL = 10;

    public TraitWeedShieldArmor() {
        super("weedshield", TextFormatting.GREEN);
    }

    @Override
    public void onArmorTick(ItemStack armor, World world, EntityPlayer player) {
        if (world.isRemote) return;

        NBTTagCompound root = TagUtil.getTagSafe(armor);
        int shield = root.getInteger("weeds");
        long lastTick = root.getLong("lastWeedTick");
        long currentTick = world.getTotalWorldTime();

        if (shield >= MAX_SHIELD) {
            if (lastTick != currentTick) {
                root.setLong("lastWeedTick", currentTick);
                armor.setTagCompound(root);
            }
            return;
        }

        if (lastTick == 0 || lastTick > currentTick) {
            root.setLong("lastWeedTick", currentTick);
            armor.setTagCompound(root);
            return;
        }

        int interval = (world.provider.getDimension() == ModConfig.dimensionId) ? BONUS_INTERVAL : NORMAL_INTERVAL;
        long diff = currentTick - lastTick;

        if (diff >= interval * 20L) {
            int add = (int) (diff / (interval * 20L));
            shield = Math.min(MAX_SHIELD, shield + add);
            long newLastTick = currentTick - (diff % (interval * 20L));
            root.setInteger("weeds", shield);
            root.setLong("lastWeedTick", newLastTick);
            armor.setTagCompound(root);
        }
    }

    @Override
    public int onArmorDamage(ItemStack armor, DamageSource source, int damage, int newDamage, EntityPlayer player, int slot) {
        NBTTagCompound root = TagUtil.getTagSafe(armor);
        int shield = root.getInteger("weeds");
        if (shield > 0) {
            int consumed = Math.min(shield, newDamage);
            shield -= consumed;
            newDamage -= consumed;
            root.setInteger("weeds", shield);
            armor.setTagCompound(root);
        }
        return newDamage;
    }
}