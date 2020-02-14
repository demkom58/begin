package com.demkom58.timings;

import com.google.common.collect.MapMaker;
import net.potion.entity.Entity;
import net.potion.network.packet.Packet;
import net.potion.tileentity.TileEntity;

import java.util.Map;

public final class PotionTimings {

    public static final Timing playerListTimer = Timings.ofSafe("Player List");
    public static final Timing connectionTimer = Timings.ofSafe("Connection Handler");
    public static final Timing timeUpdateTimer = Timings.ofSafe("Time Update");
    public static final Timing trackedEntitiesTick = Timings.ofSafe("Tracked Entities Tick");
    public static final Timing configManagerTick = Timings.ofSafe("Config Manager Tick");
    public static final Timing serverCommandTimer = Timings.ofSafe("Server Command");
    public static final Timing savePlayers = Timings.ofSafe("Save Players");

    public static final Timing tickEntityTimer = Timings.ofSafe("## tickEntity");
    public static final Timing tickTileEntityTimer = Timings.ofSafe("## tickTileEntity");
    public static final Timing packetProcessTimer = Timings.ofSafe("## Packet Processing");

    private static final Map<Class<? extends Runnable>, String> taskNameCache = new MapMaker().weakKeys().makeMap();

    private PotionTimings() {}

    /**
     * Get a named timer for the specified entity type to track type specific timings.
     * @param entity
     * @return
     */
    public static Timing getEntityTimings(Entity entity) {
        String entityType = entity.getClass().getName();
        return Timings.ofSafe("Potion", "## tickEntity - " + entityType, tickEntityTimer);
    }

    /**
     * Get a named timer for the specified tile entity type to track type specific timings.
     * @param entity
     * @return
     */
    public static Timing getTileEntityTimings(TileEntity entity) {
        String entityType = entity.getClass().getName();
        return Timings.ofSafe("Potion", "## tickTileEntity - " + entityType, tickTileEntityTimer);
    }
    public static void stopServer() {
        TimingsManager.stopServer();
    }

    public static Timing getPacketTiming(Packet packet) {
        return Timings.ofSafe("## Packet - " + packet.getClass().getSimpleName(), packetProcessTimer);
    }
}
