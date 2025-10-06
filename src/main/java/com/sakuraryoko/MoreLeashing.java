package com.sakuraryoko;

import com.sakuraryoko.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class MoreLeashing implements ModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger(Reference.MOD_ID);

	@Override
	public void onInitialize()
	{
        if (!ConfigManager.INSTANCE.loadConfig())
        {
            debugLog(Reference.MOD_ID+": Saving new config file.");
            ConfigManager.INSTANCE.saveConfig();
        }
	}

	public static void debugLog(String str, Object... args)
	{
		if (Reference.DEBUG)
		{
			LOGGER.info(str, args);
		}
	}
}