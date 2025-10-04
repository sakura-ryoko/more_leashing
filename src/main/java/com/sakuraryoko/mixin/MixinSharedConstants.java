package com.sakuraryoko.mixin;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sakuraryoko.Reference;

@Mixin(SharedConstants.class)
public class MixinSharedConstants
{
	@Shadow public static boolean isDevelopment;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void more_leashing$sharedConstants(CallbackInfo ci)
	{
		isDevelopment = Reference.DEBUG;
	}
}
