package com.npstra.tinkerbetweenlands.content.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;

public class TraitBetween extends AbstractTrait {
    public static final TraitBetween INSTANCE = new TraitBetween();
    private TraitBetween() {
        super("between", 0x00AA00);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        if (tool.getItem() instanceof IBetweenlandsTool) {
            return newDamage;
        }
        return newDamage * 0.75f;
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        if (tool.getItem() instanceof IBetweenlandsTool) {
            return;
        }
        event.setNewSpeed(event.getNewSpeed() * 0.75f);
    }
}