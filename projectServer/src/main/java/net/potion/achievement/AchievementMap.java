package net.potion.achievement;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class AchievementMap {
    public static AchievementMap instance = new AchievementMap();
    private Int2ObjectMap<String> guidMap = new Int2ObjectRBTreeMap<>();

    private AchievementMap() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(AchievementMap.class.getResourceAsStream("/achievement/map.txt")));

            String str;
            while ((str = reader.readLine()) != null) {
                String[] split = str.split(",");
                int parsed = Integer.parseInt(split[0]);
                this.guidMap.put(parsed, split[1]);
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getGuid(int id) {
        return instance.guidMap.get(id);
    }
}
