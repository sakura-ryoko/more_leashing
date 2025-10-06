package com.sakuraryoko.utils;

import com.google.gson.*;
import com.sakuraryoko.MoreLeashing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

public class JsonUtils
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Nullable
    public static JsonObject getNestedObject(JsonObject parent, String key, boolean create)
    {
        if (parent.has(key) == false || parent.get(key).isJsonObject() == false)
        {
            if (create == false)
            {
                return null;
            }

            JsonObject obj = new JsonObject();
            parent.add(key, obj);
            return obj;
        }
        else
        {
            return parent.get(key).getAsJsonObject();
        }
    }

    public static boolean hasBoolean(JsonObject obj, String name)
    {
        JsonElement el = obj.get(name);

        if (el != null && el.isJsonPrimitive())
        {
            try
            {
                el.getAsBoolean();
                return true;
            }
            catch (Exception ignore) {}
        }

        return false;
    }

    public static boolean hasInteger(JsonObject obj, String name)
    {
        JsonElement el = obj.get(name);

        if (el != null && el.isJsonPrimitive())
        {
            try
            {
                el.getAsInt();
                return true;
            }
            catch (Exception ignore) {}
        }

        return false;
    }

    public static boolean hasLong(JsonObject obj, String name)
    {
        JsonElement el = obj.get(name);

        if (el != null && el.isJsonPrimitive())
        {
            try
            {
                el.getAsLong();
                return true;
            }
            catch (Exception ignore) {}
        }

        return false;
    }

    public static boolean hasFloat(JsonObject obj, String name)
    {
        JsonElement el = obj.get(name);

        if (el != null && el.isJsonPrimitive())
        {
            try
            {
                el.getAsFloat();
                return true;
            }
            catch (Exception ignore) {}
        }

        return false;
    }

    public static boolean hasDouble(JsonObject obj, String name)
    {
        JsonElement el = obj.get(name);

        if (el != null && el.isJsonPrimitive())
        {
            try
            {
                el.getAsDouble();
                return true;
            }
            catch (Exception ignore) {}
        }

        return false;
    }

    public static boolean hasString(JsonObject obj, String name)
    {
        JsonElement el = obj.get(name);

        if (el != null && el.isJsonPrimitive())
        {
            try
            {
                el.getAsString();
                return true;
            }
            catch (Exception ignore) {}
        }

        return false;
    }

    public static boolean hasObject(JsonObject obj, String name)
    {
        JsonElement el = obj.get(name);
        return el != null && el.isJsonObject();
    }

    public static boolean hasArray(JsonObject obj, String name)
    {
        JsonElement el = obj.get(name);
        return el != null && el.isJsonArray();
    }

    public static boolean getBooleanOrDefault(JsonObject obj, String name, boolean defaultValue)
    {
        if (obj.has(name) && obj.get(name).isJsonPrimitive())
        {
            try
            {
                return obj.get(name).getAsBoolean();
            }
            catch (Exception ignore) {}
        }

        return defaultValue;
    }

    public static int getIntegerOrDefault(JsonObject obj, String name, int defaultValue)
    {
        if (obj.has(name) && obj.get(name).isJsonPrimitive())
        {
            try
            {
                return obj.get(name).getAsInt();
            }
            catch (Exception ignore) {}
        }

        return defaultValue;
    }

    public static long getLongOrDefault(JsonObject obj, String name, long defaultValue)
    {
        if (obj.has(name) && obj.get(name).isJsonPrimitive())
        {
            try
            {
                return obj.get(name).getAsLong();
            }
            catch (Exception ignore) {}
        }

        return defaultValue;
    }

    public static float getFloatOrDefault(JsonObject obj, String name, float defaultValue)
    {
        if (obj.has(name) && obj.get(name).isJsonPrimitive())
        {
            try
            {
                return obj.get(name).getAsFloat();
            }
            catch (Exception ignore) {}
        }

        return defaultValue;
    }

    public static double getDoubleOrDefault(JsonObject obj, String name, double defaultValue)
    {
        if (obj.has(name) && obj.get(name).isJsonPrimitive())
        {
            try
            {
                return obj.get(name).getAsDouble();
            }
            catch (Exception ignore) {}
        }

        return defaultValue;
    }

    public static String getStringOrDefault(JsonObject obj, String name, String defaultValue)
    {
        if (obj.has(name) && obj.get(name).isJsonPrimitive())
        {
            try
            {
                return obj.get(name).getAsString();
            }
            catch (Exception ignore) {}
        }

        return defaultValue;
    }

    public static boolean getBoolean(JsonObject obj, String name)
    {
        return getBooleanOrDefault(obj, name, false);
    }

    public static int getInteger(JsonObject obj, String name)
    {
        return getIntegerOrDefault(obj, name, 0);
    }

    public static long getLong(JsonObject obj, String name)
    {
        return getLongOrDefault(obj, name, 0);
    }

    public static float getFloat(JsonObject obj, String name)
    {
        return getFloatOrDefault(obj, name, 0);
    }

    public static double getDouble(JsonObject obj, String name)
    {
        return getDoubleOrDefault(obj, name, 0);
    }

    @Nullable
    public static String getString(JsonObject obj, String name)
    {
        return getStringOrDefault(obj, name, null);
    }

    public static boolean hasBlockPos(JsonObject obj, String name)
    {
        return blockPosFromJson(obj, name) != null;
    }

    public static JsonArray blockPosToJson(BlockPos pos)
    {
        JsonArray arr = new JsonArray();

        arr.add(pos.getX());
        arr.add(pos.getY());
        arr.add(pos.getZ());

        return arr;
    }

    @Nullable
    public static BlockPos blockPosFromJson(JsonObject obj, String name)
    {
        if (hasArray(obj, name))
        {
            JsonArray arr = obj.getAsJsonArray(name);

            if (arr.size() == 3)
            {
                try
                {
                    return new BlockPos(arr.get(0).getAsInt(), arr.get(1).getAsInt(), arr.get(2).getAsInt());
                }
                catch (Exception ignore) {}
            }
        }

        return null;
    }

    public static boolean hasVec3d(JsonObject obj, String name)
    {
        return vec3dFromJson(obj, name) != null;
    }

    public static JsonArray vec3dToJson(Vec3d vec)
    {
        JsonArray arr = new JsonArray();

        arr.add(vec.x);
        arr.add(vec.y);
        arr.add(vec.z);

        return arr;
    }

    @Nullable
    public static Vec3d vec3dFromJson(JsonObject obj, String name)
    {
        if (hasArray(obj, name))
        {
            JsonArray arr = obj.getAsJsonArray(name);

            if (arr.size() == 3)
            {
                try
                {
                    return new Vec3d(arr.get(0).getAsDouble(), arr.get(1).getAsDouble(), arr.get(2).getAsDouble());
                }
                catch (Exception ignore) {}
            }
        }

        return null;
    }

    public static Optional<JsonElement> readJsonFile(Path dir, String fileName)
    {
        try
        {
            if (!Files.exists(dir))
            {
                MoreLeashing.LOGGER.error("readJsonFile: Error reading file, directory '{}' does not exist.", dir.toAbsolutePath()
                                                                                                                 .toString());
                return Optional.empty();
            }

            if (!Files.isDirectory(dir))
            {
                MoreLeashing.LOGGER.error("readJsonFile: Error reading file, '{}' is not a directory.", dir.toAbsolutePath()
                                                                                                           .toString());
                return Optional.empty();
            }

            fileName = FileNameUtils.wrapJsonFileName(fileName);

            Path file = dir.resolve(fileName);

            if (Files.exists(file))
            {
                if (Files.isReadable(file))
                {
                    try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file), StandardCharsets.UTF_8))
                    {
                        return Optional.of(JsonParser.parseReader(reader));
                    }
                    catch (Exception err)
                    {
                        MoreLeashing.LOGGER.error("readJsonFile: Exception reading file, '{}'; {}", file.toAbsolutePath()
                                                                                                        .toString(), err.getLocalizedMessage());
                    }
                }
                else
                {
                    MoreLeashing.LOGGER.error("readJsonFile: Error reading file, '{}' is not readable.", file.toAbsolutePath()
                                                                                                             .toString());
                }
            }
            else
            {
                MoreLeashing.LOGGER.error("readJsonFile: Error reading file, '{}' does not exist.", file.toAbsolutePath()
                                                                                                        .toString());
            }
        }
        catch (Exception err)
        {
            MoreLeashing.LOGGER.error("readJsonFile: Exception reading file, '{}' -> {}; {}", dir.toAbsolutePath().toString(), fileName, err.getLocalizedMessage());
        }

        return Optional.empty();
    }

    public static boolean writeJsonFile(JsonElement root, Path dir, String fileName)
    {
        fileName = FileNameUtils.wrapJsonFileName(fileName);
        Path fileTemp = dir.resolve(fileName + ".tmp");
        Path file = dir.resolve(fileName);

        if (Files.exists(fileTemp))
        {
            fileTemp = dir.resolve(fileName + UUID.randomUUID() + ".tmp");
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(fileTemp), StandardCharsets.UTF_8))
        {
            writer.write(GSON.toJson(root));
            writer.close();

            if (Files.exists(file))
            {
                try
                {
                    Files.delete(file);
                }
                catch (Exception err)
                {
                    MoreLeashing.LOGGER.warn("writeJsonFile: Failed to delete file '{}'; {}", file.toAbsolutePath().toString(), err.getLocalizedMessage());
                }
            }

            try
            {
                Files.move(fileTemp, file);
                return true;
            }
            catch (Exception err)
            {
                MoreLeashing.LOGGER.error("writeJsonFile: Failed to move file '{}'; {}", file.toAbsolutePath().toString(), err.getLocalizedMessage());
            }
        }
        catch (Exception err)
        {
            MoreLeashing.LOGGER.error("writeJsonFile: Failed to write JSON data to file '{}'; {}", fileTemp.toAbsolutePath().toString(), err.getLocalizedMessage());
        }

        return false;
    }
}
