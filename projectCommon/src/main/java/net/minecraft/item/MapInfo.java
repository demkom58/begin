package net.minecraft.item;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.minecraft.entity.player.EntityPlayer;

public class MapInfo {
    public final EntityPlayer entityPlayer;
    // $FF: synthetic field
    final MapData mapData;
    public int[] field1;
    public int[] field2;
    private int field3;
    private int field4;
    @Side(CodeSide.SERVER)
    private byte[] field5;

    public MapInfo(MapData var1, EntityPlayer var2) {
        this.mapData = var1;
        this.field1 = new int[128];
        this.field2 = new int[128];
        this.field3 = 0;
        this.field4 = 0;
        this.entityPlayer = var2;

        for (int var3 = 0; var3 < this.field1.length; ++var3) {
            this.field1[var3] = 0;
            this.field2[var3] = 127;
        }

    }

    @Side(CodeSide.SERVER)
    public byte[] method1(ItemStack itemStack) {
        if (--this.field4 < 0) {
            this.field4 = 4;
            byte[] var2 = new byte[this.mapData.mapCoordList.size() * 3 + 1];
            var2[0] = 1;

            for (int var3 = 0; var3 < this.mapData.mapCoordList.size(); ++var3) {
                MapCoord mapCoord = this.mapData.mapCoordList.get(var3);
                var2[var3 * 3 + 1] = (byte) (mapCoord.field1 + (mapCoord.field4 & 15) * 16);
                var2[var3 * 3 + 2] = mapCoord.field2;
                var2[var3 * 3 + 3] = mapCoord.field3;
            }

            boolean var9 = true;
            if (this.field5 != null && this.field5.length == var2.length) {
                for (int var11 = 0; var11 < var2.length; ++var11) {
                    if (var2[var11] != this.field5[var11]) {
                        var9 = false;
                        break;
                    }
                }
            } else {
                var9 = false;
            }

            if (!var9) {
                this.field5 = var2;
                return var2;
            }
        }

        for (int var8 = 0; var8 < 10; ++var8) {
            int var10 = this.field3 * 11 % 128;
            ++this.field3;
            if (this.field1[var10] >= 0) {
                int var12 = this.field2[var10] - this.field1[var10] + 1;
                int var5 = this.field1[var10];
                byte[] var6 = new byte[var12 + 3];
                var6[0] = 0;
                var6[1] = (byte) var10;
                var6[2] = (byte) var5;

                for (int var7 = 0; var7 < var6.length - 3; ++var7) {
                    var6[var7 + 3] = this.mapData.colors[(var7 + var5) * 128 + var10];
                }

                this.field2[var10] = -1;
                this.field1[var10] = -1;
                return var6;
            }
        }

        return null;
    }
}
