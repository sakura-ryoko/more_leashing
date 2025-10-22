package com.sakuraryoko.events;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import com.sakuraryoko.MoreLeashing;
import com.sakuraryoko.utils.IPlayerInvoker;

public class PlayerEvents
{
	public static void onPlayerMarkAfk(ServerPlayerEntity pe)
	{
		MoreLeashing.debugLog("Player: [{}] has been marked as AFK and can be captured.", pe.getName().getString());
	}

	public static void onPlayerNotAfk(ServerPlayerEntity pe)
	{
		MoreLeashing.debugLog("Player: [{}] has been marked as not AFK and cannot be captured.", pe.getName().getString());
	}

	public static void onPlayerCapturedByVehicle(ServerPlayerEntity pe, VehicleEntity veh)
	{
		((IPlayerInvoker) pe).more_leashing$toggleCaptured(veh);
		MoreLeashing.debugLog("Player: [{}] has been captured by [{}]", pe.getName().getString(), veh.getName().getString());
	}

	public static void onPlayerCapturedByLeash(ServerPlayerEntity pe, Entity leader)
	{
		((IPlayerInvoker) pe).more_leashing$toggleCaptured(leader);
		MoreLeashing.debugLog("Player: [{}] has been captured by [{}]", pe.getName().getString(), leader.getName().getString());
	}

	public static void onPlayerSetFree(ServerPlayerEntity pe, @Nullable Entity leader)
	{
		((IPlayerInvoker) pe).more_leashing$toggleCaptured(null);

		if (pe.getStatusEffect(StatusEffects.SLOWNESS) != null)
		{
			pe.removeStatusEffect(StatusEffects.SLOWNESS);
		}

		if (leader != null)
		{
			MoreLeashing.debugLog("Player: [{}] has been set free by [{}]", pe.getName().getString(), leader.getName().getString());
		}
		else
		{
			MoreLeashing.debugLog("Player: [{}] has been set free", pe.getName().getString());
		}
	}
}
