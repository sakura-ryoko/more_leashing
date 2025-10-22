package com.sakuraryoko.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends PlayerLikeEntity
{
	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

//	//	@Override
//	@Unique
//	public Vec3d getLeashOffset()
//	{
//		return new Vec3d(
//				0.0F,
//				this.getAttachmentYOffset(),
//				this.getWidth() * 0.2F
//		);
//	}
//
//	@Unique
//	private double getAttachmentYOffset()
//	{
//		return this.getStandingEyeHeight() -
//				((this.getHeight() - this.getStandingEyeHeight()) * 1.8F);
//	}

}
