package net.potion.tileentity;

import net.hypnosis.annotations.CodeSide;
import net.hypnosis.annotations.Side;
import net.potion.block.Block;
import net.potion.nbt.TagCompound;
import net.potion.network.packet.Packet;
import net.potion.world.World;

import java.util.HashMap;
import java.util.Map;

public class TileEntity {
    private static final Map<String, Class<? extends TileEntity>> NAME_TO_CLASS_MAP = new HashMap<>();
    private static final Map<Class<? extends TileEntity>, String> CLASS_TO_NAME_MAP = new HashMap<>();

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

    public World world;
    public int xCoord;
    public int yCoord;
    public int zCoord;
    protected boolean tileEntityInvalid;

    private static void addMapping(Class<? extends TileEntity> clazz, String name) {
        if (NAME_TO_CLASS_MAP.containsKey(name)) {
            throw new IllegalArgumentException("Duplicate id: " + name);
        }

        NAME_TO_CLASS_MAP.put(name, clazz);
        CLASS_TO_NAME_MAP.put(clazz, name);
    }

    @SuppressWarnings("unchecked")
    public static TileEntity createAndLoadEntity(TagCompound tag) {
        TileEntity entity = null;

        try {
            Class<? extends TileEntity> clazz = NAME_TO_CLASS_MAP.get(tag.getString("id"));
            if (clazz != null) {
                entity = clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (entity != null) {
            entity.readFromNBT(tag);
        } else {
            System.out.println("Skipping TileEntity with id " + tag.getString("id"));
        }

        return entity;
    }

    public void readFromNBT(TagCompound tag) {
        this.xCoord = tag.getInteger("x");
        this.yCoord = tag.getInteger("y");
        this.zCoord = tag.getInteger("z");
    }

    public void writeToNBT(TagCompound tag) {
        String name = CLASS_TO_NAME_MAP.get(this.getClass());
        if (name == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }

        tag.setString("id", name);
        tag.setInteger("x", this.xCoord);
        tag.setInteger("y", this.yCoord);
        tag.setInteger("z", this.zCoord);
    }

    public void updateEntity() {
    }

    public int getBlockMetadata() {
        return this.world.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
    }

    public void onInventoryChanged() {
        if (this.world != null) {
            this.world.updateTileEntityChunkAndDoNothing(this.xCoord, this.yCoord, this.zCoord, this);
        }

    }

    public double getDistanceFrom(double x, double y, double z) {
        double dX = (double) this.xCoord + 0.5D - x;
        double dY = (double) this.yCoord + 0.5D - y;
        double dZ = (double) this.zCoord + 0.5D - z;
        return dX * dX + dY * dY + dZ * dZ;
    }

    public Block getBlockType() {
        return Block.BLOCKS_LIST[this.world.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
    }

    @Side(CodeSide.SERVER)
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
