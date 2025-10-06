package com.sakuraryoko.mixin;

import com.sakuraryoko.config.ConfigSettings;
import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends PlayerLikeEntity implements Leashable
{
	@Shadow public abstract void tickMovement();
	@Shadow public abstract void travel(Vec3d movementInput);
	@Unique private LeashData leashData;

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@Override
	public @Nullable LeashData getLeashData()
	{
		return this.leashData;
	}

	@Override
	public void setLeashData(@Nullable Leashable.LeashData leashData)
	{
		this.leashData = leashData;
	}

	@Inject(method = "writeCustomData", at = @At("TAIL"))
	public void writeCustomData(WriteView view, CallbackInfo ci)
	{
		this.writeLeashData(view, this.leashData);
	}

	@Inject(method = "readCustomData", at = @At("TAIL"))
	public void readCustomData(ReadView view, CallbackInfo ci)
	{
		this.readLeashData(view);
	}

	@Override
	public Vec3d getLeashOffset()
	{
		return new Vec3d(
				0.0F,
				this.getAttachmentYOffset(),
				this.getWidth() * 0.2F
		);
	}

	@Unique
	private double getAttachmentYOffset()
	{
		return this.getStandingEyeHeight() -
				((this.getHeight() - this.getStandingEyeHeight()) * 1.8F);
	}

//	@Override
//	public void onShortLeashTick(Entity holder)
//	{
//		Leashable.super.onShortLeashTick(holder);
//		this.tickMoveTowardsHolder(holder, 2.0F);
//	}

//FIXME -- Doesn't work
//	@Unique
//	private void tickMoveTowardsHolder(Entity holder, double maxDiff)
//	{
//		double diff = this.getLeashSnappingDistance() - maxDiff;
//		double range = this.distanceTo(holder);
//		int ticks = 4;
//
//		while (range >= diff && ticks > 0)
//		{
//			((LivingEntity) this).travel(holder.getMovement());
////			this.tickMovement();
//			range = this.distanceTo(holder);
//			ticks--;
//		}
//	}

    @Override
    public boolean canBeLeashed()
    {
        return ConfigSettings.INSTANCE.isLeadPlayers();
    }
}
