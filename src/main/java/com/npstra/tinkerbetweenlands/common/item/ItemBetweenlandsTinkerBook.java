package com.npstra.tinkerbetweenlands.common.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.util.LocUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBetweenlandsTinkerBook extends Item {
    public static BookData BOOK_DATA;

    public ItemBetweenlandsTinkerBook() {
        this.setMaxStackSize(1);
        this.setTranslationKey("tinkerbetweenlands.betweenlands_tinker_book");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (world.isRemote && BOOK_DATA != null) {
            if (BOOK_DATA.fontRenderer == null) {
                BOOK_DATA.fontRenderer = Minecraft.getMinecraft().fontRenderer;
            }
            BOOK_DATA.openGui(stack);
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (net.minecraft.util.text.translation.I18n.canTranslate(this.getTranslationKey(stack) + ".tooltip")) {
            tooltip.addAll(LocUtils.getTooltips(TextFormatting.GRAY.toString() +
                    LocUtils.translateRecursive(this.getTranslationKey(stack) + ".tooltip")));
        }
        tooltip.add(TextFormatting.GRAY + "by moos233");
    }
}