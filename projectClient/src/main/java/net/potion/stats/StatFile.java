package net.potion.stats;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class StatFile {
    public User user;
    @SerializedName("stats-change")
    public Map<Integer, Integer> statsChange;
    public String checksum;

    public StatFile() {
    }

    public StatFile(String name, String sessionId, Map<Integer, Integer> statsChange, String checksum) {
        this.user = new User(name, sessionId);
        this.statsChange = statsChange;
        this.checksum = checksum;
    }

    private static class User {
        public String name;
        public String sessionid;

        public User(String name, String sessionid) {
            this.name = name;
            this.sessionid = sessionid;
        }
    }
}
