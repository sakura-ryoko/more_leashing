package com.sakuraryoko.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MerchantEntity.class)
public abstract class MixinMerchantEntity extends PassiveEntity
{
	protected MixinMerchantEntity(EntityType<? extends PassiveEntity> entityType, World world)
	{
		super(entityType, world);
	}

	@ModifyReturnValue(method = "canBeLeashed", at = @At("RETURN"))
	private boolean more_leashing$canBeLeashed(boolean original)
	{
		return true;
	}
}
