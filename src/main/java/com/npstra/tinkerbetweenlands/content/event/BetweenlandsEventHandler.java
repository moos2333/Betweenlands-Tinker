package com.npstra.tinkerbetweenlands.content.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.tools.ToolCore;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.library.utils.ToolHelper;
import thebetweenlands.api.item.CorrosionHelper;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;
import com.npstra.tinkerbetweenlands.config.ModConfig;

public class BetweenlandsEventHandler {
    private static final int DIM_ID = ModConfig.dimensionId;

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().world.provider.getDimension() != DIM_ID) return;
        if (!(event.getSource().getTrueSource() instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
        ItemStack stack = player.getHeldItemMainhand();
        if (!(stack.getItem() instanceof ToolCore) || ToolHelper.isBroken(stack)) return;

        boolean isBetweenTool = stack.getItem() instanceof IBetweenlandsTool;
        boolean hasBetweenTrait = TinkerUtil.hasTrait(TagUtil.getTagSafe(stack), "between");

        if (isBetweenTool || hasBetweenTrait) {
            event.setAmount(event.getAmount() * CorrosionHelper.getModifier(stack));
        }
    }

    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (event.getEntity().world.provider.getDimension() != DIM_ID) return;
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        if (!(stack.getItem() instanceof ToolCore) || ToolHelper.isBroken(stack)) return;

        boolean isBetweenTool = stack.getItem() instanceof IBetweenlandsTool;
        boolean hasBetweenTrait = TinkerUtil.hasTrait(TagUtil.getTagSafe(stack), "between");

        if (isBetweenTool || hasBetweenTrait) {
            event.setNewSpeed(event.getOriginalSpeed() * CorrosionHelper.getModifier(stack));
        }
    }
}