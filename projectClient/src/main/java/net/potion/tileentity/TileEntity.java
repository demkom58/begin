package net.potion.tileentity;

import net.potion.block.Block;
import net.potion.nbt.TagCompound;
import net.potion.world.World;

import java.util.HashMap;
import java.util.Map;

public class TileEntity {
    private static Map nameToClassMap = new HashMap();
    private static Map classToNameMap = new HashMap();

    static {
        addMapping(TileEntityFurnace.class, "Furnace");
        addMapping(TileEntityChest.class, "Chest");
        addMapping(TileEntityRecordPlayer.class, "RecordPlayer");
        addMapping(TileEntityDispenser.class, "Trap");
        addMapping(TileEntitySign.class, "Sign");
        addMapping(TileEntityMobSpawner.class, "MobSpawner");
        addMapping(TileEntityNote.class, "Music");
        addMapping(TileEntityPiston.class, "Piston");
    }

    public World worldObj;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    protected boolean field_31007_h;

    private static void addMapping(Class var0, String var1) {
        if (classToNameMap.containsKey(var1)) {
            throw new IllegalArgumentException("Duplicate id: " + var1);
        } else {
            nameToClassMap.put(var1, var0);
            classToNameMap.put(var0, var1);
        }
    }

    @SuppressWarnings("unchecked")
    public static TileEntity createAndLoadEntity(TagCompound var0) {
        TileEntity var1 = null;

        try {
            Class var2 = (Class) nameToClassMap.get(var0.getString("id"));
            if (var2 != null) {
                var1 = (TileEntity) var2.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (var1 != null) {
            var1.readFromNBT(var0);
        } else {
            System.out.println("Skipping TileEntity with id " + var0.getString("id"));
        }

        return var1;
    }

    public void readFromNBT(TagCompound var1) {
        this.xCoord = var1.getInteger("x");
        this.yCoord = var1.getInteger("y");
        this.zCoord = var1.getInteger("z");
    }

    public void writeToNBT(TagCompound var1) {
        String var2 = (String) classToNameMap.get(this.getClass());
        if (var2 == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            var1.setString("id", var2);
            var1.setInteger("x", this.xCoord);
            var1.setInteger("y", this.yCoord);
            var1.setInteger("z", this.zCoord);
        }
    }

    public void updateEntity() {
    }

    public int getBlockMetadata() {
        return this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
    }

    public void onInventoryChanged() {
        if (this.worldObj != null) {
            this.worldObj.updateTileEntityChunkAndDoNothing(this.xCoord, this.yCoord, this.zCoord, this);
        }

    }

    public double getDistanceFrom(double var1, double var3, double var5) {
        double var7 = (double) this.xCoord + 0.5D - var1;
        double var9 = (double) this.yCoord + 0.5D - var3;
        double var11 = (double) this.zCoord + 0.5D - var5;
        return var7 * var7 + var9 * var9 + var11 * var11;
    }

    public Block getBlockType() {
        return Block.BLOCKS_LIST[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
    }

    public boolean isInvalid() {
        return this.field_31007_h;
    }

    public void invalidate() {
        this.field_31007_h = true;
    }

    public void func_31004_j() {
        this.field_31007_h = false;
    }
}
