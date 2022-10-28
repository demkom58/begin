package net.hypnosis.util.block;


import com.google.common.base.MoreObjects;

import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

import net.hypnosis.util.math.Direction;
import net.hypnosis.util.math.Vec3i;

public class BlockBox {
    private static final Logger LOGGER = Logger.getLogger(BlockBox.class.getName());

    private int minX;
    private int minY;
    private int minZ;
    private int maxX;
    private int maxY;
    private int maxZ;

    public BlockBox(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockBox(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        if (maxX < minX || maxY < minY || maxZ < minZ) {
            LOGGER.severe("Invalid bounding box data, inverted bounds for: " + this);
            this.minX = Math.min(minX, maxX);
            this.minY = Math.min(minY, maxY);
            this.minZ = Math.min(minZ, maxZ);
            this.maxX = Math.max(minX, maxX);
            this.maxY = Math.max(minY, maxY);
            this.maxZ = Math.max(minZ, maxZ);
        }

    }

    public static BlockBox create(Vec3i first, Vec3i second) {
        return new BlockBox(Math.min(first.getX(), second.getX()), Math.min(first.getY(), second.getY()), Math.min(first.getZ(), second.getZ()), Math.max(first.getX(), second.getX()), Math.max(first.getY(), second.getY()), Math.max(first.getZ(), second.getZ()));
    }

    public static BlockBox infinite() {
        return new BlockBox(-2147483648, -2147483648, -2147483648, 2147483647, 2147483647, 2147483647);
    }

    public static BlockBox rotated(int x, int y, int z, int offsetX, int offsetY, int offsetZ, int sizeX, int sizeY, int sizeZ, Direction facing) {
        return switch (facing) {
            case NORTH -> new BlockBox(x + offsetX, y + offsetY, z - sizeZ + 1 + offsetZ, x + sizeX - 1 + offsetX, y + sizeY - 1 + offsetY, z + offsetZ);
            case WEST -> new BlockBox(x - sizeZ + 1 + offsetZ, y + offsetY, z + offsetX, x + offsetZ, y + sizeY - 1 + offsetY, z + sizeX - 1 + offsetX);
            case EAST -> new BlockBox(x + offsetZ, y + offsetY, z + offsetX, x + sizeZ - 1 + offsetZ, y + sizeY - 1 + offsetY, z + sizeX - 1 + offsetX);
            default -> new BlockBox(x + offsetX, y + offsetY, z + offsetZ, x + sizeX - 1 + offsetX, y + sizeY - 1 + offsetY, z + sizeZ - 1 + offsetZ);
        };
    }

    public boolean intersects(BlockBox other) {
        return this.maxX >= other.minX && this.minX <= other.maxX && this.maxZ >= other.minZ && this.minZ <= other.maxZ && this.maxY >= other.minY && this.minY <= other.maxY;
    }

    public boolean intersectsXZ(int minX, int minZ, int maxX, int maxZ) {
        return this.maxX >= minX && this.minX <= maxX && this.maxZ >= minZ && this.minZ <= maxZ;
    }

    public static Optional<BlockBox> encompassPositions(Iterable<BlockPos> positions) {
        Iterator<BlockPos> iterator = positions.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }

        BlockBox blockBox = new BlockBox(iterator.next());
        Objects.requireNonNull(blockBox);
        iterator.forEachRemaining(blockBox::encompass);
        return Optional.of(blockBox);
    }

    public static Optional<BlockBox> encompass(Iterable<BlockBox> boxes) {
        Iterator<BlockBox> iterator = boxes.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }

        BlockBox blockBox = iterator.next();
        BlockBox blockBox2 = new BlockBox(blockBox.minX, blockBox.minY, blockBox.minZ, blockBox.maxX, blockBox.maxY, blockBox.maxZ);
        Objects.requireNonNull(blockBox2);
        iterator.forEachRemaining(blockBox2::encompass);
        return Optional.of(blockBox2);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public BlockBox encompass(BlockBox box) {
        this.minX = Math.min(this.minX, box.minX);
        this.minY = Math.min(this.minY, box.minY);
        this.minZ = Math.min(this.minZ, box.minZ);
        this.maxX = Math.max(this.maxX, box.maxX);
        this.maxY = Math.max(this.maxY, box.maxY);
        this.maxZ = Math.max(this.maxZ, box.maxZ);
        return this;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public BlockBox encompass(BlockPos pos) {
        this.minX = Math.min(this.minX, pos.getX());
        this.minY = Math.min(this.minY, pos.getY());
        this.minZ = Math.min(this.minZ, pos.getZ());
        this.maxX = Math.max(this.maxX, pos.getX());
        this.maxY = Math.max(this.maxY, pos.getY());
        this.maxZ = Math.max(this.maxZ, pos.getZ());
        return this;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public BlockBox move(int dx, int dy, int dz) {
        this.minX += dx;
        this.minY += dy;
        this.minZ += dz;
        this.maxX += dx;
        this.maxY += dy;
        this.maxZ += dz;
        return this;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public BlockBox move(Vec3i vec) {
        return this.move(vec.getX(), vec.getY(), vec.getZ());
    }

    public BlockBox offset(int x, int y, int z) {
        return new BlockBox(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public BlockBox expand(int offset) {
        return new BlockBox(this.getMinX() - offset, this.getMinY() - offset, this.getMinZ() - offset, this.getMaxX() + offset, this.getMaxY() + offset, this.getMaxZ() + offset);
    }

    public boolean contains(Vec3i vec) {
        return vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ && vec.getY() >= this.minY && vec.getY() <= this.maxY;
    }

    public Vec3i getDimensions() {
        return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
    }

    public int getBlockCountX() {
        return this.maxX - this.minX + 1;
    }

    public int getBlockCountY() {
        return this.maxY - this.minY + 1;
    }

    public int getBlockCountZ() {
        return this.maxZ - this.minZ + 1;
    }

    public BlockPos getCenter() {
        return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
    }

    public void forEachVertex(Consumer<BlockPos> consumer) {
        BlockPos.Mutable m = new BlockPos.Mutable();
        consumer.accept(m.set(this.maxX, this.maxY, this.maxZ));
        consumer.accept(m.set(this.minX, this.maxY, this.maxZ));
        consumer.accept(m.set(this.maxX, this.minY, this.maxZ));
        consumer.accept(m.set(this.minX, this.minY, this.maxZ));
        consumer.accept(m.set(this.maxX, this.maxY, this.minZ));
        consumer.accept(m.set(this.minX, this.maxY, this.minZ));
        consumer.accept(m.set(this.maxX, this.minY, this.minZ));
        consumer.accept(m.set(this.minX, this.minY, this.minZ));
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("minX", this.minX).add("minY", this.minY).add("minZ", this.minZ).add("maxX", this.maxX).add("maxY", this.maxY).add("maxZ", this.maxZ).toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof BlockBox blockBox)) {
            return false;
        } else {
            return this.minX == blockBox.minX && this.minY == blockBox.minY && this.minZ == blockBox.minZ && this.maxX == blockBox.maxX && this.maxY == blockBox.maxY && this.maxZ == blockBox.maxZ;
        }
    }

    public int hashCode() {
        return Objects.hash(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
    }

    public int getMinX() {
        return this.minX;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMinZ() {
        return this.minZ;
    }

    public int getMaxX() {
        return this.maxX;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getMaxZ() {
        return this.maxZ;
    }

}

