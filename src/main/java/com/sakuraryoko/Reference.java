package com.sakuraryoko;

import net.minecraft.SharedConstants;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class Reference
{
	public static final String MOD_ID = "more_leashing";
	public static final String MOD_NAME = "More Leashing";
	public static final String MOD_VERSION = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata().getVersion().getFriendlyString();
	public static final String MC_VERSION = SharedConstants.getGameVersion().id();
    public static Path ROOT_DIR = FabricLoader.getInstance().getGameDir();
    public static Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir();
	public static final boolean DEBUG = true;
}
