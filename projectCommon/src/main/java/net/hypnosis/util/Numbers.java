package net.hypnosis.util;

import net.hypnosis.util.math.Vec3b;

public final class Numbers {
    public static short compact_XYZ_4bitShort(int x, int y, int z) {
        short compacted = 0;
        compacted |= (x & 15) << 8;
        compacted |= (y & 15) << 4;
        compacted |= (z & 15);
        return compacted;
    }

    public static short compact_XYZ_4bitShort(byte x, byte y, byte z) {
        short compacted = 0;
        compacted |= (x & 15) << 8;
        compacted |= (y & 15) << 4;
        compacted |= (z & 15);
        return compacted;
    }

    public static byte extractX_XYZ_4bitShort(short xyz) {
        return (byte) ((xyz >> 8) & 15);
    }

    public static byte extractY_XYZ_4bitShort(short xyz) {
        return (byte) ((xyz >> 4) & 15);
    }

    public static byte extractZ_XYZ_4bitShort(short xyz) {
        return (byte) (xyz & 15);
    }

    public static Vec3b extract_XYZ_4bitShort(short xyz) {
        return new Vec3b(
                extractX_XYZ_4bitShort(xyz),
                extractY_XYZ_4bitShort(xyz),
                extractZ_XYZ_4bitShort(xyz)
        );
    }
}
