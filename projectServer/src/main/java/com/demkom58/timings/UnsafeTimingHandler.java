package com.demkom58.timings;

import net.potion.server.PotionServer;

class UnsafeTimingHandler extends TimingHandler {

    UnsafeTimingHandler(TimingIdentifier id) {
        super(id);
    }

    private static void checkThread() {
        if (!PotionServer.SERVER.isPrimaryThread()) {
            throw new IllegalStateException("Calling Timings from Async Operation");
        }
    }

    @Override
    public Timing startTiming() {
        checkThread();
        return super.startTiming();
    }

    @Override
    public void stopTiming() {
        checkThread();
        super.stopTiming();
    }
}
