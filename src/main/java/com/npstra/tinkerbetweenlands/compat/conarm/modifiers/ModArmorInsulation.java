package com.npstra.tinkerbetweenlands.compat.conarm.modifiers;

import com.google.common.collect.ImmutableList;
import c4.conarm.lib.modifiers.ArmorModifierTrait;
import c4.conarm.lib.utils.RecipeMatchHolder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.mantle.util.RecipeMatch;
import thebetweenlands.common.item.misc.ItemMisc;

import java.util.*;

public class ModArmorInsulation extends ArmorModifierTrait {

    private static final ItemStack RUBBER_BALL = ItemMisc.EnumItemMisc.RUBBER_BALL.create(1);

    public ModArmorInsulation() {
        super("insulation", 0xCCCCCC, 1, 1);
        RecipeMatchHolder.addRecipeMatch(this, new ItemCombination(1,
                RUBBER_BALL, RUBBER_BALL, RUBBER_BALL, RUBBER_BALL, RUBBER_BALL
        ));
    }

    @Override
    public float onHurt(ItemStack armor, EntityPlayer player, DamageSource source, float damage, float newDamage, LivingHurtEvent evt) {
        if (source == DamageSource.LIGHTNING_BOLT) {
            newDamage -= damage * 0.25f;
        }
        return newDamage;
    }

    private static class ItemCombination extends RecipeMatch {
        protected final NonNullList<ItemStack> itemStacks;

        public ItemCombination(int amountMatched, ItemStack... stacks) {
            super(amountMatched, 0);
            NonNullList<ItemStack> nonNullStacks = NonNullList.withSize(stacks.length, ItemStack.EMPTY);
            for (int i = 0; i < stacks.length; i++) {
                if (!stacks[i].isEmpty()) {
                    nonNullStacks.set(i, stacks[i].copy());
                }
            }
            this.itemStacks = nonNullStacks;
        }

        @Override
        public List<ItemStack> getInputs() {
            return ImmutableList.copyOf(this.itemStacks);
        }

        @Override
        public Optional<Match> matches(NonNullList<ItemStack> stacks) {
            List<ItemStack> found = new LinkedList<>();
            Set<Integer> needed = new HashSet<>();
            for (int i = 0; i < this.itemStacks.size(); i++) {
                if (!this.itemStacks.get(i).isEmpty()) {
                    needed.add(i);
                }
            }
            for (ItemStack stack : stacks) {
                Iterator<Integer> iter = needed.iterator();
                while (iter.hasNext()) {
                    int index = iter.next();
                    ItemStack template = this.itemStacks.get(index);
                    if (ItemStack.areItemsEqual(template, stack) && ItemStack.areItemStackTagsEqual(template, stack)) {
                        ItemStack copy = stack.copy();
                        copy.setCount(1);
                        found.add(copy);
                        iter.remove();
                        break;
                    }
                }
            }
            if (needed.isEmpty()) {
                return Optional.of(new Match(found, this.amountMatched));
            }
            return Optional.empty();
        }
    }
}