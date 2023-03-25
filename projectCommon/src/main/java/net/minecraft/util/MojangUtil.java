package net.minecraft.util;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MojangUtil {
    private static final LoadingCache<String, String> UUIDS = Caffeine.newBuilder()
            .maximumSize(1_000)
            .build(MojangUtil::requestMojangUuid);

    private static final String SKIN_URL_BASE = System.getProperty("skinUrlBase", "https://crafatar.com/skins/");
    private static final String CAPE_URL_BASE = System.getProperty("capeUrlBase", "https://crafatar.com/capes/");
    private static final Pattern PLAYER_UUID_PATTERN = Pattern.compile("\"id\" : \"([0-9a-f]{32})\"");

    public static String getSkinUrl(String playerName) {
        String uuid = UUIDS.get(playerName);
        if (uuid == null) return null;

        return SKIN_URL_BASE + uuid;
    }

    public static String getCapeUrl(String playerName) {
        String uuid = UUIDS.get(playerName);
        if (uuid == null) return null;

        return CAPE_URL_BASE + uuid;
    }

    private static @Nullable String requestMojangUuid(String playerName) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
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
        } catch (IOException e) {
            return null;
        }
    }
}
