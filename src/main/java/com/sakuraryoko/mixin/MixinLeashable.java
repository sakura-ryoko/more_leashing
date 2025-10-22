package com.sakuraryoko.mixin;

import java.util.Set;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPosition;
import net.minecraft.entity.Leashable;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Thanks to PatBox for this
 */
@Mixin(Leashable.class)
public interface MixinLeashable
{
	// TODO
	@WrapOperation(method = "applyElasticity",
	               at = @At(value = "INVOKE",
	                        target = "Lnet/minecraft/entity/Entity;addVelocityInternal(Lnet/minecraft/util/math/Vec3d;)V"))
	private void handleVelocity(Entity instance, Vec3d velocity, Operation<Void> original)
	{
		original.call(instance, velocity);

//		if (instance instanceof ServerPlayerEntity player)
//		{
//			player.networkHandler.sendPacket(
//					new PlayerPositionLookS2CPacket(0,
//					                                new EntityPosition(Vec3d.ZERO, velocity, 0, 0),
//					                                Set.of(PositionFlag.DELTA_X, PositionFlag.DELTA_Y, PositionFlag.DELTA_Z,
//					                                       PositionFlag.X, PositionFlag.Y, PositionFlag.Z,
//					                                       PositionFlag.X_ROT, PositionFlag.Y_ROT)
//			));
//		}
	}
}