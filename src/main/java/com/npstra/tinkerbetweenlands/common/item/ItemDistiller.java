package com.npstra.tinkerbetweenlands.common.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thebetweenlands.api.item.IAnimatorRepairable;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.registries.ItemRegistry;
import com.npstra.tinkerbetweenlands.content.fluid.FluidRegister;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDistiller extends Item implements IAnimatorRepairable {
    public static final int CAPACITY = 1000;
    public static final int SULFUR_PER_BLOCK = 250;
    public static final int MAX_DURABILITY = 33;
    public static final int CHARGE_DURATION = 32;
    private static final int USE_DURATION = 72000;
    private static final String TAG_AMOUNT = "DistillerAmount";

    public ItemDistiller() {
        setMaxStackSize(1);
        setMaxDamage(MAX_DURABILITY);
        setTranslationKey("tinkerbetweenlands.distiller");
    }

    private static int getAmount(ItemStack stack) {
        NBTTagCompound tag = stack.getTagCompound();
        return tag == null ? 0 : tag.getInteger(TAG_AMOUNT);
    }

    private static void setAmount(ItemStack stack, int amount) {
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
        }
        tag.setInteger(TAG_AMOUNT, amount);
    }

    private static boolean canExtract(ItemStack stack) {
        return stack.getItemDamage() < MAX_DURABILITY - 1 && getAmount(stack) < CAPACITY;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!canExtract(stack)) {
            return new ActionResult<>(EnumActionResult.FAIL, stack);
        }
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int timeLeft) {
        if (USE_DURATION - timeLeft < CHARGE_DURATION) return;
        if (!(entity instanceof EntityPlayer)) return;
        EntityPlayer player = (EntityPlayer) entity;
        if (!canExtract(stack)) return;

        RayTraceResult result = rayTrace(world, player, false);
        if (result == null || result.typeOfHit != Type.BLOCK) return;
        BlockPos pos = result.getBlockPos();
        if (world.getBlockState(pos).getBlock() != BlockRegistry.SULFUR_BLOCK) return;

        if (world.isRemote) {
            spawnExtractionParticles(world, result);
            return;
        }

        world.setBlockToAir(pos);
        setAmount(stack, Math.min(CAPACITY, getAmount(stack) + SULFUR_PER_BLOCK));
        stack.damageItem(1, player);
        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 0.6F, 0.8F);
    }

    private static void spawnExtractionParticles(World world, RayTraceResult result) {
        double x = result.hitVec.x;
        double y = result.hitVec.y;
        double z = result.hitVec.z;
        for (int i = 0; i < 8; i++) {
            world.spawnParticle(EnumParticleTypes.FLAME,
                    x + world.rand.nextFloat() * 0.4 - 0.2,
                    y + world.rand.nextFloat() * 0.4 - 0.2,
                    z + world.rand.nextFloat() * 0.4 - 0.2,
                    0, 0.05, 0);
        }
        for (int i = 0; i < 4; i++) {
            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL,
                    x + world.rand.nextFloat() * 0.4 - 0.2,
                    y + world.rand.nextFloat() * 0.4 - 0.2,
                    z + world.rand.nextFloat() * 0.4 - 0.2,
                    0, 0.05, 0);
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos,
                                      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        int amount = getAmount(stack);
        if (amount <= 0) return EnumActionResult.PASS;

        TileEntity te = world.getTileEntity(pos);
        if (te == null) return EnumActionResult.PASS;
        IFluidHandler target = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
        if (target == null) return EnumActionResult.PASS;
        if (world.isRemote) return EnumActionResult.SUCCESS;

        FluidStack offer = new FluidStack(FluidRegister.fluidMoltenSulfur, amount);
        int accepted = target.fill(offer, false);
        if (accepted <= 0) return EnumActionResult.FAIL;
        target.fill(new FluidStack(FluidRegister.fluidMoltenSulfur, accepted), true);
        setAmount(stack, amount - accepted);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return USE_DURATION;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMinRepairFuelCost(ItemStack stack) {
        return 4;
    }

    @Override
    public int getFullRepairFuelCost(ItemStack stack) {
        return 12;
    }

    @Override
    public int getMinRepairLifeCost(ItemStack stack) {
        return 8;
    }

    @Override
    public int getFullRepairLifeCost(ItemStack stack) {
        return 24;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == ItemRegistry.OCTINE_INGOT || super.getIsRepairable(toRepair, repair);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return getAmount(stack) > 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.tinkerbetweenlands.distiller.extract"));
        tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.tinkerbetweenlands.distiller.repair"));
        tooltip.add(TextFormatting.GRAY + String.format("%d / %d mB", getAmount(stack), CAPACITY));
    }
}