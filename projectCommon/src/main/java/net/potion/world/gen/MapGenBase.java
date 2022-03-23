package net.potion.world.gen;

import net.potion.world.World;
import net.potion.world.chunk.IChunkProvider;

import java.util.Random;

public class MapGenBase {
    protected int range = 8;
    protected Random rand = new Random();

    public void generate(IChunkProvider provider, World world, int centerX, int centerZ, byte[] chunk) {
        int range = this.range;

        this.rand.setSeed(world.getRandomSeed());
        long randX = this.rand.nextLong() / 2L * 2L + 1L;
        long randZ = this.rand.nextLong() / 2L * 2L + 1L;

        for (int x = centerX - range; x <= centerX + range; ++x) {
            for (int z = centerZ - range; z <= centerZ + range; ++z) {
                this.rand.setSeed((long) x * randX + (long) z * randZ ^ world.getRandomSeed());
                this.recursiveGenerate(world, x, z, centerX, centerZ, chunk);
            }
        }

    }

    protected void recursiveGenerate(World world, int x, int z, int centerX, int centerZ, byte[] chunk) {
    }
}
