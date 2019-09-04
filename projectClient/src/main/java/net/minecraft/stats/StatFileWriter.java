package net.minecraft.stats;

import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import net.minecraft.achievement.Achievement;
import net.minecraft.client.Session;
import net.minecraft.json.*;
import net.minecraft.util.MD5String;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StatFileWriter {
    private Map<StatBase, Integer> map1 = new HashMap<>();
    private Map<StatBase, Integer> map2 = new HashMap<>();
    private boolean field_27189_c = false;
    private StatsSyncher statsSyncher;

    public StatFileWriter(Session session, File rootDirectory) {
        File statsDirectory = new File(rootDirectory, "stats");

        if (!statsDirectory.exists())
            statsDirectory.mkdir();

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

    public static Map<StatBase, Integer> readStatMap(String json) {
        Map<StatBase, Integer> map = new HashMap<>();

        try {
            String salt = "local";
            StringBuilder builder = new StringBuilder();
            J_JsonRootNode node = new J_JdomParser().func_27367_a(json);

            for (J_JsonNode jsonNode : node.func_27217_b("stats-change")) {
                Map<J_JsonStringNode, J_JsonNode> var8 = jsonNode.func_27214_c();
                Entry<J_JsonStringNode, J_JsonNode> entry = var8.entrySet().iterator().next();
                int statId = Integer.parseInt(entry.getKey().getValue());
                int statValue = Integer.parseInt(entry.getValue().getValue());
                StatBase statBase = StatList.getStat(statId);

                if (statBase == null) {
                    System.out.println(statId + " is not a valid stat");
                    continue;
                }

                builder.append(StatList.getStat(statId).statGuid).append(",");
                builder.append(statValue).append(",");
                map.put(statBase, statValue);
            }

            MD5String md5String = new MD5String(salt);
            String hash = md5String.hash(builder.toString());
            if (!hash.equals(node.func_27213_a("checksum"))) {
                System.out.println("CHECKSUM MISMATCH");
                return null;
            }
        } catch (J_InvalidSyntaxException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static String toJson(String name, String sessionId, Map<StatBase, Integer> map) {
        StringBuilder builder = new StringBuilder();
        StringBuilder var4 = new StringBuilder();
        boolean var5 = true;
        builder.append("{\r\n");
        if (name != null && sessionId != null) {
            builder.append("  \"user\":{\r\n");
            builder.append("    \"name\":\"").append(name).append("\",\r\n");
            builder.append("    \"sessionid\":\"").append(sessionId).append("\"\r\n");
            builder.append("  },\r\n");
        }

        builder.append("  \"stats-change\":[");

        for (StatBase statBase : map.keySet()) {

            if (!var5)
                builder.append("},");
            else
                var5 = false;

            builder.append("\r\n    {\"").append(statBase.statId).append("\":").append(map.get(statBase));
            var4.append(statBase.statGuid).append(",").append(map.get(statBase)).append(",");
        }

        if (!var5)
            builder.append("}");

        MD5String md5String = new MD5String(sessionId);
        builder.append("\r\n  ],\r\n");
        builder.append("  \"checksum\":\"").append(md5String.hash(var4.toString())).append("\"\r\n");
        builder.append("}");
        return builder.toString();
    }

    public void addStat(StatBase statBase, int var2) {
        this.addStatToMap(this.map2, statBase, var2);
        this.addStatToMap(this.map1, statBase, var2);
        this.field_27189_c = true;
    }

    private void addStatToMap(Map<StatBase, Integer> map, StatBase statBase, int addition) {
        map.put(statBase, map.getOrDefault(statBase, 0) + addition);
    }

    public Map<StatBase, Integer> copyMap2() {
        return new HashMap<>(this.map2);
    }

    public void func_27179_a(Map<StatBase, Integer> map) {
        if (map == null)
            return;

        this.field_27189_c = true;

        for (StatBase statBase : map.keySet()) {
            this.addStatToMap(this.map2, statBase, map.get(statBase));
            this.addStatToMap(this.map1, statBase, map.get(statBase));
        }
    }

    public void func_27180_b(Map<StatBase, Integer> map) {
        if (map == null)
            return;

        for (StatBase statBase : map.keySet())
            this.map1.put(statBase, map.get(statBase) + this.map2.getOrDefault(statBase, 0));
    }

    public void func_27187_c(Map<StatBase, Integer> map) {
        if (map == null)
            return;

        this.field_27189_c = true;

        for (StatBase statBase : map.keySet())
            this.addStatToMap(this.map2, statBase, map.get(statBase));
    }

    public boolean hasAchievementUnlocked(Achievement achievement) {
        return this.map1.containsKey(achievement);
    }

    public boolean func_27181_b(Achievement achievement) {
        return achievement.parentAchievement == null || this.hasAchievementUnlocked(achievement.parentAchievement);
    }

    public int writeStat(StatBase statBase) {
        return this.map1.getOrDefault(statBase, 0);
    }

    public void func_27175_b() {
    }

    public void syncStats() {
        this.statsSyncher.syncStatsFileWithMap(this.copyMap2());
    }

    public void func_27178_d() {
        if (this.field_27189_c && this.statsSyncher.func_27420_b())
            this.statsSyncher.func_27424_a(this.copyMap2());

        this.statsSyncher.func_27425_c();
    }
}
