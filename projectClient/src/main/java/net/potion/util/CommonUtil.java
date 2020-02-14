package net.potion.util;

public class CommonUtil {
    public static String getDirectionName(int id) {
        switch (id) {
            case 0:
                return "West";
            case 1:
                return "North";
            case 2:
                return "East";
            case 3:
                return "South";
            default:
                return "Unknown Direction";
        }
    }
}
