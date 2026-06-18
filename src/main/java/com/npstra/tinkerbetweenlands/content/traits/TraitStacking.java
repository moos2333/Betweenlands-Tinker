package com.npstra.tinkerbetweenlands.content.traits;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierNBT;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.utils.ModifierTagHolder;
import java.util.List;

public class TraitStacking extends AbstractTrait {
    public static final TraitStacking INSTANCE = new TraitStacking();
    private static final int MAX_STACK = 100;
    private static final int REPAIR_THRESHOLD = 500;

    private TraitStacking() {
        super("stacking", 0x00AA00);
    }

    @Override
    public int onToolHeal(ItemStack tool, int amount, int newAmount, EntityLivingBase entity) {
        int halved = newAmount / 2;
        if (amount > REPAIR_THRESHOLD) {
            ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
            Data data = modtag.getTagData(Data.class);
            if (data == null) {
                data = new Data();
                data.stack = 0;
                data.write(modtag.tag);
                modtag.save();
            }
            if (data.stack < MAX_STACK) {
                data.stack++;
                modtag.save();
            }
        }
        return halved;
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        if (entity == null) {
            return newDamage;
        }
        ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
        Data data = modtag.getTagData(Data.class);
        if (data == null) {
            data = new Data();
            data.stack = 0;
            data.write(modtag.tag);
            modtag.save();
            return newDamage;
        }
        if (data.stack > 0 && entity.world.rand.nextFloat() < data.stack * 0.01f) {
            return 0;
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
        Data data = modtag.getTagData(Data.class);
        int stackCount = (data == null) ? 0 : data.stack;
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, stackCount));
    }

    private static class Data extends ModifierNBT {
        public int stack;

        @Override
        public void read(NBTTagCompound tag) {
            super.read(tag);
            stack = tag.getInteger("stack");
        }

        @Override
        public void write(NBTTagCompound tag) {
            super.write(tag);
            tag.setInteger("stack", stack);
        }
    }
}