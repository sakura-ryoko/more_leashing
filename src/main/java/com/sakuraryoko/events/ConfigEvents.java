package com.sakuraryoko.events;

import com.sakuraryoko.MoreLeashing;

public class ConfigEvents
{
    public static void onSave()
    {
        MoreLeashing.debugLog("ConfigManager#loadConfig(): Config saved successfully.");
    }

    public static void onLoad()
    {
        MoreLeashing.debugLog("ConfigManager#loadConfig(): Config loaded successfully.");
    }

    public static void onReload()
    {
        MoreLeashing.debugLog("ConfigManager#loadConfig(): Config reloaded successfully.");
    }
}
