package net.minecraft.util;

public class CommonUtil {
    public static String getDirectionName(int id) {
        switch (id) {
            case 0:
                return "South";
            case 1:
                return "West";
            case 2:
                return "North";
            case 3:
                return "East";
            default:
                return "Unknown Direction";
        }
    }
}
