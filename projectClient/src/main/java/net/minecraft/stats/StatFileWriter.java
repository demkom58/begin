package net.minecraft.stats;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.Session;
import net.minecraft.util.MD5String;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class StatFileWriter {
    private static final Gson GSON = new GsonBuilder().create();

    private final Map<StatBase, Integer> stats = new ConcurrentHashMap<>();
    private boolean changed = false;
    private final StatsSyncher statsSyncher;

    public StatFileWriter(Session session, File rootDirectory) {
        File statsDirectory = new File(rootDirectory, "stats");

        if (!statsDirectory.exists()) {
            statsDirectory.mkdir();
        }

        for (File rootStatFile : rootDirectory.listFiles()) {
            if (rootStatFile.getName().startsWith("stats_") && rootStatFile.getName().endsWith(".dat")) {
                File correctPathStatFile = new File(statsDirectory, rootStatFile.getName());

                if (!correctPathStatFile.exists()) {
                    System.out.println("Relocating " + rootStatFile.getName());
                    rootStatFile.renameTo(correctPathStatFile);
                }
            }
        }

        this.statsSyncher = new StatsSyncher(session, this, statsDirectory);
    }

    public void addStat(StatBase statBase, int addition) {
        addStatToMap(this.stats, statBase, addition);
        this.changed = true;
    }

    public void addStats(Map<StatBase, Integer> map) {
        if (map == null) {
            return;
        }

        this.changed = true;
        map.forEach((k, v) -> addStatToMap(stats, k, v));
    }

    public boolean hasAchievementUnlocked(Achievement achievement) {
        return this.stats.containsKey(achievement);
    }

    public boolean canBeUnlocked(Achievement achievement) {
        return achievement.parentAchievement == null
                || this.hasAchievementUnlocked(achievement.parentAchievement);
    }

    public int getStatsValue(StatBase statBase) {
        return this.stats.getOrDefault(statBase, 0);
    }

    public void syncStats() {
        this.statsSyncher.tryToWrite(stats);
    }

    public void saveAndPush() {
        if (this.changed && this.statsSyncher.shouldSave()) {
            this.statsSyncher.writeStats(stats);
        }

        this.statsSyncher.pushStats();
    }

    public void onExitOrWorldChange() {
    }

    private static void addStatToMap(Map<StatBase, Integer> map, StatBase statBase, int addition) {
        map.compute(statBase, (k, v) -> (v == null ? 0 : v) + addition);
    }

    public static Map<StatBase, Integer> fromJson(String json) {
        final Map<StatBase, Integer> map = new HashMap<>();

        try {
            final StatFile statFile = GSON.fromJson(json, StatFile.class);
            final String hash = checksum("local", statFile.statsChange);
            if (!hash.equals(statFile.checksum)) {
                System.out.println("CHECKSUM MISMATCH");
                return null;
            }

            for (Map.Entry<Integer, Integer> entry : statFile.statsChange.entrySet()) {
                final int statId = entry.getKey();
                final StatBase statBase = StatList.getStat(statId);
                if (statBase == null) {
                    System.out.println(statId + " is not a valid stat");
                    continue;
                }

                map.put(statBase, entry.getValue());
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static String toJson(String name, String sessionId, Map<StatBase, Integer> map) {
        final Map<Integer, Integer> stats = new HashMap<>();
        map.forEach((k, v) -> stats.put(k.statId, v));
        return GSON.toJson(new StatFile(name, sessionId, stats, checksum("local", stats)));
    }

    public static String checksum(String salt, Map<Integer, Integer> map) {
        String hashValue = map.entrySet().stream()
                .map(e -> e.getKey() + ";" + e.getValue())
                .collect(Collectors.joining(","));

        return new MD5String(salt).hash(hashValue);
    }

}
