package net.hypnosis.util.math;

import net.hypnosis.util.math.Direction.Axis;

public enum AxisCycleDirection {
    NONE {
        @Override
        public int choose(int x, int y, int z, Axis axis) {
            return axis.choose(x, y, z);
        }

        @Override
        public double choose(double x, double y, double z, Axis axis) {
            return axis.choose(x, y, z);
        }

        @Override
        public Axis cycle(Axis axis) {
            return axis;
        }

        @Override
        public AxisCycleDirection opposite() {
            return this;
        }
    },
    FORWARD {
        @Override
        public int choose(int x, int y, int z, Axis axis) {
            return axis.choose(z, x, y);
        }

        @Override
        public double choose(double x, double y, double z, Axis axis) {
            return axis.choose(z, x, y);
        }

        @Override
        public Axis cycle(Axis axis) {
            return AXES[Math.floorMod(axis.ordinal() + 1, 3)];
        }

        @Override
        public AxisCycleDirection opposite() {
            return BACKWARD;
        }
    },
    BACKWARD {
        @Override
        public int choose(int x, int y, int z, Axis axis) {
            return axis.choose(y, z, x);
        }

        @Override
        public double choose(double x, double y, double z, Axis axis) {
            return axis.choose(y, z, x);
        }

        @Override
        public Axis cycle(Axis axis) {
            return AXES[Math.floorMod(axis.ordinal() - 1, 3)];
        }

        @Override
        public AxisCycleDirection opposite() {
            return FORWARD;
        }
    };

    public static final Axis[] AXES = Axis.values();
    public static final AxisCycleDirection[] VALUES = values();

    AxisCycleDirection() {
    }

    public abstract int choose(int x, int y, int z, Axis axis);

    public abstract double choose(double x, double y, double z, Axis axis);

    public abstract Axis cycle(Axis axis);

    public abstract AxisCycleDirection opposite();

    public static AxisCycleDirection between(Axis from, Axis to) {
        return VALUES[Math.floorMod(to.ordinal() - from.ordinal(), 3)];
    }
}
