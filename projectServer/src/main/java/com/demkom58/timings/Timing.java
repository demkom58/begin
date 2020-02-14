package com.demkom58.timings;

/**
 * Provides an ability to time sections of code within the Potion Server
 */
public interface Timing extends AutoCloseable {
    /**
     * Starts timing the execution until {@link #stopTiming()} is called.
     *
     * @return Timing
     */
    Timing startTiming();

    /**
     * <p>Stops timing and records the data. Propagates the data up to group handlers.</p>
     *
     * Will automatically be called when this Timing is used with try-with-resources
     */
    void stopTiming();

    /**
     * Starts timing the execution until {@link #stopTiming()} is called.
     *
     * But only if we are on the primary thread.
     *
     * @return Timing
     */
    Timing startTimingIfSync();

    /**
     * <p>Stops timing and records the data. Propagates the data up to group handlers.</p>
     *
     * <p>Will automatically be called when this Timing is used with try-with-resources</p>
     *
     * But only if we are on the primary thread.
     */
    void stopTimingIfSync();

    /**
     * Stops timing and disregards current timing data.
     */
    void abort();

    /**
     * Used internally to get the actual backing Handler in the case of delegated Handlers
     *
     * @return TimingHandler
     */
    TimingHandler getTimingHandler();

    @Override
    void close();
}
