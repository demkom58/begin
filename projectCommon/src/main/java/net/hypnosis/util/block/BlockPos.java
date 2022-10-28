package net.hypnosis.util.block;


import com.google.common.collect.AbstractIterator;
import net.hypnosis.util.Validate;
import net.hypnosis.util.math.*;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static net.hypnosis.util.math.Direction.Axis;

@Unmodifiable
public class BlockPos extends Vec3i {
    public static final BlockPos ORIGIN;
    private static final int SIZE_BITS_X;
    private static final int SIZE_BITS_Z;
    public static final int SIZE_BITS_Y;
    private static final long BITS_X;
    private static final long BITS_Y;
    private static final long BITS_Z;
    private static final int field_33083 = 0;
    private static final int BIT_SHIFT_Z;
    private static final int BIT_SHIFT_X;

    public BlockPos(int i, int j, int k) {
        super(i, j, k);
    }

    public BlockPos(double d, double e, double f) {
        super(d, e, f);
    }

    public BlockPos(Vec3d pos) {
        this(pos.x, pos.y, pos.z);
    }

    public BlockPos(Position pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockPos(Vec3i pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }

    public static long offset(long value, Direction direction) {
        return add(value, direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
    }

    public static long add(long value, int x, int y, int z) {
        return asLong(unpackLongX(value) + x, unpackLongY(value) + y, unpackLongZ(value) + z);
    }

    public static int unpackLongX(long packedPos) {
        return (int) (packedPos << 64 - BIT_SHIFT_X - SIZE_BITS_X >> 64 - SIZE_BITS_X);
    }

    public static int unpackLongY(long packedPos) {
        return (int) (packedPos << 64 - SIZE_BITS_Y >> 64 - SIZE_BITS_Y);
    }

    public static int unpackLongZ(long packedPos) {
        return (int) (packedPos << 64 - BIT_SHIFT_Z - SIZE_BITS_Z >> 64 - SIZE_BITS_Z);
    }

    public static BlockPos fromLong(long packedPos) {
        return new BlockPos(unpackLongX(packedPos), unpackLongY(packedPos), unpackLongZ(packedPos));
    }

    public long asLong() {
        return asLong(this.getX(), this.getY(), this.getZ());
    }

    public static long asLong(int x, int y, int z) {
        long l = 0L;
        l |= ((long) x & BITS_X) << BIT_SHIFT_X;
        l |= ((long) y & BITS_Y) << 0;
        l |= ((long) z & BITS_Z) << BIT_SHIFT_Z;
        return l;
    }

    public static long removeChunkSectionLocalY(long y) {
        return y & -16L;
    }

    @Override
    public BlockPos add(double d, double e, double f) {
        return d == 0.0D && e == 0.0D && f == 0.0D ? this : new BlockPos((double) this.getX() + d, (double) this.getY() + e, (double) this.getZ() + f);
    }

    @Override
    public BlockPos add(int i, int j, int k) {
        return i == 0 && j == 0 && k == 0 ? this : new BlockPos(this.getX() + i, this.getY() + j, this.getZ() + k);
    }

    @Override
    public BlockPos add(Vec3i vec3i) {
        return this.add(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    @Override
    public BlockPos subtract(Vec3i vec3i) {
        return this.add(-vec3i.getX(), -vec3i.getY(), -vec3i.getZ());
    }

    @Override
    public BlockPos multiply(int i) {
        if (i == 1) {
            return this;
        } else {
            return i == 0 ? ORIGIN : new BlockPos(this.getX() * i, this.getY() * i, this.getZ() * i);
        }
    }

    @Override
    public BlockPos up() {
        return this.offset(Direction.UP);
    }

    @Override
    public BlockPos up(int distance) {
        return this.offset(Direction.UP, distance);
    }

    @Override
    public BlockPos down() {
        return this.offset(Direction.DOWN);
    }

    @Override
    public BlockPos down(int i) {
        return this.offset(Direction.DOWN, i);
    }

    @Override
    public BlockPos north() {
        return this.offset(Direction.NORTH);
    }

    @Override
    public BlockPos north(int distance) {
        return this.offset(Direction.NORTH, distance);
    }

    @Override
    public BlockPos south() {
        return this.offset(Direction.SOUTH);
    }

    @Override
    public BlockPos south(int distance) {
        return this.offset(Direction.SOUTH, distance);
    }

    @Override
    public BlockPos west() {
        return this.offset(Direction.WEST);
    }

    @Override
    public BlockPos west(int distance) {
        return this.offset(Direction.WEST, distance);
    }

    @Override
    public BlockPos east() {
        return this.offset(Direction.EAST);
    }

    @Override
    public BlockPos east(int distance) {
        return this.offset(Direction.EAST, distance);
    }

    @Override
    public BlockPos offset(Direction direction) {
        return new BlockPos(this.getX() + direction.getOffsetX(), this.getY() + direction.getOffsetY(), this.getZ() + direction.getOffsetZ());
    }

    @Override
    public BlockPos offset(Direction d, int value) {
        return value == 0 ? this : new BlockPos(
                this.getX() + d.getOffsetX() * value,
                this.getY() + d.getOffsetY() * value,
                this.getZ() + d.getOffsetZ() * value
        );
    }

    @Override
    public BlockPos offset(Axis axis, int value) {
        if (value == 0) {
            return this;
        }

        int x = axis == Axis.X ? value : 0;
        int y = axis == Axis.Y ? value : 0;
        int z = axis == Axis.Z ? value : 0;
        return new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    public BlockPos rotate(BlockRotation rotation) {
        return switch (rotation) {
            case NONE -> this;
            case CLOCKWISE_90 -> new BlockPos(-this.getZ(), this.getY(), this.getX());
            case CLOCKWISE_180 -> new BlockPos(-this.getX(), this.getY(), -this.getZ());
            case COUNTERCLOCKWISE_90 -> new BlockPos(this.getZ(), this.getY(), -this.getX());
        };
    }

    @Override
    public BlockPos crossProduct(Vec3i pos) {
        return new BlockPos(this.getY() * pos.getZ() - this.getZ() * pos.getY(), this.getZ() * pos.getX() - this.getX() * pos.getZ(), this.getX() * pos.getY() - this.getY() * pos.getX());
    }

    public BlockPos withY(int y) {
        return new BlockPos(this.getX(), y, this.getZ());
    }

    public BlockPos toImmutable() {
        return this;
    }

    public BlockPos.Mutable mutableCopy() {
        return new BlockPos.Mutable(this.getX(), this.getY(), this.getZ());
    }

    public static Iterable<BlockPos> iterateRandomly(Random random, int count, BlockPos around, int range) {
        return iterateRandomly(random, count, around.getX() - range, around.getY() - range, around.getZ() - range, around.getX() + range, around.getY() + range, around.getZ() + range);
    }

    public static Iterable<BlockPos> iterateRandomly(Random random, int count, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        int dX = maxX - minX + 1;
        int dY = maxY - minY + 1;
        int dZ = maxZ - minZ + 1;
        return () -> new AbstractIterator<>() {
            final Mutable pos = new Mutable();
            int remaining = dX;

            @Override
            protected BlockPos computeNext() {
                if (this.remaining <= 0) {
                    return this.endOfData();
                }

                BlockPos blockPos = this.pos.set(
                        minX + random.nextInt(dX),
                        minY + random.nextInt(dY),
                        minZ + random.nextInt(dZ)
                );
                --this.remaining;
                return blockPos;
            }
        };
    }

    public static Iterable<BlockPos> iterateOutwards(BlockPos center, int rangeX, int rangeY, int rangeZ) {
        final int rangeSum = rangeX + rangeY + rangeZ;
        final int centerX = center.getX();
        final int centerY = center.getY();
        final int centerZ = center.getZ();

        return () -> new AbstractIterator<>() {
            private final Mutable pos = new Mutable();
            private int manhattanDistance;
            private int limitX;
            private int limitY;
            private int dx;
            private int dy;
            private boolean swapZ;

            @Override
            protected BlockPos computeNext() {
                if (this.swapZ) {
                    this.swapZ = false;
                    this.pos.setZ(rangeSum - (this.pos.getZ() - rangeSum));
                    return this.pos;
                }

                Mutable blockPos;
                for (blockPos = null; blockPos == null; ++this.dy) {
                    if (this.dy > this.limitY) {
                        ++this.dx;
                        if (this.dx > this.limitX) {
                            ++this.manhattanDistance;
                            if (this.manhattanDistance > centerX) {
                                return this.endOfData();
                            }

                            this.limitX = Math.min(centerY, this.manhattanDistance);
                            this.dx = -this.limitX;
                        }

                        this.limitY = Math.min(centerZ, this.manhattanDistance - Math.abs(this.dx));
                        this.dy = -this.limitY;
                    }

                    int dX = this.dx;
                    int dY = this.dy;
                    int kX = this.manhattanDistance - Math.abs(dX) - Math.abs(dY);
                    if (kX <= rangeZ) {
                        this.swapZ = kX != 0;
                        blockPos = this.pos.set(centerX + dX, centerY + dY, rangeSum + kX);
                    }
                }

                return blockPos;
            }
        };
    }

    public static Optional<BlockPos> findClosest(BlockPos pos, int horizontalRange, int verticalRange, Predicate<BlockPos> condition) {
        Iterable<BlockPos> outwards = iterateOutwards(pos, horizontalRange, verticalRange, horizontalRange);

        for (BlockPos outward : outwards) {
            if (condition.test(outward)) {
                return Optional.of(outward);
            }
        }

        return Optional.empty();
    }

    public static Stream<BlockPos> streamOutwards(BlockPos center, int maxX, int maxY, int maxZ) {
        return StreamSupport.stream(iterateOutwards(center, maxX, maxY, maxZ).spliterator(), false);
    }

    public static Iterable<BlockPos> iterate(BlockPos start, BlockPos end) {
        return iterate(Math.min(start.getX(), end.getX()), Math.min(start.getY(), end.getY()), Math.min(start.getZ(), end.getZ()), Math.max(start.getX(), end.getX()), Math.max(start.getY(), end.getY()), Math.max(start.getZ(), end.getZ()));
    }

    public static Stream<BlockPos> stream(BlockPos start, BlockPos end) {
        return StreamSupport.stream(iterate(start, end).spliterator(), false);
    }

    public static Stream<BlockPos> stream(BlockBox box) {
        return stream(Math.min(box.getMinX(), box.getMaxX()), Math.min(box.getMinY(), box.getMaxY()), Math.min(box.getMinZ(), box.getMaxZ()), Math.max(box.getMinX(), box.getMaxX()), Math.max(box.getMinY(), box.getMaxY()), Math.max(box.getMinZ(), box.getMaxZ()));
    }

    public static Stream<BlockPos> stream(Box box) {
        return stream(MathHelper.floor(box.minX), MathHelper.floor(box.minY), MathHelper.floor(box.minZ), MathHelper.floor(box.maxX), MathHelper.floor(box.maxY), MathHelper.floor(box.maxZ));
    }

    public static Stream<BlockPos> stream(int startX, int startY, int startZ, int endX, int endY, int endZ) {
        return StreamSupport.stream(iterate(startX, startY, startZ, endX, endY, endZ).spliterator(), false);
    }

    public static Iterable<BlockPos> iterate(int startX, int startY, int startZ, int endX, int endY, int endZ) {
        int i = endX - startX + 1;
        int j = endY - startY + 1;
        int k = endZ - startZ + 1;
        int l = i * j * k;
        return () -> new AbstractIterator<>() {
            private final Mutable pos = new Mutable();
            private int index;

            @Override
            protected BlockPos computeNext() {
                if (this.index == i) {
                    return this.endOfData();
                }

                int ix = this.index % j;
                int jx = this.index / j;
                int kx = jx % k;
                int lx = jx / k;
                ++this.index;
                return this.pos.set(l + ix, startY + kx, startZ + lx);
            }
        };
    }

    public static Iterable<BlockPos.Mutable> iterateInSquare(BlockPos center, int radius, Direction firstDirection, Direction secondDirection) {
        Validate.validateState(firstDirection.getAxis() != secondDirection.getAxis(), "The two directions cannot be on the same axis");
        return () -> new AbstractIterator<>() {
            private final Direction[] directions = new Direction[]{firstDirection, secondDirection, firstDirection.getOpposite(), secondDirection.getOpposite()};
            private final Mutable pos = center.mutableCopy().move(secondDirection);
            private final int maxDirectionChanges = 4 * radius;
            private int directionChangeCount = -1;
            private int maxSteps;
            private int steps;
            private int currentX;
            private int currentY;
            private int currentZ;

            {
                this.currentX = this.pos.getX();
                this.currentY = this.pos.getY();
                this.currentZ = this.pos.getZ();
            }

            @Override
            protected Mutable computeNext() {
                this.pos.set(this.currentX, this.currentY, this.currentZ).move(this.directions[(this.directionChangeCount + 4) % 4]);
                this.currentX = this.pos.getX();
                this.currentY = this.pos.getY();
                this.currentZ = this.pos.getZ();
                if (this.steps >= this.maxSteps) {
                    if (this.directionChangeCount >= this.maxDirectionChanges) {
                        return this.endOfData();
                    }

                    ++this.directionChangeCount;
                    this.steps = 0;
                    this.maxSteps = this.directionChangeCount / 2 + 1;
                }

                ++this.steps;
                return this.pos;
            }
        };
    }

    static {
        ORIGIN = new BlockPos(0, 0, 0);
        SIZE_BITS_X = 1 + MathHelper.floorLog2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
        SIZE_BITS_Z = SIZE_BITS_X;
        SIZE_BITS_Y = 64 - SIZE_BITS_X - SIZE_BITS_Z;
        BITS_X = (1L << SIZE_BITS_X) - 1L;
        BITS_Y = (1L << SIZE_BITS_Y) - 1L;
        BITS_Z = (1L << SIZE_BITS_Z) - 1L;
        BIT_SHIFT_Z = SIZE_BITS_Y;
        BIT_SHIFT_X = SIZE_BITS_Y + SIZE_BITS_Z;
    }

    public static class Mutable extends BlockPos {
        public Mutable() {
            this(0, 0, 0);
        }

        public Mutable(int i, int j, int k) {
            super(i, j, k);
        }

        public Mutable(double d, double e, double f) {
            this(MathHelper.floor(d), MathHelper.floor(e), MathHelper.floor(f));
        }

        @Override
        public BlockPos add(double d, double e, double f) {
            return super.add(d, e, f).toImmutable();
        }

        @Override
        public BlockPos add(int i, int j, int k) {
            return super.add(i, j, k).toImmutable();
        }

        @Override
        public BlockPos multiply(int i) {
            return super.multiply(i).toImmutable();
        }

        @Override
        public BlockPos offset(Direction d, int value) {
            return super.offset(d, value).toImmutable();
        }

        @Override
        public BlockPos offset(Axis axis, int value) {
            return super.offset(axis, value).toImmutable();
        }

        @Override
        public BlockPos rotate(BlockRotation rotation) {
            return super.rotate(rotation).toImmutable();
        }

        public BlockPos.Mutable set(int x, int y, int z) {
            this.setX(x);
            this.setY(y);
            this.setZ(z);
            return this;
        }

        public BlockPos.Mutable set(double x, double y, double z) {
            return this.set(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
        }

        public BlockPos.Mutable set(Vec3i pos) {
            return this.set(pos.getX(), pos.getY(), pos.getZ());
        }

        public BlockPos.Mutable set(long pos) {
            return this.set(unpackLongX(pos), unpackLongY(pos), unpackLongZ(pos));
        }

        public BlockPos.Mutable set(AxisCycleDirection axis, int x, int y, int z) {
            return this.set(axis.choose(x, y, z, Axis.X), axis.choose(x, y, z, Axis.Y), axis.choose(x, y, z, Axis.Z));
        }

        public BlockPos.Mutable set(Vec3i pos, Direction direction) {
            return this.set(pos.getX() + direction.getOffsetX(), pos.getY() + direction.getOffsetY(), pos.getZ() + direction.getOffsetZ());
        }

        public BlockPos.Mutable set(Vec3i pos, int x, int y, int z) {
            return this.set(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
        }

        public BlockPos.Mutable set(Vec3i vec1, Vec3i vec2) {
            return this.set(vec1.getX() + vec2.getX(), vec1.getY() + vec2.getY(), vec1.getZ() + vec2.getZ());
        }

        public BlockPos.Mutable move(Direction direction) {
            return this.move(direction, 1);
        }

        public BlockPos.Mutable move(Direction direction, int distance) {
            return this.set(this.getX() + direction.getOffsetX() * distance, this.getY() + direction.getOffsetY() * distance, this.getZ() + direction.getOffsetZ() * distance);
        }

        public BlockPos.Mutable move(int dx, int dy, int dz) {
            return this.set(this.getX() + dx, this.getY() + dy, this.getZ() + dz);
        }

        public BlockPos.Mutable move(Vec3i vec) {
            return this.set(this.getX() + vec.getX(), this.getY() + vec.getY(), this.getZ() + vec.getZ());
        }

        public BlockPos.Mutable clamp(Axis axis, int min, int max) {
            switch (axis) {
                case X:
                    return this.set(MathHelper.clamp(this.getX(), min, max), this.getY(), this.getZ());
                case Y:
                    return this.set(this.getX(), MathHelper.clamp(this.getY(), min, max), this.getZ());
                case Z:
                    return this.set(this.getX(), this.getY(), MathHelper.clamp(this.getZ(), min, max));
                default:
                    throw new IllegalStateException("Unable to clamp axis " + axis);
            }
        }

        @Override
        public BlockPos.Mutable setX(int i) {
            super.setX(i);
            return this;
        }

        @Override
        public BlockPos.Mutable setY(int i) {
            super.setY(i);
            return this;
        }

        @Override
        public BlockPos.Mutable setZ(int i) {
            super.setZ(i);
            return this;
        }

        @Override
        public BlockPos toImmutable() {
            return new BlockPos(this);
        }
    }
}
