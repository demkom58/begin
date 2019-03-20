package com.demkom58.timings;

import java.util.List;

import static co.aikar.util.JSONUtil.toArrayMapper;

class TimingHistoryEntry {
    final TimingData data;
    private final TimingData[] children;

    TimingHistoryEntry(TimingHandler handler) {
        this.data = handler.record.clone();
        children = handler.cloneChildren();
    }

    List<Object> export() {
        List<Object> result = data.export();
        if (children.length > 0) {
            result.add(
                toArrayMapper(children, TimingData::export)
            );
        }
        return result;
    }
}
