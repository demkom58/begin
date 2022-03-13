package net.hypnosis.util.math;


public final class Quaternion {
    public static final Quaternion IDENTITY = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Quaternion(Vec3f axis, float rotationAngle, boolean degrees) {
        if (degrees) {
            rotationAngle *= 0.017453292F;
        }

        float f = sin(rotationAngle / 2.0F);
        this.x = axis.getX() * f;
        this.y = axis.getY() * f;
        this.z = axis.getZ() * f;
        this.w = cos(rotationAngle / 2.0F);
    }

    public Quaternion(float x, float y, float z, boolean degrees) {
        if (degrees) {
            x *= 0.017453292F;
            y *= 0.017453292F;
            z *= 0.017453292F;
        }

        float f = sin(0.5F * x);
        float g = cos(0.5F * x);
        float h = sin(0.5F * y);
        float i = cos(0.5F * y);
        float j = sin(0.5F * z);
        float k = cos(0.5F * z);
        this.x = f * i * k + g * h * j;
        this.y = g * h * k - f * i * j;
        this.z = f * h * k + g * i * j;
        this.w = g * i * k - f * h * j;
    }

    public Quaternion(Quaternion other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.w = other.w;
    }

    public static Quaternion fromEulerYxz(float x, float y, float z) {
        Quaternion quaternion = IDENTITY.copy();
        quaternion.hamiltonProduct(new Quaternion(0.0F, (float)Math.sin(x / 2.0F), 0.0F, (float)Math.cos(x / 2.0F)));
        quaternion.hamiltonProduct(new Quaternion((float)Math.sin(y / 2.0F), 0.0F, 0.0F, (float)Math.cos(y / 2.0F)));
        quaternion.hamiltonProduct(new Quaternion(0.0F, 0.0F, (float)Math.sin(z / 2.0F), (float)Math.cos(z / 2.0F)));
        return quaternion;
    }

    public static Quaternion fromEulerXyzDegrees(Vec3f vector) {
        return fromEulerXyz((float)Math.toRadians(vector.getX()), (float)Math.toRadians(vector.getY()), (float)Math.toRadians(vector.getZ()));
    }

    public static Quaternion fromEulerXyz(Vec3f vector) {
        return fromEulerXyz(vector.getX(), vector.getY(), vector.getZ());
    }

    public static Quaternion fromEulerXyz(float x, float y, float z) {
        Quaternion quaternion = IDENTITY.copy();
        quaternion.hamiltonProduct(new Quaternion((float)Math.sin(x / 2.0F), 0.0F, 0.0F, (float)Math.cos(x / 2.0F)));
        quaternion.hamiltonProduct(new Quaternion(0.0F, (float)Math.sin(y / 2.0F), 0.0F, (float)Math.cos(y / 2.0F)));
        quaternion.hamiltonProduct(new Quaternion(0.0F, 0.0F, (float)Math.sin(z / 2.0F), (float)Math.cos(z / 2.0F)));
        return quaternion;
    }

    public Vec3f toEulerYxz() {
        float w2 = this.getW() * this.getW();
        float x2 = this.getX() * this.getX();
        float y2 = this.getY() * this.getY();
        float z2 = this.getZ() * this.getZ();
        float sumSq = w2 + x2 + y2 + z2;
        float k = 2.0F * this.getW() * this.getX() - 2.0F * this.getY() * this.getZ();
        float l = (float)Math.asin(k / sumSq);
        return Math.abs(k) > 0.999F * sumSq ? new Vec3f(2.0F * (float)Math.atan2(this.getX(), this.getW()), l, 0.0F) : new Vec3f((float)Math.atan2(2.0F * this.getY() * this.getZ() + 2.0F * this.getX() * this.getW(), w2 - x2 - y2 + z2), l, (float)Math.atan2(2.0F * this.getX() * this.getY() + 2.0F * this.getW() * this.getZ(), w2 + x2 - y2 - z2));
    }

