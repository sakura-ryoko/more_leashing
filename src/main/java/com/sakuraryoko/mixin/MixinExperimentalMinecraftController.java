package com.sakuraryoko.mixin;

import java.util.List;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.ExperimentalMinecartController;
import net.minecraft.entity.vehicle.MinecartController;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.config.ConfigSettings;
import com.sakuraryoko.events.PlayerEvents;
import com.sakuraryoko.utils.IPlayerInvoker;

@Mixin(ExperimentalMinecartController.class)
public abstract class MixinExperimentalMinecraftController extends MinecartController
{
	protected MixinExperimentalMinecraftController(AbstractMinecartEntity minecart)
	{
		super(minecart);
	}

	@Inject(method = "pickUpEntities",
	        at = @At(value = "INVOKE",
	                 target = "Ljava/util/List;isEmpty()Z"),
	        cancellable = true)
	private void more_leashing$onHandleCollision(Box box, CallbackInfoReturnable<Boolean> cir,
	                                             @Local List<Entity> list)
	{
		if (ConfigSettings.INSTANCE.isCapturePlayersInMinecarts() &&
			!this.minecart.hasControllingPassenger() &&
			!list.isEmpty())
		{
			for (Entity ent : list)
			{
				if (ent instanceof ServerPlayerEntity pe &&
					!pe.hasVehicle() &&
						((IPlayerInvoker) pe).more_leashing$canBeCaptured())
				{
					if (pe.startRiding(this.minecart))
					{
						PlayerEvents.onPlayerCapturedByVehicle(pe, this.minecart);
						cir.setReturnValue(true);
					}
				}
			}
		}
	}
}
