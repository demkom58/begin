package com.demkom58.timings;

import co.aikar.util.LoadingMap;
import com.google.common.collect.EvictingQueue;
import net.potion.server.PotionServer;

import java.util.*;
import java.util.logging.Level;

public final class TimingsManager {
    static final Map<TimingIdentifier, TimingHandler> TIMING_MAP =
            Collections.synchronizedMap(LoadingMap.newHashMap(id -> (id.protect ?
                    new UnsafeTimingHandler(id) :
                    new TimingHandler(id)
            ), 256, .5F));
    public static final FullServerTickHandler FULL_SERVER_TICK = new FullServerTickHandler();
    public static final TimingHandler TIMINGS_TICK = Timings.ofSafe("Timings Tick", FULL_SERVER_TICK);
    public static List<String> hiddenConfigs = new ArrayList<>();
    public static boolean privacy = false;

    static final Collection<TimingHandler> HANDLERS = new ArrayDeque<>();
    static final ArrayDeque<TimingHistory.MinuteReport> MINUTE_REPORTS = new ArrayDeque<>();

    @SuppressWarnings("UnstableApiUsage")
    static EvictingQueue<TimingHistory> HISTORY = EvictingQueue.create(12);
    static TimingHandler CURRENT;
    static long timingStart = 0;
    static long historyStart = 0;
    static boolean needsFullReset = false;
    static boolean needsRecheckEnabled = false;

    private TimingsManager() {
    }

    /**
     * Resets all timing data on the next tick
     */
    static void reset() {
        needsFullReset = true;
    }

    /**
     * Ticked every tick by CraftBukkit to count the number of times a timer
     * caused TPS loss.
     */
    static void tick() {
        if (Timings.timingsEnabled) {
            boolean violated = FULL_SERVER_TICK.isViolated();

            for (TimingHandler handler : HANDLERS) {
                if (handler.isSpecial()) {
                    // We manually call this
                    continue;
                }
                handler.processTick(violated);
            }

            TimingHistory.playerTicks += PotionServer.SERVER.configManager.playerEntities.size();
            TimingHistory.timedTicks++;
            // Generate TPS/Ping/Tick reports every minute
        }
    }

    static void stopServer() {
        Timings.timingsEnabled = false;
        recheckEnabled();
    }

    static void recheckEnabled() {
        synchronized (TIMING_MAP) {
            for (TimingHandler timings : TIMING_MAP.values()) {
                timings.checkEnabled();
            }
        }
        needsRecheckEnabled = false;
    }

    static void resetTimings() {
        if (needsFullReset) {
            // Full resets need to re-check every handlers enabled state
            // Timing map can be modified from async so we must sync on it.
            synchronized (TIMING_MAP) {
                for (TimingHandler timings : TIMING_MAP.values()) {
                    timings.reset(true);
                }
            }
            PotionServer.LOGGER.log(Level.INFO, "Timings Reset");
            HISTORY.clear();
            needsFullReset = false;
            needsRecheckEnabled = false;
            timingStart = System.currentTimeMillis();
        } else {
            // Soft resets only need to act on timings that have done something
            // Handlers can only be modified on main thread.
            for (TimingHandler timings : HANDLERS) {
                timings.reset(false);
            }
        }

        HANDLERS.clear();
        MINUTE_REPORTS.clear();

        TimingHistory.resetTicks(true);
        historyStart = System.currentTimeMillis();
    }

    static TimingHandler getHandler(String group, String name, Timing parent, boolean protect) {
        return TIMING_MAP.get(new TimingIdentifier(group, name, parent, protect));
    }

}
