package com.sakuraryoko.config;

import com.google.gson.JsonElement;
import com.sakuraryoko.MoreLeashing;
import com.sakuraryoko.Reference;
import com.sakuraryoko.events.ConfigEvents;
import com.sakuraryoko.utils.JsonUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class ConfigManager
{
    public static final ConfigManager INSTANCE = new ConfigManager();

    private final String CONFIG_DIR = "config";
    private final String CONFIG_FILE = Reference.MOD_ID+".json";

    private Path root = Reference.ROOT_DIR;
    private Path config = Reference.CONFIG_DIR;

    private ConfigManager()
    {
    }

    public void setRoot(Path root)
    {
        this.root = root;
        this.setConfig(root.resolve(CONFIG_DIR));
    }

    public void setConfig(Path config)
    {
        this.config = config;
        this.ensureDirExists(config);
    }

    public Path getRoot() { return this.root.normalize(); }

    public Path getConfig()
    {
        Path check = this.config.normalize();
        this.ensureDirExists(check);
        return check;
    }

    public String getConfigFile() { return this.CONFIG_FILE; }

    public Path getConfigFileAsPath() { return this.config.resolve(CONFIG_FILE).normalize(); }

    private void ensureDirExists(Path dir) throws RuntimeException
    {
        try
        {
            if (!Files.isDirectory(dir))
            {
                Files.createDirectory(dir);
            }
        }
        catch (Exception err)
        {
            final String message = String.format("ensureDirExists: Exception creating folder '%s'; %s", dir.toAbsolutePath().toString(), err.getLocalizedMessage());
            MoreLeashing.LOGGER.error("{}", message);
            throw new RuntimeException(message);
        }
    }

    public boolean loadConfig()
    {
        Optional<JsonElement> loaded = JsonUtils.readJsonFile(this.getConfig(), this.getConfigFile());

        if (loaded.isPresent())
        {
            ConfigSettings.INSTANCE.fromJson(loaded.get());
            ConfigEvents.onLoad();
            return true;
        }

        return false;
    }

    public boolean saveConfig()
    {
        Optional<JsonElement> opt = ConfigSettings.INSTANCE.toJson();

        if (opt.isPresent())
        {
            boolean result = JsonUtils.writeJsonFile(opt.get(), this.getConfig(), this.getConfigFile());

            if (result)
            {
                ConfigEvents.onSave();
            }

            return result;
        }

        return false;
    }

    public boolean reloadConfig()
    {
        Optional<JsonElement> opt = ConfigSettings.INSTANCE.toJson();

        if (opt.isPresent())
        {
            boolean result = JsonUtils.writeJsonFile(opt.get(), this.getConfig(), this.getConfigFile());

            if (result)
            {
                Optional<JsonElement> loaded = JsonUtils.readJsonFile(this.getConfig(), this.getConfigFile());

                if (loaded.isPresent())
                {
                    ConfigSettings.INSTANCE.fromJson(loaded.get());
                    ConfigEvents.onReload();
                    return true;
                }
            }
        }

        return false;
    }
}
