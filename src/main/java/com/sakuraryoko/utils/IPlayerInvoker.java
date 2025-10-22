package com.sakuraryoko.utils;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;

public interface IPlayerInvoker
{
	void more_leashing$toggleCaptured(@Nullable Entity leader);
	boolean more_leashing$isCaptured();
	void more_leashing$toggleAfk();
	boolean more_leashing$canBeCaptured();
	@Nullable Entity more_leashing$getLeader();
}
