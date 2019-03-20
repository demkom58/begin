package net.minecraft;

import com.demkom58.timings.MinecraftTimings;
import com.demkom58.timings.Timing;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public class TileEntity {
    private static Map<String, Class<? extends TileEntity>> nameToClassMap = new HashMap<>();
    private static Map<Class<? extends TileEntity>, String> classToNameMap = new HashMap<>();

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

    public Timing tickTimer = MinecraftTimings.getTileEntityTimings(this);
    public World worldObj;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    protected boolean tileEntityInvalid;

    private static void addMapping(Class<? extends TileEntity> clazz, String name) {
        if (classToNameMap.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate id: " + name);
        } else {
            nameToClassMap.put(name, clazz);
            classToNameMap.put(clazz, name);
        }
    }

    @SuppressWarnings("unchecked")
    public static TileEntity createAndLoadEntity(NBTTagCompound compound) {
        TileEntity tileEntity = null;

        try {
            Class clazz = nameToClassMap.get(compound.getString("id"));
            if (clazz != null) {
                tileEntity = (TileEntity) clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tileEntity != null) {
            tileEntity.readFromNBT(compound);
        } else {
            System.out.println("Skipping TileEntity with id " + compound.getString("id"));
        }

        return tileEntity;
    }

    public void readFromNBT(NBTTagCompound compound) {
        this.xCoord = compound.getInteger("x");
        this.yCoord = compound.getInteger("y");
        this.zCoord = compound.getInteger("z");
    }

    public void writeToNBT(NBTTagCompound compound) {
        String name = classToNameMap.get(this.getClass());
        if (name == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }

        compound.setString("id", name);
        compound.setInteger("x", this.xCoord);
        compound.setInteger("y", this.yCoord);
        compound.setInteger("z", this.zCoord);
    }

    public void updateEntity() {
    }

    public int func_31005_e() {
        return this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
    }

    public void onInventoryChanged() {
        if (this.worldObj != null) {
            this.worldObj.updateTileEntityChunkAndDoNothing(this.xCoord, this.yCoord, this.zCoord, this);
        }

    }

    public Packet getDescriptionPacket() {
        return null;
    }

    public boolean isInvalid() {
        return this.tileEntityInvalid;
    }

    public void invalidate() {
        this.tileEntityInvalid = true;
    }

    public void validate() {
        this.tileEntityInvalid = false;
    }

}
