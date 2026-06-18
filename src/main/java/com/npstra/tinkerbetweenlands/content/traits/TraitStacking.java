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

    private TraitStacking() {
        super("stacking", 0x00AA00);
    }

    @Override
    public int onToolHeal(ItemStack tool, int amount, int newAmount, EntityLivingBase entity) {
        if (newAmount <= 1) {
            return 0;
        }
        int halved = newAmount / 2;
        if (newAmount > 1000) {
            ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
            Data data = modtag.getTagData(Data.class);
            if (data.stack < MAX_STACK) {
                data.stack++;
                modtag.save();
            }
        }
        return halved;
    }

    @Override
    public int onToolDamage(ItemStack tool, int damage, int newDamage, EntityLivingBase entity) {
        ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
        Data data = modtag.getTagData(Data.class);
        if (data.stack > 0 && entity.world.rand.nextFloat() < data.stack * 0.01f) {
            return 0;
        }
        return newDamage;
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        ModifierTagHolder modtag = ModifierTagHolder.getModifier(tool, getModifierIdentifier());
        Data data = modtag.getTagData(Data.class);
        String loc = String.format(LOC_Extra, getModifierIdentifier());
        return ImmutableList.of(Util.translateFormatted(loc, data.stack));
    }

    public static class Data extends ModifierNBT {
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