    public Vec3f toEulerYxzDegrees() {
        Vec3f eulerYxz = this.toEulerYxz();
        return new Vec3f(
                (float)Math.toDegrees(eulerYxz.getX()),
                (float)Math.toDegrees(eulerYxz.getY()),
                (float)Math.toDegrees(eulerYxz.getZ())
        );
    }

    public Vec3f toEulerXyz() {
        float w2 = this.getW() * this.getW();
        float x2 = this.getX() * this.getX();
        float y2 = this.getY() * this.getY();
        float z2 = this.getZ() * this.getZ();
        float sumSq = w2 + x2 + y2 + z2;
        float k = 2.0F * this.getW() * this.getX() - 2.0F * this.getY() * this.getZ();
        float l = (float)Math.asin(k / sumSq);
        return Math.abs(k) > 0.999F * sumSq
                ? new Vec3f(l, 2.0F * (float)Math.atan2(this.getY(), this.getW()), 0.0F)
                : new Vec3f(l, (float)Math.atan2(2.0F * this.getX() * this.getZ() + 2.0F * this.getY() * this.getW(), w2 - x2 - y2 + z2), (float)Math.atan2(2.0F * this.getX() * this.getY() + 2.0F * this.getW() * this.getZ(), w2 - x2 + y2 - z2));
    }

    public Vec3f toEulerXyzDegrees() {
        Vec3f eulerXyz = this.toEulerXyz();
        return new Vec3f(
                (float)Math.toDegrees(eulerXyz.getX()),
                (float)Math.toDegrees(eulerXyz.getY()),
                (float)Math.toDegrees(eulerXyz.getZ())
        );
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Quaternion quaternion = (Quaternion)o;
            if (Float.compare(quaternion.x, this.x) != 0) {
                return false;
            } else if (Float.compare(quaternion.y, this.y) != 0) {
                return false;
            } else if (Float.compare(quaternion.z, this.z) != 0) {
                return false;
            } else {
                return Float.compare(quaternion.w, this.w) == 0;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = Float.floatToIntBits(this.x);
        i = 31 * i + Float.floatToIntBits(this.y);
        i = 31 * i + Float.floatToIntBits(this.z);
        i = 31 * i + Float.floatToIntBits(this.w);
        return i;
    }

    public String toString() {
        return "Quaternion[" + this.getW() + " + " +
                this.getX() + "i + " +
                this.getY() + "j + " +
                this.getZ() + "k]";
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public float getW() {
        return this.w;
    }

    public void hamiltonProduct(Quaternion other) {
        float x = this.getX();
        float y = this.getY();
        float z = this.getZ();
        float w = this.getW();

        float oX = other.getX();
        float oY = other.getY();
        float oZ = other.getZ();
        float oW = other.getW();

        this.x = w * oX + x * oW + y * oZ - z * oY;
        this.y = w * oY - x * oZ + y * oW + z * oX;
        this.z = w * oZ + x * oY - y * oX + z * oW;
        this.w = w * oW - x * oX - y * oY - z * oZ;
    }

    public void scale(float scale) {
        this.x *= scale;
        this.y *= scale;
        this.z *= scale;
        this.w *= scale;
    }

    public void conjugate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
    }

    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    private static float cos(float value) {
        return (float)Math.cos(value);
    }

    private static float sin(float value) {
        return (float)Math.sin(value);
    }

    public void normalize() {
        float f = this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ() + this.getW() * this.getW();
        if (f > 1.0E-6F) {
            float g = MathHelper.fastInverseSqrt(f);
            this.x *= g;
            this.y *= g;
            this.z *= g;
            this.w *= g;
        } else {
            this.x = 0.0F;
            this.y = 0.0F;
            this.z = 0.0F;
            this.w = 0.0F;
        }

    }

    public Quaternion copy() {
        return new Quaternion(this);
    }
}
