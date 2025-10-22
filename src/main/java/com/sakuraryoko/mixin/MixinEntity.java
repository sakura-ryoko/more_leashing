package com.sakuraryoko.mixin;

import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.ImmutableList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.sakuraryoko.config.ConfigSettings;
import com.sakuraryoko.events.PlayerEvents;
import com.sakuraryoko.utils.IPlayerInvoker;

@Mixin(Entity.class)
public abstract class MixinEntity
{
	@Shadow public abstract EntityType<?> getType();
	@Shadow private ImmutableList<Entity> passengerList;

	@Inject(method = "addPassenger",
	          at = @At(value = "RETURN"))
	private void more_leashing$addPassenger2(Entity passenger, CallbackInfo ci)
	{
		if (ConfigSettings.INSTANCE.isCapturePlayersInBoats() &&
			this.getType().isIn(EntityTypeTags.BOAT) &&
				passenger != null)
		{
			List<Entity> newList = new ArrayList<>(this.passengerList);
			Entity firstEnt = newList.getFirst();

			if (firstEnt instanceof ServerPlayerEntity fpe &&
				((IPlayerInvoker) fpe).more_leashing$isCaptured() &&
					passenger instanceof ServerPlayerEntity npe &&
				!((IPlayerInvoker) npe).more_leashing$isCaptured())
			{
				// Mark any uncaptured players as the first (Controlling player)
				if (newList.size() > 1)
				{
					newList.removeFirst();
					newList.addLast(firstEnt);
				}
				else
				{
					newList.clear();
					newList.add(passenger);
					newList.add(firstEnt);
				}

				this.passengerList = ImmutableList.copyOf(newList);
			}
		}
	}

	@Inject(method = "updatePassengerForDismount", at = @At("HEAD"))
	private void more_leashing$removePassenger1(LivingEntity passenger, CallbackInfoReturnable<Vec3d> cir)
	{
		if (passenger instanceof ServerPlayerEntity pe)
		{
			if (((IPlayerInvoker) pe).more_leashing$isCaptured())
			{
				PlayerEvents.onPlayerSetFree(pe, (Entity) (Object) this);
			}
		}
	}

	@Inject(method = "removePassenger", at = @At("HEAD"))
	private void more_leashing$removePassenger2(Entity passenger, CallbackInfo ci)
	{
		if (passenger instanceof ServerPlayerEntity pe)
		{
			if (((IPlayerInvoker) pe).more_leashing$isCaptured())
			{
				PlayerEvents.onPlayerSetFree(pe, (Entity) (Object) this);
			}
		}
	}

	@Inject(method = "stopRiding", at = @At("HEAD"))
	private void more_leashing$removePassenger3(CallbackInfo ci)
	{
		Entity thisEntity = (Entity) (Object) this;

		if (thisEntity instanceof ServerPlayerEntity pe &&
				((IPlayerInvoker) pe).more_leashing$isCaptured())
		{
			PlayerEvents.onPlayerSetFree(pe, null);
		}
	}
}
