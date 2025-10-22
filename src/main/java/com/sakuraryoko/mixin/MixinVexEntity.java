package com.sakuraryoko.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

import com.sakuraryoko.utils.IVexInvoker;

@Mixin(VexEntity.class)
public abstract class MixinVexEntity extends HostileEntity
		implements IVexInvoker
{
	protected MixinVexEntity(EntityType<? extends HostileEntity> entityType, World world)
	{
		super(entityType, world);
	}
}
