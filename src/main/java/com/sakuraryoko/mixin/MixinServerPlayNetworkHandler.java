package com.sakuraryoko.mixin;

import java.util.Collections;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPosition;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sakuraryoko.config.ConfigSettings;
import com.sakuraryoko.events.PlayerEvents;
import com.sakuraryoko.utils.IPlayerInvoker;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class MixinServerPlayNetworkHandler
{
	@Shadow public ServerPlayerEntity player;
//	@Shadow public abstract void requestTeleport(double x, double y, double z, float yaw, float pitch);
	@Shadow protected abstract boolean handlePendingTeleport();

	@Inject(method = "tick", at = @At("HEAD"))
	private void more_leashing$onTick(CallbackInfo ci)
	{
		if (((IPlayerInvoker) this.player).more_leashing$canBeCaptured())
		{
			return;
		}

		long duration = Util.getMeasuringTimeMs() - this.player.getLastActionTime();
		int timeout = ConfigSettings.INSTANCE.getCapturePlayersAfkTimeout();

		if (duration > (timeout * 1000L))
		{
			((IPlayerInvoker) this.player).more_leashing$toggleAfk();
			PlayerEvents.onPlayerMarkAfk(this.player);
		}
	}

	@Inject(method = "onPlayerMove", at = @At("HEAD"), cancellable = true)
	private void more_leashing$onPlayerMove(PlayerMoveC2SPacket packet, CallbackInfo ci)
	{
		IPlayerInvoker invoker = (IPlayerInvoker) this.player;
		float pitch = MathHelper.wrapDegrees(packet.getPitch(this.player.getPitch()));
		float yaw = MathHelper.wrapDegrees(packet.getYaw(this.player.getYaw()));

		if (packet.changesLook() && !invoker.more_leashing$isCaptured())
		{
			if (pitch != packet.getPitch(pitch) || yaw != packet.getYaw(yaw))
			{
				this.player.updateLastActionTime();
			}
		}
		else if (invoker.more_leashing$isCaptured())
		{
			NetworkThreadUtils.forceMainThread(packet, (ServerPlayNetworkHandler) (Object) this, this.player.getEntityWorld());

			if (this.player.isLoaded())
			{
				if (this.player.getStatusEffect(StatusEffects.SLOWNESS) == null)
				{
					this.player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 500, 50), invoker.more_leashing$getLeader());
				}

				if (this.handlePendingTeleport())
				{
					this.player.setAngles(yaw, pitch);
				}

				if (packet.changesPosition())
				{
					if (this.player.hasVehicle() &&
							(ConfigSettings.INSTANCE.isCapturePlayersInBoats()) ||
							ConfigSettings.INSTANCE.isCapturePlayersInMinecarts())
					{
						Entity veh = this.player.getRootVehicle();

						veh.updatePassengerPosition(this.player);
					}

					this.player.move(MovementType.PISTON, Vec3d.ZERO);
					this.player.networkHandler.sendPacket(
							new PlayerPositionLookS2CPacket(0,
							                                EntityPosition.fromEntity(this.player),
							                                Collections.emptySet())
					);

					ci.cancel();
				}
			}
			else
			{
				invoker.more_leashing$toggleCaptured(null);
			}
		}
	}
}
