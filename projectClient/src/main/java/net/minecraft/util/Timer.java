package net.minecraft.util;

public class Timer {
    public float ticksPerSecond;
    public int elapsedTicks;
    public float renderPartialTicks;
    public float timerSpeed = 1.0F;
    public float elapsedPartialTicks = 0.0F;
    private double lastHRTime;
    private long lastSyncSysClock;
    private long lastSyncHRClock;
    private long accumulated;
    private double timeSyncAdjustment = 1.0D;

    public Timer(float ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
        this.lastSyncSysClock = System.currentTimeMillis();
        this.lastSyncHRClock = System.nanoTime() / 1_000_000L;
    }

    public void updateTimer() {
        long nowSys = System.currentTimeMillis();
        long diff = nowSys - this.lastSyncSysClock;
        long nowHR = System.nanoTime() / 1_000_000L;

        double nowHRSec = (double) nowHR / 1_000.0D;
        if (diff < 0L || diff > 1000L) {
            this.lastHRTime = nowHRSec;
        } else {
            this.accumulated += diff;
            if (this.accumulated > 1_000L) {
                long var9 = nowHR - this.lastSyncHRClock;
                double var11 = (double) this.accumulated / (double) var9;
                this.timeSyncAdjustment += (var11 - this.timeSyncAdjustment) * 0.20000000298023224D;
                this.lastSyncHRClock = nowHR;
                this.accumulated = 0L;
            }

            if (this.accumulated < 0L) {
                this.lastSyncHRClock = nowHR;
            }
        }

        this.lastSyncSysClock = nowSys;
        double var13 = (nowHRSec - this.lastHRTime) * this.timeSyncAdjustment;
        this.lastHRTime = nowHRSec;
        if (var13 < 0.0D) {
            var13 = 0.0D;
        }

        if (var13 > 1.0D) {
            var13 = 1.0D;
        }

        this.elapsedPartialTicks = (float) ((double) this.elapsedPartialTicks + var13 * (double) this.timerSpeed * (double) this.ticksPerSecond);
        this.elapsedTicks = (int) this.elapsedPartialTicks;
        this.elapsedPartialTicks -= (float) this.elapsedTicks;
        if (this.elapsedTicks > 10) {
            this.elapsedTicks = 10;
        }

        this.renderPartialTicks = this.elapsedPartialTicks;
    }
}
