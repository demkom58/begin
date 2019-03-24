package net.minecraft;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class StatFileWriter {
    private Map field_25102_a = new HashMap();
    private Map field_25101_b = new HashMap();
    private boolean field_27189_c = false;
    private StatsSyncher statsSyncher;

    public StatFileWriter(Session var1, File var2) {
        File var3 = new File(var2, "stats");
        if (!var3.exists()) {
            var3.mkdir();
        }

        for (File var7 : var2.listFiles()) {
            if (var7.getName().startsWith("stats_") && var7.getName().endsWith(".dat")) {
                File var8 = new File(var3, var7.getName());
                if (!var8.exists()) {
                    System.out.println("Relocating " + var7.getName());
                    var7.renameTo(var8);
                }
            }
        }

        this.statsSyncher = new StatsSyncher(var1, this, var3);
    }

    public static Map func_27177_a(String var0) {
        HashMap var1 = new HashMap();

        try {
            String var2 = "local";
            StringBuilder var3 = new StringBuilder();
            J_JsonRootNode var4 = (new J_JdomParser()).func_27367_a(var0);

            for (J_JsonNode var7 : var4.func_27217_b("stats-change")) {
                Map var8 = var7.func_27214_c();
                Entry var9 = (Entry) var8.entrySet().iterator().next();
                int var10 = Integer.parseInt(((J_JsonStringNode) var9.getKey()).func_27216_b());
                int var11 = Integer.parseInt(((J_JsonNode) var9.getValue()).func_27216_b());
                StatBase var12 = StatList.func_27361_a(var10);
                if (var12 == null) {
                    System.out.println(var10 + " is not a valid stat");
                } else {
                    var3.append(StatList.func_27361_a(var10).statGuid).append(",");
                    var3.append(var11).append(",");
                    var1.put(var12, var11);
                }
            }

            MD5String var14 = new MD5String(var2);
            String var15 = var14.func_27369_a(var3.toString());
            if (!var15.equals(var4.func_27213_a("checksum"))) {
                System.out.println("CHECKSUM MISMATCH");
                return null;
            }
        } catch (J_InvalidSyntaxException e) {
            e.printStackTrace();
        }

        return var1;
    }

    public static String func_27185_a(String var0, String var1, Map<StatBase, Object> var2) {
        StringBuilder var3 = new StringBuilder();
        StringBuilder var4 = new StringBuilder();
        boolean var5 = true;
        var3.append("{\r\n");
        if (var0 != null && var1 != null) {
            var3.append("  \"user\":{\r\n");
            var3.append("    \"name\":\"").append(var0).append("\",\r\n");
            var3.append("    \"sessionid\":\"").append(var1).append("\"\r\n");
            var3.append("  },\r\n");
        }

        var3.append("  \"stats-change\":[");

        for (StatBase var7 : var2.keySet()) {
            if (!var5) {
                var3.append("},");
            } else {
                var5 = false;
            }

            var3.append("\r\n    {\"").append(var7.statId).append("\":").append(var2.get(var7));
            var4.append(var7.statGuid).append(",");
            var4.append(var2.get(var7)).append(",");
        }

        if (!var5) {
            var3.append("}");
        }

        MD5String var8 = new MD5String(var1);
        var3.append("\r\n  ],\r\n");
        var3.append("  \"checksum\":\"").append(var8.func_27369_a(var4.toString())).append("\"\r\n");
        var3.append("}");
        return var3.toString();
    }

    public void readStat(StatBase var1, int var2) {
        this.writeStatToMap(this.field_25101_b, var1, var2);
        this.writeStatToMap(this.field_25102_a, var1, var2);
        this.field_27189_c = true;
    }

    private void writeStatToMap(Map var1, StatBase var2, int var3) {
        Integer var4 = (Integer) var1.get(var2);
        int var5 = var4 == null ? 0 : var4;
        var1.put(var2, var5 + var3);
    }

    public Map func_27176_a() {
        return new HashMap<>(this.field_25101_b);
    }

    public void func_27179_a(Map<StatBase, Object> var1) {
        if (var1 != null) {
            this.field_27189_c = true;

            for (StatBase var3 : var1.keySet()) {
                this.writeStatToMap(this.field_25101_b, var3, (Integer) var1.get(var3));
                this.writeStatToMap(this.field_25102_a, var3, (Integer) var1.get(var3));
            }

        }
    }

    public void func_27180_b(Map<StatBase, Object> var1) {
        if (var1 != null) {
            for (StatBase var3 : var1.keySet()) {
                Integer var4 = (Integer) this.field_25101_b.get(var3);
                int var5 = var4 == null ? 0 : var4;
                this.field_25102_a.put(var3, ((Integer) var1.get(var3)).intValue() + var5);
            }

        }
    }

    public void func_27187_c(Map<StatBase, Object> var1) {
        if (var1 != null) {
            this.field_27189_c = true;

            for (StatBase var3 : var1.keySet()) {
                this.writeStatToMap(this.field_25101_b, var3, (Integer) var1.get(var3));
            }

        }
    }

    public boolean hasAchievementUnlocked(Achievement var1) {
        return this.field_25102_a.containsKey(var1);
    }

    public boolean func_27181_b(Achievement var1) {
        return var1.parentAchievement == null || this.hasAchievementUnlocked(var1.parentAchievement);
    }

    public int writeStat(StatBase var1) {
        Integer var2 = (Integer) this.field_25102_a.get(var1);
        return var2 == null ? 0 : var2;
    }

    public void func_27175_b() {
    }

    public void syncStats() {
        this.statsSyncher.syncStatsFileWithMap(this.func_27176_a());
    }

    public void func_27178_d() {
        if (this.field_27189_c && this.statsSyncher.func_27420_b()) {
            this.statsSyncher.func_27424_a(this.func_27176_a());
        }

        this.statsSyncher.func_27425_c();
    }
}
