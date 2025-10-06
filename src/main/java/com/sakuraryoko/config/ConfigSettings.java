package com.sakuraryoko.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sakuraryoko.MoreLeashing;
import com.sakuraryoko.utils.JsonUtils;

import java.util.Optional;

public class ConfigSettings
{
    public static final ConfigSettings INSTANCE = new ConfigSettings();

    private final String LEAD_MANNEQUINS    = "leadMannequins";
    private final String LEAD_MERCHANTS     = "leadMerchants";
    private final String LEAD_MOBS          = "leadMobs";
    private final String LEAD_PLAYERS       = "leadPlayers";

    private boolean leadMannequins = true;
    private boolean leadMerchants = true;
    private boolean leadMobs = true;
    private boolean leadPlayers = true;

    private ConfigSettings()
    {
    }

    public boolean isLeadMannequins()
    {
        return this.leadMannequins;
    }

    public boolean isLeadMerchants()
    {
        return this.leadMerchants;
    }

    public boolean isLeadMobs()
    {
        return this.leadMobs;
    }

    public boolean isLeadPlayers()
    {
        return this.leadPlayers;
    }

    public void setLeadMannequins(boolean leadMannequins)
    {
        this.leadMannequins = leadMannequins;
    }

    public void setLeadMerchants(boolean leadMerchants)
    {
        this.leadMerchants = leadMerchants;
    }

    public void setLeadMobs(boolean leadMobs)
    {
        this.leadMobs = leadMobs;
    }

    public void setLeadPlayers(boolean leadPlayers)
    {
        this.leadPlayers = leadPlayers;
    }

    protected void fromJson(JsonElement ele)
    {
        try
        {
            if (ele.isJsonObject())
            {
                JsonObject obj = ele.getAsJsonObject();

                if (JsonUtils.hasBoolean(obj, LEAD_MANNEQUINS))
                {
                    this.setLeadMannequins(JsonUtils.getBoolean(obj, LEAD_MANNEQUINS));
                }
                if (JsonUtils.hasBoolean(obj, LEAD_MERCHANTS))
                {
                    this.setLeadMerchants(JsonUtils.getBoolean(obj, LEAD_MERCHANTS));
                }
                if (JsonUtils.hasBoolean(obj, LEAD_MOBS))
                {
                    this.setLeadMobs(JsonUtils.getBoolean(obj, LEAD_MOBS));
                }
                if (JsonUtils.hasBoolean(obj, LEAD_PLAYERS))
                {
                    this.setLeadPlayers(JsonUtils.getBoolean(obj, LEAD_PLAYERS));
                }
            }
        }
        catch (Exception err)
        {
            MoreLeashing.LOGGER.error("ConfigSettings#fromJson(): Exception reading config; {}", err.getLocalizedMessage());
        }
    }

    protected Optional<JsonElement> toJson()
    {
        try
        {
            JsonObject obj = new JsonObject();

            obj.add(LEAD_MANNEQUINS, new JsonPrimitive(this.isLeadMannequins()));
            obj.add(LEAD_MERCHANTS, new JsonPrimitive(this.isLeadMerchants()));
            obj.add(LEAD_MOBS, new JsonPrimitive(this.isLeadMobs()));
            obj.add(LEAD_PLAYERS, new JsonPrimitive(this.isLeadPlayers()));

            return Optional.of(obj);
        }
        catch (Exception err)
        {
            MoreLeashing.LOGGER.error("ConfigSettings#toJson(): Exception saving config; {}", err.getLocalizedMessage());
        }

        return Optional.empty();
    }
}
