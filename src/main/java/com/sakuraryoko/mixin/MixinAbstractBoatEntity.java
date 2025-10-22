package com.sakuraryoko.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.config.ConfigSettings;
import com.sakuraryoko.events.PlayerEvents;
import com.sakuraryoko.utils.IPlayerInvoker;

@Mixin(AbstractBoatEntity.class)
public abstract class MixinAbstractBoatEntity extends VehicleEntity
{
	@Shadow protected abstract int getMaxPassengers();

	public MixinAbstractBoatEntity(EntityType<?> entityType, World world)
	{
		super(entityType, world);
	}

	@Inject(method = "tick",
	        at = @At(value = "INVOKE",
	                 target = "Lnet/minecraft/entity/vehicle/AbstractBoatEntity;pushAwayFrom(Lnet/minecraft/entity/Entity;)V",
	                 shift = At.Shift.BEFORE)
	)
	private void more_leashing$redirectPushAwayFrom(CallbackInfo ci,
	                                                @Local Entity entity)
	{
		if (ConfigSettings.INSTANCE.isCapturePlayersInBoats() &&
			entity instanceof ServerPlayerEntity pe &&
			this.getPassengerList().size() < this.getMaxPassengers() &&
			!pe.hasVehicle() &&
				((IPlayerInvoker) pe).more_leashing$canBeCaptured())
		{
			if (pe.startRiding(this))
			{
				PlayerEvents.onPlayerCapturedByVehicle(pe, this);
			}
		}
	}

	@Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
	private void more_leashing$cancelPushAwayFrom(Entity entity, CallbackInfo ci)
	{
		if (ConfigSettings.INSTANCE.isCapturePlayersInBoats() &&
			entity instanceof ServerPlayerEntity pe &&
			this.hasPassenger(pe))
		{
			ci.cancel();
		}
	}

	@Inject(method = "getControllingPassenger", at = @At("RETURN"), cancellable = true)
	private void more_leashing$restrictCapturedPassenger(CallbackInfoReturnable<LivingEntity> cir)
	{
		LivingEntity passenger = cir.getReturnValue();

		if (ConfigSettings.INSTANCE.isCapturePlayersInBoats() &&
			passenger instanceof ServerPlayerEntity pe &&
				((IPlayerInvoker) pe).more_leashing$isCaptured())
		{
			cir.setReturnValue(null);
		}
	}
}
