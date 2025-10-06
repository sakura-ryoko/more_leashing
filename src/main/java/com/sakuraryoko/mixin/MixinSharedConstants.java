package com.sakuraryoko.mixin;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.sakuraryoko.Reference;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SharedConstants.class)
public class MixinSharedConstants
{
	@Shadow public static boolean isDevelopment;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void more_leashing$sharedConstants(CallbackInfo ci)
	{
		isDevelopment = Reference.DEBUG;
	}

    @Inject(method = "propertyIsSet", at = @At("RETURN"), cancellable = true)
    private static void more_leashing$isPropertySet(String name, CallbackInfoReturnable<Boolean> cir)
    {
        if (Reference.DEBUG)
        {
            switch (name.toUpperCase())
            {
                case "MC_DEBUG_ENABLED",
                     "MC_DEBUG_PATHFINDING" -> cir.setReturnValue(true);
                default -> {}
            }
        }
    }
}
