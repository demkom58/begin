package net.hypnosis.util.math;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.hypnosis.entity.Rotatable;
import net.hypnosis.util.block.BlockPos;
import net.hypnosis.util.Collects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Direction {
    DOWN(0, 1, -1, "down", Direction.AxisDirection.NEGATIVE, Direction.Axis.Y, new Vec3i(0, -1, 0)),
    UP(1, 0, -1, "up", Direction.AxisDirection.POSITIVE, Direction.Axis.Y, new Vec3i(0, 1, 0)),
    NORTH(2, 3, 2, "north", Direction.AxisDirection.NEGATIVE, Direction.Axis.Z, new Vec3i(0, 0, -1)),
    SOUTH(3, 2, 0, "south", Direction.AxisDirection.POSITIVE, Direction.Axis.Z, new Vec3i(0, 0, 1)),
    WEST(4, 5, 1, "west", Direction.AxisDirection.NEGATIVE, Direction.Axis.X, new Vec3i(-1, 0, 0)),
    EAST(5, 4, 3, "east", Direction.AxisDirection.POSITIVE, Direction.Axis.X, new Vec3i(1, 0, 0));

    private static final Direction[] ALL = values();
    private static final Map<String, Direction> NAME_MAP = Arrays.stream(ALL).collect(Collectors.toMap(Direction::getName, (direction) -> direction));
    private static final Direction[] VALUES = Arrays.stream(ALL).sorted(Comparator.comparingInt((direction) -> direction.id)).toArray(Direction[]::new);
    private static final Direction[] HORIZONTAL = Arrays.stream(ALL).filter((direction) -> direction.getAxis().isHorizontal()).sorted(Comparator.comparingInt((direction) -> direction.idHorizontal)).toArray(Direction[]::new);
    private static final Long2ObjectMap<Direction> VECTOR_TO_DIRECTION = Arrays.stream(ALL).collect(Collectors.toMap((direction) -> (new BlockPos(direction.getVector())).asLong(), (direction) -> direction, (direction1, direction2) -> {
        throw new IllegalArgumentException("Duplicate keys");
    }, Long2ObjectOpenHashMap::new));


    private final int id;
    private final int idOpposite;
    private final int idHorizontal;

    private final String name;
    private final Direction.Axis axis;
    private final Direction.AxisDirection direction;

    private final Vec3i vector;

    Direction(int id, int idOpposite, int idHorizontal, String name, Direction.AxisDirection direction, Direction.Axis axis, Vec3i vector) {
        this.id = id;
        this.idHorizontal = idHorizontal;
        this.idOpposite = idOpposite;
        this.name = name;
        this.axis = axis;
        this.direction = direction;
        this.vector = vector;
    }

    public static Direction[] getEntityFacingOrder(Rotatable rotatable) {
        float f = rotatable.getPitch(1.0F) * MathConstants.RADIANS_PER_DEGREE;
        float g = -rotatable.getYaw(1.0F) * MathConstants.RADIANS_PER_DEGREE;
        float h = MathHelper.sin(f);
        float i = MathHelper.cos(f);
        float j = MathHelper.sin(g);
        float k = MathHelper.cos(g);
        boolean bl = j > 0.0F;
        boolean bl2 = h < 0.0F;
        boolean bl3 = k > 0.0F;
        float l = bl ? j : -j;
        float m = bl2 ? -h : h;
        float n = bl3 ? k : -k;
        float o = l * i;
        float p = n * i;
        Direction direction = bl ? EAST : WEST;
        Direction direction2 = bl2 ? UP : DOWN;
        Direction direction3 = bl3 ? SOUTH : NORTH;
        if (l > n) {
            if (m > o) {
                return listClosest(direction2, direction, direction3);
            } else {
                return p > m ? listClosest(direction, direction3, direction2) : listClosest(direction, direction2, direction3);
            }
        } else if (m > p) {
            return listClosest(direction2, direction3, direction);
        } else {
            return o > m ? listClosest(direction3, direction, direction2) : listClosest(direction3, direction2, direction);
        }
    }

    private static Direction[] listClosest(Direction first, Direction second, Direction third) {
        return new Direction[]{first, second, third, third.getOpposite(), second.getOpposite(), first.getOpposite()};
    }

    public static Direction transform(Matrix4f matrix, Direction direction) {
        Vec3i vec3i = direction.getVector();
        Vec4f vec4f = new Vec4f(vec3i.getX(), vec3i.getY(), vec3i.getZ(), 0.0F);
        vec4f.transform(matrix);
        return getFacing(vec4f.getX(), vec4f.getY(), vec4f.getZ());
    }

    public Quaternion getRotationQuaternion() {
        final Quaternion quaternion = Vec3f.POSITIVE_X.getDegreesQuaternion(90.0F);
        Quaternion result;

        switch (this) {
            case DOWN -> result = Vec3f.POSITIVE_X.getDegreesQuaternion(180.0F);
            case UP -> result = Quaternion.IDENTITY.copy();
            case NORTH -> {
                quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
                result = quaternion;
            }
            case SOUTH -> result = quaternion;
            case WEST -> {
                quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
                result = quaternion;
            }
            case EAST -> {
                quaternion.hamiltonProduct(Vec3f.POSITIVE_Z.getDegreesQuaternion(-90.0F));
                result = quaternion;
            }
            default -> throw new IncompatibleClassChangeError();
        }

        return result;
    }

    public int getId() {
        return this.id;
    }

    public int getHorizontal() {
        return this.idHorizontal;
    }

    public Direction.AxisDirection getDirection() {
        return this.direction;
    }

    public static Direction getLookDirectionForAxis(Rotatable entity, Direction.Axis axis) {
        return switch (axis) {
            case X -> EAST.pointsTo(entity.getYaw(1.0F)) ? EAST : WEST;
            case Z -> SOUTH.pointsTo(entity.getYaw(1.0F)) ? SOUTH : NORTH;
            case Y -> entity.getPitch(1.0F) < 0.0F ? UP : DOWN;
        };
    }

    public Direction getOpposite() {
        return byId(this.idOpposite);
    }

    public Direction rotateClockwise(Direction.Axis axis) {
        return switch (axis) {
            case X -> this != WEST && this != EAST ? this.rotateXClockwise() : this;
            case Z -> this != NORTH && this != SOUTH ? this.rotateZClockwise() : this;
            case Y -> this != UP && this != DOWN ? this.rotateYClockwise() : this;
        };
    }

    public Direction rotateCounterclockwise(Direction.Axis axis) {

        return switch (axis) {
            case X -> this != WEST && this != EAST ? this.rotateXCounterclockwise() : this;
            case Z -> this != NORTH && this != SOUTH ? this.rotateZCounterclockwise() : this;
            case Y -> this != UP && this != DOWN ? this.rotateYCounterclockwise() : this;
        };
    }

    public Direction rotateYClockwise() {
        return switch (this) {
            case NORTH -> EAST;
            case SOUTH -> WEST;
            case WEST -> NORTH;
            case EAST -> SOUTH;
            default -> throw new IllegalStateException("Unable to get Y-rotated facing of " + this);
        };
    }

    private Direction rotateXClockwise() {
        return switch (this) {
            case DOWN -> SOUTH;
            case UP -> NORTH;
            case NORTH -> DOWN;
            case SOUTH -> UP;
            default -> throw new IllegalStateException("Unable to get X-rotated facing of " + this);
        };
    }

    private Direction rotateXCounterclockwise() {
        return switch (this) {
            case DOWN -> NORTH;
            case UP -> SOUTH;
            case NORTH -> UP;
            case SOUTH -> DOWN;
            default -> throw new IllegalStateException("Unable to get X-rotated facing of " + this);
        };
    }

    private Direction rotateZClockwise() {
        return switch (this) {
            case DOWN -> WEST;
            case UP -> EAST;
            case WEST -> UP;
            case EAST -> DOWN;
            default -> throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
        };
    }

    private Direction rotateZCounterclockwise() {
        return switch (this) {
            case DOWN -> EAST;
            case UP -> WEST;
            case WEST -> DOWN;
            case EAST -> UP;
            default -> throw new IllegalStateException("Unable to get Z-rotated facing of " + this);
        };
    }

    public Direction rotateYCounterclockwise() {
        return switch (this) {
            case NORTH -> WEST;
            case SOUTH -> EAST;
            case WEST -> SOUTH;
            case EAST -> NORTH;
            default -> throw new IllegalStateException("Unable to get CCW facing of " + this);
        };
    }

    public int getOffsetX() {
        return this.vector.getX();
    }

    public int getOffsetY() {
        return this.vector.getY();
    }

    public int getOffsetZ() {
        return this.vector.getZ();
    }

    public Vec3f getUnitVector() {
        return new Vec3f((float) this.getOffsetX(), (float) this.getOffsetY(), (float) this.getOffsetZ());
    }

    public String getName() {
        return this.name;
    }

    public Direction.Axis getAxis() {
        return this.axis;
    }

    @Nullable
    public static Direction byName(@Nullable String name) {
        return name == null ? null : NAME_MAP.get(name.toLowerCase(Locale.ROOT));
    }

    public static Direction byId(int id) {
        return VALUES[MathHelper.abs(id % VALUES.length)];
    }

    public static Direction fromHorizontal(int value) {
        return HORIZONTAL[MathHelper.abs(value % HORIZONTAL.length)];
    }

    @Nullable
    public static Direction fromVector(BlockPos pos) {
        return VECTOR_TO_DIRECTION.get(pos.asLong());
    }

    @Nullable
    public static Direction fromVector(int x, int y, int z) {
        return VECTOR_TO_DIRECTION.get(BlockPos.asLong(x, y, z));
    }

    public static Direction fromRotation(double rotation) {
        return fromHorizontal(MathHelper.floor(rotation / 90.0D + 0.5D) & 3);
    }

    public static Direction from(Direction.Axis axis, Direction.AxisDirection direction) {
        return switch (axis) {
            case X -> direction == AxisDirection.POSITIVE ? EAST : WEST;
            case Z -> direction == AxisDirection.POSITIVE ? SOUTH : NORTH;
            case Y -> direction == AxisDirection.POSITIVE ? UP : DOWN;
        };
    }

    public float asRotation() {
        return (float) ((this.idHorizontal & 3) * 90);
    }

    public static Direction random(Random random) {
        return Collects.getRandom(ALL, random);
    }

    public static Direction getFacing(double x, double y, double z) {
        return getFacing((float) x, (float) y, (float) z);
    }

    public static Direction getFacing(float x, float y, float z) {
        Direction direction = NORTH;
        float f = 1.4E-45F;

        Direction[] directions = ALL;
        for (int i = 0; i < directions.length; ++i) {
            final Direction dir = directions[i];
            float g = x * (float) dir.vector.getX()
                    + y * (float) dir.vector.getY()
                    + z * (float) dir.vector.getZ();

            if (g > f) {
                f = g;
                direction = dir;
            }
        }

        return direction;
    }

    public String toString() {
        return this.name;
    }

    public String asString() {
        return this.name;
    }

    private static boolean isVertical(Direction direction) {
        return direction.getAxis().isVertical();
    }

    public static Direction get(Direction.AxisDirection direction, Direction.Axis axis) {
        final Direction[] directions = ALL;
        for (int i = 0; i < directions.length; ++i) {
            Direction dir = directions[i];
            if (dir.getDirection() == direction && dir.getAxis() == axis) {
                return dir;
            }
        }

        throw new IllegalArgumentException("No such direction: " + direction + " " + axis);
    }

    public Vec3i getVector() {
        return this.vector;
    }

    public boolean pointsTo(float yaw) {
        float f = yaw * MathConstants.RADIANS_PER_DEGREE;
        float g = -MathHelper.sin(f);
        float h = MathHelper.cos(f);
        return (float) this.vector.getX() * g + (float) this.vector.getZ() * h > 0.0F;
    }

    public enum Axis implements Predicate<Direction> {
        X("x") {
            @Override
            public int choose(int x, int y, int z) {
                return x;
            }

            @Override
            public double choose(double x, double y, double z) {
                return x;
            }
        },
        Y("y") {
            @Override
            public int choose(int x, int y, int z) {
                return y;
            }

            @Override
            public double choose(double x, double y, double z) {
                return y;
            }
        },
        Z("z") {
            @Override
            public int choose(int x, int y, int z) {
                return z;
            }

            @Override
            public double choose(double x, double y, double z) {
                return z;
            }
        };

        public static final Direction.Axis[] VALUES = values();
        private static final Map<String, Direction.Axis> BY_NAME = Arrays.stream(VALUES).collect(Collectors.toMap(Axis::getName, (axis) -> axis));
        private final String name;

        Axis(String name) {
            this.name = name;
        }

        @Nullable
        public static Direction.Axis fromName(String name) {
            return BY_NAME.get(name.toLowerCase(Locale.ROOT));
        }

        public String getName() {
            return this.name;
        }

        public boolean isVertical() {
            return this == Y;
        }

        public boolean isHorizontal() {
            return this == X || this == Z;
        }

        public String toString() {
            return this.name;
        }

        public static Direction.Axis pickRandomAxis(Random random) {
            return Collects.getRandom(VALUES, random);
        }

        @Override
        public boolean test(@Nullable Direction direction) {
            return direction != null && direction.getAxis() == this;
        }

        public Direction.Type getType() {
            return switch (this) {
                case X, Z -> Type.HORIZONTAL;
                case Y -> Type.VERTICAL;
            };
        }

        public String asString() {
            return this.name;
        }

        public abstract int choose(int x, int y, int z);

        public abstract double choose(double x, double y, double z);
    }

    public enum AxisDirection {
        POSITIVE(1, "Towards positive"),
        NEGATIVE(-1, "Towards negative");

        private final int offset;
        private final String description;

        AxisDirection(int offset, String description) {
            this.offset = offset;
            this.description = description;
        }

        public int offset() {
            return this.offset;
        }

        public String getDescription() {
            return this.description;
        }

        public String toString() {
            return this.description;
        }

        public Direction.AxisDirection getOpposite() {
            return this == POSITIVE ? NEGATIVE : POSITIVE;
        }
    }

    public enum Type implements Iterable<Direction>, Predicate<Direction> {
        HORIZONTAL(new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}, new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z}),
        VERTICAL(new Direction[]{Direction.UP, Direction.DOWN}, new Direction.Axis[]{Direction.Axis.Y});

        private final Direction[] facingArray;
        private final Direction.Axis[] axisArray;

        Type(Direction[] facingArray, Direction.Axis[] axisArray) {
            this.facingArray = facingArray;
            this.axisArray = axisArray;
        }

        public Direction random(Random random) {
            return Collects.getRandom(this.facingArray, random);
        }

        public Direction.Axis randomAxis(Random random) {
            return Collects.getRandom(this.axisArray, random);
        }

        @Override
        public boolean test(@Nullable Direction direction) {
            return direction != null && direction.getAxis().getType() == this;
        }

        @Override
        public @NotNull Iterator<Direction> iterator() {
            return Arrays.stream(this.facingArray).iterator();
        }

        public Stream<Direction> stream() {
            return Arrays.stream(this.facingArray);
        }
    }
}
