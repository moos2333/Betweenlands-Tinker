package com.npstra.tinkerbetweenlands.content.modifiers;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.modifiers.ModifierTrait;
import thebetweenlands.common.entity.mobs.EntitySwampHag;

import java.util.Collections;
import java.util.List;

public class ModHagHacker extends ModifierTrait {
    public ModHagHacker() {
        super("hag_hacker", 0x8B008B, 3, 1);
        Item item = Item.getByNameOrId("thebetweenlands:hag_hacker");
        if (item != null) {
            ItemStack stack = new ItemStack(item);
            stack.setItemDamage(32767);
            stack.setTagCompound(null);
            items.add(new RecipeMatch.Item(stack, 1, 1));
        }
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        if (target instanceof EntitySwampHag) {
            int level = getData(tool).level;
            newDamage += level * 7f;
        }
        return super.damage(tool, player, target, damage, newDamage, isCritical);
    }

    @Override
    public List<String> getExtraInfo(ItemStack tool, NBTTagCompound modifierTag) {
        int level = getData(tool).level;
        float bonus = level * 7f;
        String loc = String.format(LOC_Extra, getIdentifier());
        return Collections.singletonList(Util.translateFormatted(loc, bonus));
    }
}