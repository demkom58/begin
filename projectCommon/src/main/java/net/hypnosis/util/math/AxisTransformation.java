package net.hypnosis.util.math;


import net.hypnosis.util.Collects;

import java.util.Arrays;

public enum AxisTransformation {
    P123(0, 1, 2),
    P213(1, 0, 2),
    P132(0, 2, 1),
    P231(1, 2, 0),
    P312(2, 0, 1),
    P321(2, 1, 0);

    private final int[] mappings;
    private final Matrix3f matrix;
    private static final int field_33113 = 3;
    private static final AxisTransformation[][] COMBINATIONS = Collects.make(new AxisTransformation[values().length][values().length], (axisTransformations) -> {
        AxisTransformation[] values = values();
        for (AxisTransformation axisTransformation : values) {
            for (AxisTransformation axisTransformation2 : values) {
                int[] is = new int[field_33113];
                for (int q = 0; q < field_33113; ++q) {
                    is[q] = axisTransformation.mappings[axisTransformation2.mappings[q]];
                }

                AxisTransformation axisTransformation3 = Arrays.stream(values()).filter((axisTransformationx) -> Arrays.equals(axisTransformationx.mappings, is)).findFirst().get();
                axisTransformations[axisTransformation.ordinal()][axisTransformation2.ordinal()] = axisTransformation3;
            }
        }

    });

    AxisTransformation(int xMapping, int yMapping, int zMapping) {
        this.mappings = new int[]{xMapping, yMapping, zMapping};
        this.matrix = new Matrix3f();
        this.matrix.set(0, this.map(0), 1.0F);
        this.matrix.set(1, this.map(1), 1.0F);
        this.matrix.set(2, this.map(2), 1.0F);
    }

    public AxisTransformation prepend(AxisTransformation transformation) {
        return COMBINATIONS[this.ordinal()][transformation.ordinal()];
    }

    public int map(int oldAxis) {
        return this.mappings[oldAxis];
    }

    public Matrix3f getMatrix() {
        return this.matrix;
    }
}
