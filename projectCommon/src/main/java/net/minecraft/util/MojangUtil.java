package net.minecraft.util;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MojangUtil {
    private static final Map<String, String> uuids = new HashMap<>();
    private static final String SKIN_URL_BASE = System.getProperty("skinUrlBase", "https://crafatar.com/skins/");
    private static final String CAPE_URL_BASE = System.getProperty("capeUrlBase", "https://crafatar.com/capes/");
    private static final Pattern PLAYER_UUID_PATTERN = Pattern.compile("\"id\" : \"([0-9a-f]{32})\"");

    public static String getSkinUrl(String playerName) {
        String uuid = getUuidStringFromName(playerName);
        if (uuid == null) return null;

        System.out.println("Skin URL: " + SKIN_URL_BASE + uuid);
        return SKIN_URL_BASE + uuid;
    }

    public static String getCapeUrl(String playerName) {
        String uuid = getUuidStringFromName(playerName);
        if (uuid == null) return null;

        return CAPE_URL_BASE + uuid;
    }

    private static String getUuidStringFromName(String playerName) {
        if (uuids.containsKey(playerName)) {
            return uuids.get(playerName);
        }

        String uuid = requestMojangUuid(playerName);
        if (uuid != null) {
            uuids.put(playerName, uuid);
        }

        return uuid;
    }

    private static @Nullable String requestMojangUuid(String playerName) {
        try(BufferedReader in = new BufferedReader(new InputStreamReader(
                new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName).openStream()))) {
            String uuidString = null;
            String line;

            while ((line = in.readLine()) != null) {
                Matcher matcher;
                if (line.isEmpty() || !(matcher = PLAYER_UUID_PATTERN.matcher(line)).find()) continue;
                uuidString = matcher.group(1);
                break;
            }

            return uuidString;
        }
        catch (IOException e) {
            return null;
        }
    }
}
