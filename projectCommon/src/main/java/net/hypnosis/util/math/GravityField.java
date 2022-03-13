package net.hypnosis.util.math;


import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

public class GravityField {
    private final List<Point> points = Lists.newArrayList();

    public GravityField() {
    }

    public void addPoint(Vec3i pos, double mass) {
        if (mass != 0.0D) {
            this.points.add(new GravityField.Point(pos, mass));
        }
    }

    public double calculate(Vec3i pos, double mass) {
        if (mass == 0.0D) {
            return 0.0D;
        }

        double d = 0.0D;
        for (Point point : this.points) {
            d += point.getGravityFactor(pos);
        }

        return d * mass;
    }

    private record Point(Vec3i pos, double mass) {

        public double getGravityFactor(Vec3i pos) {
            double d = this.pos.getSquaredDistance(pos);
            return d == 0.0D ? 1.0D / 0.0 : this.mass / Math.sqrt(d);
        }
    }
}
