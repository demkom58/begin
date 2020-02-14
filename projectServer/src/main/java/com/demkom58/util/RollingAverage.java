package com.demkom58.util;

import net.potion.server.PotionServer;

public class RollingAverage {
    private final int size;
    private long time;
    private double total;
    private int index = 0;
    private final double[] samples;
    private final long[] times;

    public RollingAverage(int size) {
        this.size = size;
        this.time = size * PotionServer.SEC_IN_NANO;
        this.total = PotionServer.TPS * PotionServer.SEC_IN_NANO * size;
        this.samples = new double[size];
        this.times = new long[size];
        for (int i = 0; i < size; i++) {
            this.samples[i] = PotionServer.TPS;
            this.times[i] = PotionServer.SEC_IN_NANO;
        }
    }

    public void add(double x, long t) {
        time -= times[index];
        total -= samples[index] * times[index];
        samples[index] = x;
        times[index] = t;
        time += t;
        total += x * t;
        if (++index == size) {
            index = 0;
        }
    }

    public double getAverage() {
        return total / time;
    }
}
