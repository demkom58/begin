package net.minecraft.util;

import java.util.Objects;

class MCHashEntry {
    final int hashEntry;
    final int slotHash;
    Object valueEntry;
    MCHashEntry nextEntry;

    MCHashEntry(int var1, int var2, Object var3, MCHashEntry var4) {
        this.valueEntry = var3;
        this.nextEntry = var4;
        this.hashEntry = var2;
        this.slotHash = var1;
    }

    public final int getHash() {
        return this.hashEntry;
    }

    public final Object getValue() {
        return this.valueEntry;
    }

    public final boolean equals(Object var1) {
        if (!(var1 instanceof MCHashEntry))
            return false;

        MCHashEntry var2 = (MCHashEntry) var1;
        Integer var3 = this.getHash();
        Integer var4 = var2.getHash();
        if (Objects.equals(var3, var4)) {
            Object var5 = this.getValue();
            Object var6 = var2.getValue();
            return Objects.equals(var5, var6);
        }

        return false;
    }

    public final int hashCode() {
        return MCHash.getHash(this.hashEntry);
    }

    public final String toString() {
        return this.getHash() + "=" + this.getValue();
    }
}
