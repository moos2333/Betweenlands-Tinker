package com.npstra.tinkerbetweenlands.content.traits;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.traits.AbstractTrait;
import thebetweenlands.common.world.WorldProviderBetweenlands;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;

public class TraitBetween extends AbstractTrait {
    public static final TraitBetween INSTANCE = new TraitBetween();
    private static final float PENALTY = 0.75f;

    private TraitBetween() {
        super("between", 0x00AA00);
    }

    @Override
    public float damage(ItemStack tool, EntityLivingBase player, EntityLivingBase target,
                        float damage, float newDamage, boolean isCritical) {
        if (player == null || player.world == null) return newDamage;
        if (!(player.world.provider instanceof WorldProviderBetweenlands)) return newDamage;
        if (tool.getItem() instanceof IBetweenlandsTool) return newDamage;
        return newDamage * PENALTY;
    }

    @Override
    public void miningSpeed(ItemStack tool, PlayerEvent.BreakSpeed event) {
        if (event.getEntity() == null || event.getEntity().world == null) return;
        if (!(event.getEntity().world.provider instanceof WorldProviderBetweenlands)) return;
        if (tool.getItem() instanceof IBetweenlandsTool) return;
        event.setNewSpeed(event.getNewSpeed() * PENALTY);
    }
}