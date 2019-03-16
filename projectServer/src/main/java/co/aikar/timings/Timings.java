/*
 * This file is licensed under the MIT License (MIT).
 *
 * Copyright (c) 2014 Daniel Ennis <http://aikar.co>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package co.aikar.timings;

import com.google.common.collect.EvictingQueue;
import net.minecraft.server.MinecraftServer;

import java.util.Queue;
import java.util.logging.Level;

@SuppressWarnings({"UnusedDeclaration", "WeakerAccess", "SameParameterValue"})
public final class Timings {

    private static final int MAX_HISTORY_FRAMES = 12;
    public static final Timing NULL_HANDLER = new NullTimingHandler();
    static boolean timingsEnabled = false;
    static boolean verboseEnabled = false;
    private static int historyInterval = -1;
    private static int historyLength = -1;

    private Timings() {}


    /**
     * Gets whether or not the Spigot Timings system is enabled
     *
     * @return Enabled or not
     */
    public static boolean isTimingsEnabled() {
        return timingsEnabled;
    }

    /**
     * <p>Sets whether or not the Spigot Timings system should be enabled</p>
     *
     * Calling this will reset timing data.
     *
     * @param enabled Should timings be reported
     */
    public static void setTimingsEnabled(boolean enabled) {
        timingsEnabled = enabled;
        reset();
    }

    /**
     * <p>Sets whether or not the Timings should monitor at Verbose level.</p>
     *
     * <p>When Verbose is disabled, high-frequency timings will not be available.</p>
     *
     * @return Enabled or not
     */
    public static boolean isVerboseTimingsEnabled() {
        return verboseEnabled;
    }

    /**
     * <p>Sets whether or not the Timings should monitor at Verbose level.</p>
     *
     * When Verbose is disabled, high-frequency timings will not be available.
     * Calling this will reset timing data.
     *
     * @param enabled Should high-frequency timings be reported
     */
    public static void setVerboseTimingsEnabled(boolean enabled) {
        verboseEnabled = enabled;
        TimingsManager.needsRecheckEnabled = true;
    }

    /**
     * <p>Gets the interval between Timing History report generation.</p>
     *
     * Defaults to 5 minutes (6000 ticks)
     *
     * @return Interval in ticks
     */
    public static int getHistoryInterval() {
        return historyInterval;
    }

    /**
     * <p>Sets the interval between Timing History report generations.</p>
     *
     * <p>Defaults to 5 minutes (6000 ticks)</p>
     *
     * This will recheck your history length, so lowering this value will lower your
     * history length if you need more than 60 history windows.
     *
     * @param interval Interval in ticks
     */
    public static void setHistoryInterval(int interval) {
        historyInterval = Math.max(20*60, interval);
        // Recheck the history length with the new Interval
        if (historyLength != -1) {
            setHistoryLength(historyLength);
        }
    }

    /**
     * Gets how long in ticks Timings history is kept for the server.
     *
     * Defaults to 1 hour (72000 ticks)
     *
     * @return Duration in Ticks
     */
    public static int getHistoryLength() {
        return historyLength;
    }

    /**
     * Sets how long Timing History reports are kept for the server.
     *
     * Defaults to 1 hours(72000 ticks)
     *
     * This value is capped at a maximum of getHistoryInterval() * MAX_HISTORY_FRAMES (12)
     *
     * Will not reset Timing Data but may truncate old history if the new length is less than old length.
     *
     * @param length Duration in ticks
     */
    public static void setHistoryLength(int length) {
        // Cap at 12 History Frames, 1 hour at 5 minute frames.
        int maxLength = historyInterval * MAX_HISTORY_FRAMES;
        // For special cases of servers with special permission to bypass the max.
        // This max helps keep data file sizes reasonable for processing on Aikar's Timing parser side.
        // Setting this will not help you bypass the max unless Aikar has added an exception on the API side.
        if (System.getProperty("timings.bypassMax") != null) {
            maxLength = Integer.MAX_VALUE;
        }
        historyLength = Math.max(Math.min(maxLength, length), historyInterval);
        Queue<TimingHistory> oldQueue = TimingsManager.HISTORY;
        int frames = (getHistoryLength() / getHistoryInterval());
        if (length > maxLength) {
            MinecraftServer.LOGGER.log(Level.WARNING, "Timings Length too high. Requested " + length + ", max is " + maxLength + ". To get longer history, you must increase your interval. Set Interval to " + Math.ceil(length / MAX_HISTORY_FRAMES) + " to achieve this length.");
        }
        TimingsManager.HISTORY = EvictingQueue.create(frames);
        TimingsManager.HISTORY.addAll(oldQueue);
    }

    /**
     * Resets all Timing Data
     */
    public static void reset() {
        TimingsManager.reset();
    }

    /*
    =================
    Protected API: These are for internal use only in Bukkit/CraftBukkit
    These do not have isPrimaryThread() checks in the startTiming/stopTiming
    =================
    */
    static TimingHandler ofSafe(String name) {
        return ofSafe(null, name, null);
    }

    static TimingHandler ofSafe(String name, Timing groupHandler) {
        return ofSafe(null, name, groupHandler);
    }

    static TimingHandler ofSafe(String groupName, String name, Timing groupHandler) {
        return TimingsManager.getHandler(groupName, name, groupHandler, false);
    }

    public static void generateReport() {
        TimingsExport.report = true;
    }
}
