package com.sakuraryoko.mixin;

import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.DefaultMinecartController;
import net.minecraft.entity.vehicle.MinecartController;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.config.ConfigSettings;
import com.sakuraryoko.events.PlayerEvents;
import com.sakuraryoko.utils.IPlayerInvoker;

@Mixin(DefaultMinecartController.class)
public abstract class MixinDefaultMinecraftController extends MinecartController
{
	protected MixinDefaultMinecraftController(AbstractMinecartEntity minecart)
	{
		super(minecart);
	}

	@Inject(method = "handleCollision",
	               at = @At(value = "INVOKE",
	                 target = "Lnet/minecraft/entity/Entity;pushAwayFrom(Lnet/minecraft/entity/Entity;)V",
	                        shift = At.Shift.BEFORE,
	                        ordinal = 0))
	private void more_leashing$onHandleCollision(CallbackInfoReturnable<Boolean> cir,
	                                             @Local Entity entity)
	{
		if (ConfigSettings.INSTANCE.isCapturePlayersInMinecarts() &&
			entity instanceof ServerPlayerEntity pe &&
			!this.minecart.hasControllingPassenger() &&
			!pe.hasVehicle() &&
				((IPlayerInvoker) pe).more_leashing$canBeCaptured())
		{
			if (pe.startRiding(this.minecart))
			{
				PlayerEvents.onPlayerCapturedByVehicle(pe, this.minecart);
			}
		}
	}
}
