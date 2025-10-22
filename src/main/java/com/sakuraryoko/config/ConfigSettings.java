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

    private final String CAPTURE_PLAYERS_IN_BOATS       = "capturePlayersInBoats";
    private final String CAPTURE_PLAYERS_IN_MINECARTS   = "capturePlayersInMinecarts";
	private final String CAPTURE_PLAYERS_AFK_TIMEOUT    = "capturePlayersAfkTimeout";
    private final String LEAD_PLAYERS                   = "leadPlayers";

    private boolean capturePlayersInBoats = true;
    private boolean capturePlayersInMinecarts = true;
    private boolean leadPlayers = true;
	private int capturePlayersAfkTimeout = 15;

    private ConfigSettings()
    {
    }

	public boolean isCapturePlayersInMinecarts()
	{
		return this.capturePlayersInMinecarts;
	}

	public boolean isCapturePlayersInBoats()
    {
        return this.capturePlayersInBoats;
    }

	public int getCapturePlayersAfkTimeout()
	{
		return this.capturePlayersAfkTimeout;
	}

    public boolean isLeadPlayers()
    {
        return this.leadPlayers;
    }

	public void setCapturePlayersInMinecarts(boolean toggle)
	{
		this.capturePlayersInMinecarts = toggle;
	}

	public void setCapturePlayersInBoats(boolean toggle)
    {
        this.capturePlayersInBoats = toggle;
    }

	public void setCapturePlayersAfkTimeout(int timeout)
	{
		this.capturePlayersAfkTimeout = timeout;
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

                if (JsonUtils.hasBoolean(obj, CAPTURE_PLAYERS_IN_MINECARTS))
                {
                    this.setCapturePlayersInMinecarts(JsonUtils.getBoolean(obj, CAPTURE_PLAYERS_IN_MINECARTS));
                }
	            if (JsonUtils.hasBoolean(obj, CAPTURE_PLAYERS_IN_BOATS))
	            {
		            this.setCapturePlayersInBoats(JsonUtils.getBoolean(obj, CAPTURE_PLAYERS_IN_BOATS));
	            }
	            if (JsonUtils.hasInteger(obj, CAPTURE_PLAYERS_AFK_TIMEOUT))
	            {
		            this.setCapturePlayersAfkTimeout(JsonUtils.getInteger(obj, CAPTURE_PLAYERS_AFK_TIMEOUT));
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

	        obj.add(CAPTURE_PLAYERS_IN_MINECARTS, new JsonPrimitive(this.isCapturePlayersInMinecarts()));
            obj.add(CAPTURE_PLAYERS_IN_BOATS, new JsonPrimitive(this.isCapturePlayersInBoats()));
	        obj.add(CAPTURE_PLAYERS_AFK_TIMEOUT, new JsonPrimitive(this.getCapturePlayersAfkTimeout()));
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
