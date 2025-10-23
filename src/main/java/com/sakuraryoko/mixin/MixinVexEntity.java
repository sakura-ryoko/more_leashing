package com.sakuraryoko.mixin;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.utils.IVexInvoker;

@Mixin(VexEntity.class)
public abstract class MixinVexEntity extends HostileEntity
		implements IVexInvoker
{
	@Shadow private @Nullable LazyEntityReference<MobEntity> owner;
	@Shadow private int lifeTicks;
	@Shadow private boolean alive;

	@Unique @Nullable private LivingEntity livingTarget = null;
	@Unique private boolean isBoatCaptureUnit = false;
	@Unique private boolean isLeadCaptureUnit = false;
	@Unique private boolean isTame = false;

	protected MixinVexEntity(EntityType<? extends HostileEntity> entityType, World world)
	{
		super(entityType, world);
	}

//	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
//	public void more_leashing$tick(CallbackInfo ci)
//	{
//		if (this.isTame &&
//				(this.isBoatCaptureUnit || this.isLeadCaptureUnit))
//		{
//			this.lifeTicks = 20;
//			this.alive = true;
//		}
//	}

//	@Override
//	public boolean isAngryAt(ServerWorld world, PlayerEntity player)
//	{
//		if (this.isTame) return false;
//		else if (this.getTarget() != null && player.getUuid().equals(this.getTarget().getUuid()))
//		{
//			return false;
//		}
//
//		return super.isAngryAt(world, player);
//	}
//
//	@Override
//	public boolean tryAttack(ServerWorld world, Entity target)
//	{
//		if (this.isTame) return false;
//		else if (this.getTarget() != null && target.getUuid().equals(this.getTarget().getUuid()))
//		{
//			return false;
//		}
//
//		return super.tryAttack(world, target);
//	}
//
//	@Override
//	public void playSound(SoundEvent sound, float volume, float pitch)
//	{
//		if (this.isTame) return;
//		super.playSound(sound, volume, pitch);
//	}
//
	@Inject(method = "getOwner*", at = @At("RETURN"),
	        cancellable = true)
	public void getOwner(CallbackInfoReturnable<Entity> cir)
	{
		if (this.isTame)
		{
			cir.setReturnValue(this.livingTarget);
		}
	}

	@Override
	public boolean canBeLeashed()
	{
		return this.isTame;
	}

	@Override
	public void more_leashing$setTarget(@Nullable LivingEntity target)
	{
		this.livingTarget = target;
	}

	@Override
	public void more_leashing$toggleBoatCaptureUnit(boolean toggle)
	{
		this.isBoatCaptureUnit = toggle;
	}

	@Override
	public void more_leashing$toggleLeadCaptureUnit(boolean toggle)
	{
		this.isLeadCaptureUnit = toggle;
	}

	@Override
	public void more_leashing$toggleTame(boolean toggle)
	{
		this.isTame = toggle;
	}

	@Override
	public @Nullable LivingEntity more_leashing$getTarget()
	{
		return this.livingTarget;
	}

	@Override
	public boolean more_leashing$isTrackingBoat()
	{
		return this.isBoatCaptureUnit;
	}

	@Override
	public boolean more_leashing$isTrackingLead()
	{
		return this.isLeadCaptureUnit;
	}

	@Override
	public boolean more_leashing$isTame()
	{
		return this.isTame;
	}

	@Override
	public void more_leashing$kill()
	{
		this.remove(RemovalReason.DISCARDED);
	}

	@Mixin(VexEntity.ChargeTargetGoal.class)
	public abstract static class MixinVexEntity_ChargeTargetGoal extends Goal
	{

	}

	@Mixin(VexEntity.LookAtTargetGoal.class)
	public abstract static class MixinVexEntity_LookAtTargetGoal extends Goal
	{

	}

	@Mixin(VexEntity.TrackOwnerTargetGoal.class)
	public abstract static class MixinVexEntity_TrackOwnerTargetGoal extends TrackTargetGoal
	{
		public MixinVexEntity_TrackOwnerTargetGoal(MobEntity mob, boolean checkVisibility, boolean checkNavigable)
		{
			super(mob, checkVisibility, checkNavigable);
		}
	}

	@Mixin(VexEntity.VexMoveControl.class)
	public abstract static class MixinVexEntity_VexMoveControl extends MoveControl
	{
		public MixinVexEntity_VexMoveControl(MobEntity entity)
		{
			super(entity);
		}
	}
}
