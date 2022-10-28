package net.potion.world.gen.struct;

import net.potion.world.World;

import java.util.Random;

public abstract class WorldGenerator {
    public abstract boolean generate(World world, Random random, int x, int y, int z);

    public void setScale(double scaleX, double scaleY, double scaleZ) {
    }
}
