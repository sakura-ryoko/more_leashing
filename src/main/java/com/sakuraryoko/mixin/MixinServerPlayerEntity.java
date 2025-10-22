package com.sakuraryoko.mixin;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sakuraryoko.config.ConfigSettings;
import com.sakuraryoko.events.PlayerEvents;
import com.sakuraryoko.utils.IPlayerInvoker;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity
		implements IPlayerInvoker
{
	@Unique Entity leader = null;
	@Unique boolean captured = false;
	@Unique private boolean isAfk = false;

	public MixinServerPlayerEntity(World world, GameProfile profile)
	{
		super(world, profile);
	}

	@Override
	public void pushAwayFrom(Entity entity)
	{
		if (ConfigSettings.INSTANCE.isCapturePlayersInMinecarts() &&
			entity instanceof AbstractMinecartEntity)
		{
			return;
		}
		else if (ConfigSettings.INSTANCE.isCapturePlayersInBoats() &&
				 entity instanceof AbstractBoatEntity)
		{
			return;
		}

		super.pushAwayFrom(entity);
	}

	@Inject(method = "updateLastActionTime", at = @At("HEAD"))
	private void more_leashing$updateLastAction(CallbackInfo ci)
	{
		if (this.isAfk && !this.captured)
		{
			this.isAfk = false;
			PlayerEvents.onPlayerNotAfk((ServerPlayerEntity) (Object) this);
		}
	}

	@Override
	public void more_leashing$toggleCaptured(@Nullable Entity leader)
	{
		boolean wasCaptured = this.captured;

		if (wasCaptured)
		{
			if (this.isAfk) this.isAfk = false;
			this.captured = false;
			this.leader = null;
		}
		else
		{
			if (!this.isAfk) this.isAfk = true;
			this.captured = true;
			this.leader = leader;
		}
	}

	@Override
	public boolean more_leashing$isCaptured()
	{
		return this.captured;
	}

	@Override
	public void more_leashing$toggleAfk()
	{
		this.isAfk = !this.isAfk;
	}

	@Override
	public boolean more_leashing$canBeCaptured()
	{
		return this.isAfk;
	}

	@Override
	public @Nullable Entity more_leashing$getLeader()
	{
		return this.leader;
	}
}
