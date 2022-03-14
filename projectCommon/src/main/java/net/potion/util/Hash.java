package net.potion.util;

import java.util.Arrays;

public class Hash {
    private final float growFactor = 0.75F;
    private transient HashEntry[] slots = new HashEntry[16];
    private transient int count;
    private int threshold = 12;
    private transient volatile int versionStamp;

    private static int computeHash(int var0) {
        var0 = var0 ^ var0 >>> 20 ^ var0 >>> 12;
        return var0 ^ var0 >>> 7 ^ var0 >>> 4;
    }

    private static int getSlotIndex(int var0, int var1) {
        return var0 & var1 - 1;
    }

    // $FF: synthetic method
    static int getHash(int var0) {
        return computeHash(var0);
    }

    public Object lookup(int var1) {
        int var2 = computeHash(var1);

        for (HashEntry var3 = this.slots[getSlotIndex(var2, this.slots.length)]; var3 != null; var3 = var3.nextEntry) {
            if (var3.hashEntry == var1) {
                return var3.valueEntry;
            }
        }

        return null;
    }

    public boolean containsItem(int var1) {
        return this.lookupEntry(var1) != null;
    }

    final HashEntry lookupEntry(int var1) {
        int var2 = computeHash(var1);

        for (HashEntry var3 = this.slots[getSlotIndex(var2, this.slots.length)]; var3 != null; var3 = var3.nextEntry) {
            if (var3.hashEntry == var1) {
                return var3;
            }
        }

        return null;
    }

    public void addKey(int var1, Object var2) {
        int var3 = computeHash(var1);
        int var4 = getSlotIndex(var3, this.slots.length);

        for (HashEntry var5 = this.slots[var4]; var5 != null; var5 = var5.nextEntry) {
            if (var5.hashEntry == var1) {
                var5.valueEntry = var2;
            }
        }

        ++this.versionStamp;
        this.insert(var3, var1, var2, var4);
    }

    private void grow(int var1) {
        HashEntry[] var2 = this.slots;
        int var3 = var2.length;
        if (var3 == 1073741824) {
            this.threshold = Integer.MAX_VALUE;
        } else {
            HashEntry[] var4 = new HashEntry[var1];
            this.copyTo(var4);
            this.slots = var4;
            this.threshold = (int) ((float) var1 * this.growFactor);
        }
    }

    private void copyTo(HashEntry[] var1) {
        HashEntry[] var2 = this.slots;
        int var3 = var1.length;

        for (int var4 = 0; var4 < var2.length; ++var4) {
            HashEntry var5 = var2[var4];
            if (var5 != null) {
                var2[var4] = null;

                while (true) {
                    HashEntry var6 = var5.nextEntry;
                    int var7 = getSlotIndex(var5.slotHash, var3);
                    var5.nextEntry = var1[var7];
                    var1[var7] = var5;
                    var5 = var6;
                    if (var6 == null) {
                        break;
                    }
                }
            }
        }

    }

    public Object removeObject(int var1) {
        HashEntry removed = this.removeEntry(var1);
        return removed == null ? null : removed.valueEntry;
    }

    final HashEntry removeEntry(int var1) {
        int hash = computeHash(var1);
        int index = getSlotIndex(hash, this.slots.length);
        HashEntry value = this.slots[index];

        HashEntry next;
        for (HashEntry cur = value; cur != null; cur = next) {
            next = cur.nextEntry;
            if (cur.hashEntry == var1) {
                ++this.versionStamp;
                --this.count;
                if (value == cur) {
                    this.slots[index] = next;
                } else {
                    value.nextEntry = next;
                }

                return cur;
            }

            value = cur;
        }

        return null;
    }

    public void clearMap() {
        ++this.versionStamp;
        Arrays.fill(this.slots, null);
        this.count = 0;
    }

    private void insert(int var1, int var2, Object var3, int var4) {
        HashEntry var5 = this.slots[var4];
        this.slots[var4] = new HashEntry(var1, var2, var3, var5);
        if (this.count++ >= this.threshold) {
            this.grow(2 * this.slots.length);
        }

    }
}
