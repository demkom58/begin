package com.demkom58.timings;

import co.aikar.util.LoadingIntMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.potion.server.PotionServer;

import java.util.logging.Level;

class TimingHandler implements Timing {

    private static int idPool = 1;
    final int id = idPool++;

    final String name;
    private final boolean verbose;

    private final Int2ObjectOpenHashMap<TimingData> children = new LoadingIntMap<>(TimingData::new);

    final TimingData record;
    private final TimingHandler groupHandler;

    private long start = 0;
    private int timingDepth = 0;
    private boolean added;
    private boolean timed;
    private boolean enabled;
    private TimingHandler parent;

    TimingHandler(TimingIdentifier id) {
        if (id.name.startsWith("##")) {
            verbose = true;
            this.name = id.name.substring(3);
        } else {
            this.name = id.name;
            verbose = false;
        }

        this.record = new TimingData(this.id);
        this.groupHandler = id.groupHandler;

        TimingIdentifier.getGroup(id.group).handlers.add(this);
        checkEnabled();
    }

    final void checkEnabled() {
        enabled = Timings.timingsEnabled && (!verbose || Timings.verboseEnabled);
    }

    void processTick(boolean violated) {
        if (timingDepth != 0 || record.getCurTickCount() == 0) {
            timingDepth = 0;
            start = 0;
            return;
        }

        record.processTick(violated);
        for (TimingData handler : children.values()) {
            handler.processTick(violated);
        }
    }

    @Override
    public Timing startTimingIfSync() {
        if (PotionServer.SERVER.isPrimaryThread()) {
            startTiming();
        }
        return this;
    }

    @Override
    public void stopTimingIfSync() {
        if (PotionServer.SERVER.isPrimaryThread()) {
            stopTiming();
        }
    }

    @Override
    public Timing startTiming() {
        if (enabled && ++timingDepth == 1) {
            start = System.nanoTime();
            parent = TimingsManager.CURRENT;
            TimingsManager.CURRENT = this;
        }
        return this;
    }

    @Override
    public void stopTiming() {
        if (enabled && --timingDepth == 0 && start != 0) {
            if (!PotionServer.SERVER.isPrimaryThread()) {
                PotionServer.LOGGER.log(Level.SEVERE, "stopTiming called async for " + name);
                new Throwable().printStackTrace();
                start = 0;
                return;
            }
            addDiff(System.nanoTime() - start);
            start = 0;
        }
    }

    @Override
    public void abort() {
        if (enabled && timingDepth > 0) {
            start = 0;
        }
    }

    void addDiff(long diff) {
        if (TimingsManager.CURRENT == this) {
            TimingsManager.CURRENT = parent;
            if (parent != null) {
                parent.children.get(id).add(diff);
            }
        }
        record.add(diff);
        if (!added) {
            added = true;
            timed = true;
            TimingsManager.HANDLERS.add(this);
        }
        if (groupHandler != null) {
            groupHandler.addDiff(diff);
            groupHandler.children.get(id).add(diff);
        }
    }

    /**
     * Reset this timer, setting all values to zero.
     *
     * @param full
     */
    void reset(boolean full) {
        record.reset();
        if (full) {
            timed = false;
        }
        start = 0;
        timingDepth = 0;
        added = false;
        children.clear();
        checkEnabled();
    }

    @Override
    public TimingHandler getTimingHandler() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return (this == o);
    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * This is simply for the Closeable interface so it can be used with
     * try-with-resources ()
     */
    @Override
    public void close() {
        stopTimingIfSync();
    }

    public boolean isSpecial() {
        return this == TimingsManager.FULL_SERVER_TICK || this == TimingsManager.TIMINGS_TICK;
    }

    boolean isTimed() {
        return timed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    TimingData[] cloneChildren() {
        final TimingData[] clonedChildren = new TimingData[children.size()];
        int i = 0;
        for (TimingData child : children.values()) {
            clonedChildren[i++] = child.clone();
        }
        return clonedChildren;
    }
}
