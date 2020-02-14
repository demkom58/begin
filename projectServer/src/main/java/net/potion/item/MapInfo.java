package net.potion.item;

import net.potion.entity.player.EntityPlayer;

public class MapInfo {
    public final EntityPlayer entityPlayer;
    private final MapData mapData;
    public int[] field_28119_b;
    public int[] field_28125_c;
    private int field_28123_e;
    private int field_28122_f;
    private byte[] field_28121_g;

    public MapInfo(MapData mapData, EntityPlayer entityPlayer) {
        this.mapData = mapData;
        this.field_28119_b = new int[128];
        this.field_28125_c = new int[128];
        this.field_28123_e = 0;
        this.field_28122_f = 0;
        this.entityPlayer = entityPlayer;

        for (int i = 0; i < this.field_28119_b.length; ++i) {
            this.field_28119_b[i] = 0;
            this.field_28125_c[i] = 127;
        }

    }

    public byte[] func_28118_a(ItemStack itemStack) {
        if (--this.field_28122_f < 0) {
            this.field_28122_f = 4;
            byte[] var2 = new byte[this.mapData.mapCoordList.size() * 3 + 1];
            var2[0] = 1;

            for (int var3 = 0; var3 < this.mapData.mapCoordList.size(); ++var3) {
                MapCoord mapCoord = this.mapData.mapCoordList.get(var3);
                var2[var3 * 3 + 1] = (byte) (mapCoord.field_28202_a + (mapCoord.field_28204_d & 15) * 16);
                var2[var3 * 3 + 2] = mapCoord.field_28201_b;
                var2[var3 * 3 + 3] = mapCoord.field_28205_c;
            }

            boolean var9 = true;
            if (this.field_28121_g != null && this.field_28121_g.length == var2.length) {
                for (int var11 = 0; var11 < var2.length; ++var11) {
                    if (var2[var11] != this.field_28121_g[var11]) {
                        var9 = false;
                        break;
                    }
                }
            } else {
                var9 = false;
            }

            if (!var9) {
                this.field_28121_g = var2;
                return var2;
            }
        }

        for (int var8 = 0; var8 < 10; ++var8) {
            int var10 = this.field_28123_e * 11 % 128;
            ++this.field_28123_e;
            if (this.field_28119_b[var10] >= 0) {
                int var12 = this.field_28125_c[var10] - this.field_28119_b[var10] + 1;
                int var5 = this.field_28119_b[var10];
                byte[] var6 = new byte[var12 + 3];
                var6[0] = 0;
                var6[1] = (byte) var10;
                var6[2] = (byte) var5;

                for (int var7 = 0; var7 < var6.length - 3; ++var7) {
                    var6[var7 + 3] = this.mapData.colors[(var7 + var5) * 128 + var10];
                }

                this.field_28125_c[var10] = -1;
                this.field_28119_b[var10] = -1;
                return var6;
            }
        }

        return null;
    }
}
