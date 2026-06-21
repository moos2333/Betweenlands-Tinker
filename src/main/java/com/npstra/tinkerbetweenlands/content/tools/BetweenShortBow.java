package com.npstra.tinkerbetweenlands.content.tools;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import slimeknights.tconstruct.library.client.crosshair.Crosshairs;
import slimeknights.tconstruct.library.client.crosshair.ICrosshair;
import slimeknights.tconstruct.library.client.crosshair.ICustomCrosshairUser;
import slimeknights.tconstruct.library.events.ProjectileEvent;
import slimeknights.tconstruct.library.events.TinkerToolEvent;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.BowStringMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.tinkering.Category;
import slimeknights.tconstruct.library.tinkering.PartMaterialType;
import slimeknights.tconstruct.library.tools.ProjectileLauncherNBT;
import slimeknights.tconstruct.library.tools.ranged.BowCore;
import slimeknights.tconstruct.library.utils.ToolHelper;
import thebetweenlands.api.item.CorrosionHelper;
import thebetweenlands.api.item.ICorrodible;
import thebetweenlands.common.capability.circlegem.CircleGemHelper;
import com.npstra.tinkerbetweenlands.api.IBetweenlandsTool;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static com.npstra.tinkerbetweenlands.content.parts.ModParts.*;

public class BetweenShortBow extends BowCore implements ICorrodible, IBetweenlandsTool, ICustomCrosshairUser {

    public BetweenShortBow() {
        super(
                PartMaterialType.bow(BETWEENLANDS_LIMB),
                PartMaterialType.bow(BETWEENLANDS_LIMB),
                PartMaterialType.bowstring(BETWEENLANDS_BOWSTRING)
        );
        this.addCategory(Category.LAUNCHER);
        this.setTranslationKey("tinkerbetweenlands.between_shortbow");
        CorrosionHelper.addCorrosionPropertyOverrides(this);
        CircleGemHelper.addGemPropertyOverrides(this);
    }

    @Override
    public int getDrawTime() {
        return 12;
    }

    @Override
    public float baseProjectileDamage() {
        return 0.0f;
    }

    @Override
    public float damagePotential() {
        return 0.7f;
    }

    @Override
    public double attackSpeed() {
        return 1.5d;
    }

    @Override
    protected float baseInaccuracy() {
        return 1.0f;
    }

    @Override
    public float projectileDamageModifier() {
        return 0.8f;
    }

    @Override
    protected List<Item> getAmmoItems() {
        List<Item> ammo = new ArrayList<>();
        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof ItemArrow) {
                ammo.add(item);
            }
        }
        return ammo;
    }

    @Override
    public ProjectileLauncherNBT buildTagData(List<Material> materials) {
        ProjectileLauncherNBT data = new ProjectileLauncherNBT();
        data.head(new HeadMaterialStats[]{
                materials.get(0).getStatsOrUnknown("head"),
                materials.get(1).getStatsOrUnknown("head")
        });
        data.limb(new BowMaterialStats[]{
                materials.get(0).getStatsOrUnknown("bow"),
                materials.get(1).getStatsOrUnknown("bow")
        });
        data.bowstring(new BowStringMaterialStats[]{
                materials.get(2).getStatsOrUnknown("bowstring")
        });
        return data;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity holder, int slot, boolean isSelected) {
        CorrosionHelper.updateCorrosion(stack, world, holder, slot, isSelected);
        this.preventSlowDown(holder, 0.5f);
        super.onUpdate(stack, world, holder, slot, isSelected);
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack old, ItemStack newS) {
        return CorrosionHelper.shouldCauseBlockBreakReset(old, newS);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack old, ItemStack newS, boolean slotChanged) {
        return CorrosionHelper.shouldCauseReequipAnimation(old, newS, slotChanged);
    }

    @Override
    public ICrosshair getCrosshair(ItemStack itemStack, EntityPlayer player) {
        return Crosshairs.SQUARE;
    }

    @Override
    public float getCrosshairState(ItemStack itemStack, EntityPlayer player) {
        return this.getDrawbackProgress(itemStack, player);
    }

    @Override
    public void shootProjectile(@Nonnull ItemStack ammoIn, @Nonnull ItemStack bow, World worldIn, EntityPlayer player, int useTime) {
        float progress = this.getDrawbackProgress(bow, useTime);
        float power = ItemBow.getArrowVelocity((int)(progress * 20.0F)) * progress * this.baseProjectileSpeed();
        power *= ProjectileLauncherNBT.from(bow).range;
        power *= CorrosionHelper.getModifier(bow);

        if (!worldIn.isRemote) {
            TinkerToolEvent.OnBowShoot event = TinkerToolEvent.OnBowShoot.fireEvent(bow, ammoIn, player, useTime, this.baseInaccuracy());
            ItemStack ammoStackToShoot = ammoIn.copy();

            for (int i = 0; i < event.projectileCount; ++i) {
                boolean usedAmmo = false;
                if (i == 0 || event.consumeAmmoPerProjectile) {
                    usedAmmo = this.consumeAmmo(ammoIn, player);
                }
                float inaccuracy = event.getBaseInaccuracy();
                if (i > 0) {
                    inaccuracy += event.bonusInaccuracy;
                }

                EntityArrow projectile = this.getProjectileEntity(ammoStackToShoot, bow, worldIn, player, power, inaccuracy, progress * progress, usedAmmo);
                if (projectile != null && ProjectileEvent.OnLaunch.fireEvent(projectile, bow, player)) {
                    if (progress >= 1.0F) {
                        projectile.setIsCritical(true);
                    }
                    if (!player.capabilities.isCreativeMode) {
                        ToolHelper.damageTool(bow, 1, player);
                    }
                    worldIn.spawnEntity(projectile);
                }
            }
        }

        this.playShootSound(power, worldIn, player);
    }
}