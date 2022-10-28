package net.potion.material;

public class MapColor {
    public static final MapColor[] MAP_COLOR_ARRAY = new MapColor[16];
    public static final MapColor AIR_COLOR = new MapColor(0, 0x000000);
    public static final MapColor GRASS_COLOR = new MapColor(1, 0x7fb238);
    public static final MapColor SAND_COLOR = new MapColor(2, 0xf7e9a3);
    public static final MapColor CLOTH_COLOR = new MapColor(3, 0xa7a7a7);
    public static final MapColor TNT_COLOR = new MapColor(4, 0xff0000);
    public static final MapColor ICE_COLOR = new MapColor(5, 0xa0a0ff);
    public static final MapColor IRON_COLOR = new MapColor(6, 0xa7a7a7);
    public static final MapColor FOLIAGE_COLOR = new MapColor(7, 0x7c00);
    public static final MapColor SNOW_COLOR = new MapColor(8, 0xffffff);
    public static final MapColor CLAY_COLOR = new MapColor(9, 0xa4a8b8);
    public static final MapColor DIRT_COLOR = new MapColor(10, 0xb76a2f);
    public static final MapColor STONE_COLOR = new MapColor(11, 0x707070);
    public static final MapColor WATER_COLOR = new MapColor(12, 0x4040ff);
    public static final MapColor WOOD_COLOR = new MapColor(13, 0x685332);

    public final int colorValue;
    public final int colorIndex;

    private MapColor(int colorIndex, int colorValue) {
        this.colorIndex = colorIndex;
        this.colorValue = colorValue;
        MAP_COLOR_ARRAY[colorIndex] = this;
    }
}
