package com.npstra.tinkerbetweenlands.content.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import thebetweenlands.common.registries.BlockRegistry;
import thebetweenlands.common.world.WorldProviderBetweenlands;
import thebetweenlands.common.world.storage.BetweenlandsWorldStorage;
import com.npstra.tinkerbetweenlands.content.fluid.FluidRegister;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class SulfurLeachEventHandler {
    private static final ConcurrentHashMap<BlockPos, Long> tracked = new ConcurrentHashMap<>();
    private static final int CHECK_INTERVAL = 20;
    private static final float CHANCE = 0.2F;
    private static final int AMOUNT = 400;

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        World world = event.getWorld();
        if (world.isRemote) return;
        BlockPos pos = event.getPos();
        if (world.getBlockState(pos).getBlock() == BlockRegistry.SULFUR_BLOCK) {
            BlockPos below = pos.down();
            IBlockState belowState = world.getBlockState(below);
            if (belowState.getBlock() == BlockRegistry.SYRMORITE_BARREL) {
                tracked.put(below, world.getTotalWorldTime());
            }
        }
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        if (event.world.isRemote) return;
        if (event.phase != TickEvent.Phase.END) return;
        if (tracked.isEmpty()) return;
        World world = event.world;
        long time = world.getTotalWorldTime();
        if (time % CHECK_INTERVAL != 0) return;
        boolean isRaining;
        if (world.provider instanceof WorldProviderBetweenlands) {
            isRaining = BetweenlandsWorldStorage.forWorld(world).getEnvironmentEventRegistry().heavyRain.isActive();
        } else {
            isRaining = world.isRaining();
        }
        if (!isRaining) return;
        Iterator<BlockPos> it = tracked.keySet().iterator();
        while (it.hasNext()) {
            BlockPos pos = it.next();
            if (!world.isBlockLoaded(pos)) continue;
            IBlockState tankState = world.getBlockState(pos);
            if (tankState.getBlock() != BlockRegistry.SYRMORITE_BARREL) {
                it.remove();
                continue;
            }
            BlockPos above = pos.up();
            IBlockState sulfurState = world.getBlockState(above);
            if (sulfurState.getBlock() != BlockRegistry.SULFUR_BLOCK) {
                it.remove();
                continue;
            }
            TileEntity te = world.getTileEntity(pos);
            if (te == null) {
                it.remove();
                continue;
            }
            IFluidHandler handler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            if (handler == null) {
                it.remove();
                continue;
            }
            if (world.rand.nextFloat() < CHANCE) {
                FluidStack molten = new FluidStack(FluidRegister.fluidMoltenSulfur, AMOUNT);
                if (handler.fill(molten, true) == AMOUNT) {
                    world.setBlockToAir(above);
                }
            }
        }
    }
}