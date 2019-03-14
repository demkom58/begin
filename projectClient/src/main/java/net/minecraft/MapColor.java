package net.minecraft;

public class MapColor {
    public static final MapColor[] MAP_COLOR_ARRAY = new MapColor[16];
    public static final MapColor AIR_COLOR = new MapColor(0, 0);
    public static final MapColor GRASS_COLOR = new MapColor(1, 8368696);
    public static final MapColor SAND_COLOR = new MapColor(2, 16247203);
    public static final MapColor CLOTH_COLOR = new MapColor(3, 10987431);
    public static final MapColor TNT_COLOR = new MapColor(4, 16711680);
    public static final MapColor ICE_COLOR = new MapColor(5, 10526975);
    public static final MapColor IRON_COLOR = new MapColor(6, 10987431);
    public static final MapColor FOLIAGE_COLOR = new MapColor(7, 31744);
    public static final MapColor SNOW_COLOR = new MapColor(8, 16777215);
    public static final MapColor CLAY_COLOR = new MapColor(9, 10791096);
    public static final MapColor DIRT_COLOR = new MapColor(10, 12020271);
    public static final MapColor STONE_COLOR = new MapColor(11, 7368816);
    public static final MapColor WATER_COLOR = new MapColor(12, 4210943);
    public static final MapColor WOOD_COLOR = new MapColor(13, 6837042);
    public final int colorValue;
    public final int colorIndex;

    private MapColor(int colorIndex, int colorValue) {
        this.colorIndex = colorIndex;
        this.colorValue = colorValue;
        MAP_COLOR_ARRAY[colorIndex] = this;
    }
}
