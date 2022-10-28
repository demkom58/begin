package net.hypnosis.util;

import net.hypnosis.util.math.Vec3b;

public final class Numbers {
    private static final long BIT21_NEGATIVE_FLAG = 0b1000000000000000000000L;
    private static final long BIT20_VALUE_MASK = 0b111111111111111111111L;

    public static long compact_XYZ_21bitLong(int x, int y, int z) {
        long composed =
                x < 0 ? (BIT21_NEGATIVE_FLAG | (x & BIT20_VALUE_MASK)) << 42 : (x & BIT20_VALUE_MASK) << 42;
        composed |=
                y < 0 ? (BIT21_NEGATIVE_FLAG | (y & BIT20_VALUE_MASK)) << 21 : (y & BIT20_VALUE_MASK) << 21;
        composed |=
                z < 0 ? (BIT21_NEGATIVE_FLAG | (z & BIT20_VALUE_MASK)) : (z & BIT20_VALUE_MASK);
        return composed;
    }

    public static short compact_XYZ_u4bitShort(int x, int y, int z) {
        short compacted = 0;
        compacted |= (x & 15) << 8;
        compacted |= (y & 15) << 4;
        compacted |= (z & 15);
        return compacted;
    }

    public static short compact_XYZ_u4bitShort(byte x, byte y, byte z) {
        short compacted = 0;
        compacted |= (x & 15) << 8;
        compacted |= (y & 15) << 4;
        compacted |= (z & 15);
        return compacted;
    }

    public static byte extractX_XYZ_u4bitShort(short xyz) {
        return (byte) ((xyz >> 8) & 15);
    }

    public static byte extractY_XYZ_u4bitShort(short xyz) {
        return (byte) ((xyz >> 4) & 15);
    }

    public static byte extractZ_XYZ_u4bitShort(short xyz) {
        return (byte) (xyz & 15);
    }

    public static Vec3b extract_XYZ_u4bitShort(short xyz) {
        return new Vec3b(
                extractX_XYZ_u4bitShort(xyz),
                extractY_XYZ_u4bitShort(xyz),
                extractZ_XYZ_u4bitShort(xyz)
        );
    }
}
