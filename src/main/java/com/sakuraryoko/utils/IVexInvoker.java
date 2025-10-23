package com.sakuraryoko.utils;

import javax.annotation.Nullable;

import net.minecraft.entity.LivingEntity;

public interface IVexInvoker
{
	void more_leashing$setTarget(@Nullable LivingEntity target);
	void more_leashing$toggleBoatCaptureUnit(boolean toggle);
	void more_leashing$toggleLeadCaptureUnit(boolean toggle);
	void more_leashing$toggleTame(boolean toggle);
	@Nullable LivingEntity more_leashing$getTarget();
	boolean more_leashing$isTrackingBoat();
	boolean more_leashing$isTrackingLead();
	boolean more_leashing$isTame();
	void more_leashing$kill();
}
