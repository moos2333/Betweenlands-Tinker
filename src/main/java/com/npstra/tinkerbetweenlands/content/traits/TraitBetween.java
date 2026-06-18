package com.npstra.tinkerbetweenlands.content.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;
import com.npstra.tinkerbetweenlands.config.ModConfig;

public class TraitBetween extends AbstractTrait {
    public static final TraitBetween INSTANCE = new TraitBetween();
    private static final int DIM_ID = ModConfig.dimensionId;

    private TraitBetween() {
        super("between", 0x00AA00);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target, float damage, float newDamage, boolean isCritical) {
        if (player == null || player.world == null) {
            return newDamage;
        }
        if (player.world.provider.getDimension() != DIM_ID) {
            return newDamage;
        }
        if (tool.getItem() instanceof IBetweenlandsTool) {
            return newDamage;
        }
        return newDamage * 0.75f;
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        if (event.getEntity() == null || event.getEntity().world == null) {
            return;
        }
        if (event.getEntity().world.provider.getDimension() != DIM_ID) {
            return;
        }
        if (tool.getItem() instanceof IBetweenlandsTool) {
            return;
        }
        event.setNewSpeed(event.getNewSpeed() * 0.75f);
    }
}