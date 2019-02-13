package net.minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapData extends MapDataBase {
    public int xCenter;
    public int zCenter;
    public byte dimension;
    public byte scale;
    public byte[] colors = new byte[16384];
    public int field_28159_g;
    public List<MapInfo> mapInfoList = new ArrayList<>();
    public List<MapCoord> mapCoordList = new ArrayList<>();
    private Map<EntityPlayer, MapInfo> playerMapInfoMap = new HashMap<>();

    public MapData(String var1) {
        super(var1);
    }

    public void func_28148_a(NBTTagCompound compound) {
        this.dimension = compound.getByte("dimension");
        this.xCenter = compound.getInteger("xCenter");
        this.zCenter = compound.getInteger("zCenter");
        this.scale = compound.getByte("scale");
        if (this.scale < 0) {
            this.scale = 0;
        }

        if (this.scale > 4) {
            this.scale = 4;
        }

        short width = compound.getShort("width");
        short height = compound.getShort("height");
        if (width == 128 && height == 128) {
            this.colors = compound.getByteArray("colors");
        } else {
            byte[] colors = compound.getByteArray("colors");
            this.colors = new byte[16384];
            int var5 = (128 - width) / 2;
            int var6 = (128 - height) / 2;

            for (int var7 = 0; var7 < height; ++var7) {
                int var8 = var7 + var6;
                if (var8 >= 0 || var8 < 128) {
                    for (int var9 = 0; var9 < width; ++var9) {
                        int var10 = var9 + var5;
                        if (var10 >= 0 || var10 < 128) {
                            this.colors[var10 + var8 * 128] = colors[var9 + var7 * width];
                        }
                    }
                }
            }
        }

    }

    public void func_28147_b(NBTTagCompound compound) {
        compound.setByte("dimension", this.dimension);
        compound.setInteger("xCenter", this.xCenter);
        compound.setInteger("zCenter", this.zCenter);
        compound.setByte("scale", this.scale);
        compound.setShort("width", (short) 128);
        compound.setShort("height", (short) 128);
        compound.setByteArray("colors", this.colors);
    }

    public void func_28155_a(EntityPlayer entityPlayer, ItemStack itemStack) {
        if (!this.playerMapInfoMap.containsKey(entityPlayer)) {
            MapInfo mapInfo = new MapInfo(this, entityPlayer);
            this.playerMapInfoMap.put(entityPlayer, mapInfo);
            this.mapInfoList.add(mapInfo);
        }

        this.mapCoordList.clear();

        for (int i = 0; i < this.mapInfoList.size(); ++i) {
            MapInfo var4 = this.mapInfoList.get(i);
            if (!var4.entityPlayer.isDead && var4.entityPlayer.inventory.func_28010_c(itemStack)) {
                float var5 = (float) (var4.entityPlayer.posX - (double) this.xCenter) / (float) (1 << this.scale);
                float var6 = (float) (var4.entityPlayer.posZ - (double) this.zCenter) / (float) (1 << this.scale);
                byte var7 = 64;
                byte var8 = 64;
                if (var5 >= (float) (-var7) && var6 >= (float) (-var8) && var5 <= (float) var7 && var6 <= (float) var8) {
                    byte var9 = 0;
                    byte var10 = (byte) ((int) ((double) (var5 * 2.0F) + 0.5D));
                    byte var11 = (byte) ((int) ((double) (var6 * 2.0F) + 0.5D));
                    byte var12 = (byte) ((int) ((double) (entityPlayer.rotationYaw * 16.0F / 360.0F) + 0.5D));
                    if (this.dimension < 0) {
                        int var13 = this.field_28159_g / 10;
                        var12 = (byte) (var13 * var13 * 34187121 + var13 * 121 >> 15 & 15);
                    }

                    if (var4.entityPlayer.dimension == this.dimension) {
                        this.mapCoordList.add(new MapCoord(this, var9, var10, var11, var12));
                    }
                }
            } else {
                this.playerMapInfoMap.remove(var4.entityPlayer);
                this.mapInfoList.remove(var4);
            }
        }

    }

    public byte[] func_28154_a(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        MapInfo var4 = this.playerMapInfoMap.get(entityPlayer);
        if (var4 == null) {
            return null;
        }

        return var4.func_28118_a(itemStack);
    }

    public void func_28153_a(int var1, int var2, int var3) {
        super.func_28146_a();

        for (int i = 0; i < this.mapInfoList.size(); ++i) {
            MapInfo mapInfo = this.mapInfoList.get(i);
            if (mapInfo.field_28119_b[var1] < 0 || mapInfo.field_28119_b[var1] > var2) {
                mapInfo.field_28119_b[var1] = var2;
            }

            if (mapInfo.field_28125_c[var1] < 0 || mapInfo.field_28125_c[var1] < var3) {
                mapInfo.field_28125_c[var1] = var3;
            }
        }

    }
}
