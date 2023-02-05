package com.demkom58.timings;

import co.aikar.util.LoadingMap;
import co.aikar.util.MRUMapCache;
import com.google.common.base.Function;
import com.google.common.collect.Sets;
import net.minecraft.material.Material;
import net.minecraft.server.MinecraftServer;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static co.aikar.util.JSONUtil.*;
import static com.demkom58.timings.TimingsManager.FULL_SERVER_TICK;
import static com.demkom58.timings.TimingsManager.MINUTE_REPORTS;

@SuppressWarnings({"SuppressionAnnotation", "Convert2Lambda", "Anonymous2MethodRef"})
public class TimingHistory {
    public static long lastMinuteTime;
    public static long timedTicks;
    public static long playerTicks;
    public static long entityTicks;
    public static long tileEntityTicks;
    public static long activatedEntityTicks;
    private static int worldIdPool = 1;
    static Map<String, Integer> worldMap = LoadingMap.newHashMap((in) -> worldIdPool++);
    private final long endTime;
    private final long startTime;
    private final long totalTicks;
    private final long totalTime; // Represents all time spent running the server this history
    private final MinuteReport[] minuteReports;

    private final TimingHistoryEntry[] entries;
    final Set<Material> tileEntityTypeSet = Sets.newHashSet();
    private final Map<Object, Object> worlds;

    TimingHistory() {
        this.endTime = System.currentTimeMillis() / 1000;
        this.startTime = TimingsManager.historyStart / 1000;
        if (timedTicks % 1200 != 0 || MINUTE_REPORTS.isEmpty()) {
            this.minuteReports = MINUTE_REPORTS.toArray(new MinuteReport[MINUTE_REPORTS.size() + 1]);
            this.minuteReports[this.minuteReports.length - 1] = new MinuteReport();
        } else {
            this.minuteReports = MINUTE_REPORTS.toArray(new MinuteReport[0]);
        }
        long ticks = 0;
        for (MinuteReport mp : this.minuteReports) {
            ticks += mp.ticksRecord.timed;
        }
        this.totalTicks = ticks;
        this.totalTime = FULL_SERVER_TICK.record.getTotalTime();
        this.entries = new TimingHistoryEntry[TimingsManager.HANDLERS.size()];

        int i = 0;
        for (TimingHandler handler : TimingsManager.HANDLERS) {
            entries[i++] = new TimingHistoryEntry(handler);
        }


        // Information about all loaded chunks/entities
        // noinspection unchecked
        this.worlds = toObjectMapper(MinecraftServer.SERVER.worldServers, world -> pair(worldMap.get(world.getWorldInfo().getLevelName()), ""));
    }

    static class RegionData {
        final RegionId regionId;
        @SuppressWarnings("Guava")
        static Function<RegionId, RegionData> LOADER = RegionData::new;

        RegionData(RegionId id) {
            this.regionId = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            RegionData that = (RegionData) o;

            return regionId.equals(that.regionId);

        }

        @Override
        public int hashCode() {
            return regionId.hashCode();
        }

        @SuppressWarnings("unchecked")
        final Map<Material, Counter> tileEntityCounts = MRUMapCache.of(LoadingMap.of(new HashMap<Material, Counter>(), Counter.LOADER));

        static class RegionId {
            final int x, z;
            final long regionId;

            RegionId(int x, int z) {
                this.x = x >> 5 << 5;
                this.z = z >> 5 << 5;
                this.regionId = ((long) (this.x) << 32) + (this.z >> 5 << 5) - Integer.MIN_VALUE;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                RegionId regionId1 = (RegionId) o;

                return regionId == regionId1.regionId;

            }

            @Override
            public int hashCode() {
                return (int) (regionId ^ (regionId >>> 32));
            }
        }
    }

    static void resetTicks(boolean fullReset) {
        if (fullReset) {
            // Non full is simply for 1 minute reports
            timedTicks = 0;
        }
        lastMinuteTime = System.nanoTime();
        playerTicks = 0;
        tileEntityTicks = 0;
        entityTicks = 0;
        activatedEntityTicks = 0;
    }

    Object export() {
        return createObject(
                pair("s", startTime),
                pair("e", endTime),
                pair("tk", totalTicks),
                pair("tm", totalTime),
                pair("w", worlds),
                pair("h", toArrayMapper(entries, entry -> {
                    TimingData record = entry.data;
                    if (!record.hasData())
                        return null;

                    return entry.export();
                })),
                pair("mp", toArrayMapper(minuteReports, MinuteReport::export))
        );
    }

    static class MinuteReport {
        final long time = System.currentTimeMillis() / 1000;

        final TicksRecord ticksRecord = new TicksRecord();
        final PingRecord pingRecord = new PingRecord();
        final TimingData fst = TimingsManager.FULL_SERVER_TICK.minuteData.clone();
        final double tps = 1E9 / (System.nanoTime() - lastMinuteTime) * ticksRecord.timed;
        final double usedMemory = TimingsManager.FULL_SERVER_TICK.avgUsedMemory;
        final double freeMemory = TimingsManager.FULL_SERVER_TICK.avgFreeMemory;
        final double loadAvg = ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage();

        List export() {
            return toArray(
                    time,
                    Math.round(tps * 100D) / 100D,
                    Math.round(pingRecord.avg * 100D) / 100D,
                    fst.export(),
                    toArray(ticksRecord.timed,
                            ticksRecord.player,
                            ticksRecord.entity,
                            ticksRecord.activatedEntity,
                            ticksRecord.tileEntity
                    ),
                    usedMemory,
                    freeMemory,
                    loadAvg
            );
        }
    }

    private static class TicksRecord {
        final long timed;
        final long player;
        final long entity;
        final long tileEntity;
        final long activatedEntity;

        TicksRecord() {
            timed = timedTicks - (TimingsManager.MINUTE_REPORTS.size() * 1200);
            player = playerTicks;
            entity = entityTicks;
            tileEntity = tileEntityTicks;
            activatedEntity = activatedEntityTicks;
        }

    }

    private static class PingRecord {
        final double avg;

        PingRecord() {
            avg = 10;
//            final Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
//            int totalPing = 0;
//            for (Player player : onlinePlayers) {
//                totalPing += player.spigot().getPing();
//            }
//            avg = onlinePlayers.isEmpty() ? 0 : totalPing / onlinePlayers.size();
        }
    }


    private static class Counter {
        private int count = 0;
        @SuppressWarnings({"rawtypes", "SuppressionAnnotation", "Guava"})
        static Function LOADER = new LoadingMap.Feeder<Counter>() {
            @Override
            public Counter apply() {
                return new Counter();
            }
        };

        public int increment() {
            return ++count;
        }

        public int count() {
            return count;
        }
    }
}
