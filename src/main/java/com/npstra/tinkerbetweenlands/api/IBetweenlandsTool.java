package com.npstra.tinkerbetweenlands.api;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import slimeknights.tconstruct.library.utils.ToolHelper;
import thebetweenlands.api.item.CorrosionHelper;

import java.util.UUID;

public interface IBetweenlandsTool {
    static Multimap<String, AttributeModifier> applyCorrosion(
            Multimap<String, AttributeModifier> map, EntityEquipmentSlot slot, ItemStack stack, UUID uuid) {
        return CorrosionHelper.getAttributeModifiers(map, slot, stack, uuid, ToolHelper.getActualAttack(stack));
    }
}