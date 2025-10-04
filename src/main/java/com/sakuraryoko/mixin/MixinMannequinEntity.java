package com.sakuraryoko.mixin;

import org.jetbrains.annotations.Nullable;

import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.MannequinEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MannequinEntity.class)
public abstract class MixinMannequinEntity extends PlayerLikeEntity implements Leashable
{
	@Unique private LeashData leashData;

	protected MixinMannequinEntity(EntityType<? extends LivingEntity> entityType, World world)
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

	@Override
	public void onShortLeashTick(Entity holder)
	{
		Leashable.super.onShortLeashTick(holder);

		if (holder.getEntity() instanceof LivingEntity le &&
			!le.isEntityLookingAtMe(this, 0.025, false, true, le.getEyeY()))
		{
			this.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, holder.getEyePos());
		}
	}

}
