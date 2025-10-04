package com.sakuraryoko;

import net.minecraft.SharedConstants;
import net.fabricmc.loader.api.FabricLoader;

public class Reference
{
	public static final String MOD_ID = "more_leashing";
	public static final String MOD_NAME = "More Leashing";
	public static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
	public static final String MC_VERSION = SharedConstants.getGameVersion().id();
	public static final boolean DEBUG = true;
}
