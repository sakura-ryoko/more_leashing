package com.sakuraryoko.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobEntity.class)
public abstract class MixinMobEntity extends LivingEntity
{
	protected MixinMobEntity(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@ModifyReturnValue(method = "canBeLeashed",
	                   at = @At("RETURN"))
	private boolean more_leashing$allowMobLeashing(boolean original)
	{
		return true;
	}
}
