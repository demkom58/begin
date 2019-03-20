package com.demkom58.timings;

public final class NullTimingHandler implements Timing {
    @Override
    public Timing startTiming() {
        return this;
    }

    @Override
    public void stopTiming() {

    }

    @Override
    public Timing startTimingIfSync() {
        return this;
    }

    @Override
    public void stopTimingIfSync() {

    }

    @Override
    public void abort() {

    }

    @Override
    public TimingHandler getTimingHandler() {
        return null;
    }

    @Override
    public void close() {

    }
}