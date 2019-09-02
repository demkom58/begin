package net.minecraft.client;

import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Session {
    public static List<Block> registeredBlocksList = new ArrayList<>();

    static {
        registeredBlocksList.add(Block.STONE);
        registeredBlocksList.add(Block.COBBLESTONE);
        registeredBlocksList.add(Block.BRICK);
        registeredBlocksList.add(Block.DIRT);
        registeredBlocksList.add(Block.PLANKS);
        registeredBlocksList.add(Block.WOOD);
        registeredBlocksList.add(Block.LEAVES);
        registeredBlocksList.add(Block.TORCH_WOOD);
        registeredBlocksList.add(Block.STAIR_SINGLE);
        registeredBlocksList.add(Block.GLASS);
        registeredBlocksList.add(Block.COBBLESTONE_MOSSY);
        registeredBlocksList.add(Block.SAPLING);
        registeredBlocksList.add(Block.PLANT_YELLOW);
        registeredBlocksList.add(Block.PLANT_RED);
        registeredBlocksList.add(Block.MUSHROOM_BROWN);
        registeredBlocksList.add(Block.MUSHROOM_RED);
        registeredBlocksList.add(Block.SAND);
        registeredBlocksList.add(Block.GRAVEL);
        registeredBlocksList.add(Block.SPONGE);
        registeredBlocksList.add(Block.CLOTH);
        registeredBlocksList.add(Block.ORE_COAL);
        registeredBlocksList.add(Block.ORE_IRON);
        registeredBlocksList.add(Block.ORE_GOLD);
        registeredBlocksList.add(Block.BLOCK_IRON);
        registeredBlocksList.add(Block.BLOCK_GOLD);
        registeredBlocksList.add(Block.BOOKSHELF);
        registeredBlocksList.add(Block.TNT);
        registeredBlocksList.add(Block.OBSIDIAN);
    }

    public String username;
    public String sessionId;
    public String mpPassParameter;

    public Session(String username, String sessionId) {
        this.username = username;
        this.sessionId = sessionId;
    }
}
