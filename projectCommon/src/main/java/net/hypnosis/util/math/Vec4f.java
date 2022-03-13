package net.hypnosis.util.math;

public class Vec4f {
    private float x;
    private float y;
    private float z;
    private float w;

    public Vec4f() {
    }

    public Vec4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4f(Vec3f vector) {
        this(vector.getX(), vector.getY(), vector.getZ(), 1.0F);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Vec4f Vec4f = (Vec4f)o;
            if (Float.compare(Vec4f.x, this.x) != 0) {
                return false;
            } else if (Float.compare(Vec4f.y, this.y) != 0) {
                return false;
            } else if (Float.compare(Vec4f.z, this.z) != 0) {
                return false;
            } else {
                return Float.compare(Vec4f.w, this.w) == 0;
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

    public void multiply(float value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
        this.w *= value;
    }

    public void multiplyComponentwise(Vec3f vector) {
        this.x *= vector.getX();
        this.y *= vector.getY();
        this.z *= vector.getZ();
    }

    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
    }

    public float dotProduct(Vec4f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    }

    public boolean normalize() {
        float f = this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
        if ((double)f < 1.0E-5D) {
            return false;
        } else {
            float g = MathHelper.fastInverseSqrt(f);
            this.x *= g;
            this.y *= g;
            this.z *= g;
            this.w *= g;
            return true;
        }
    }

    public void transform(Matrix4f m) {
        float f = this.x;
        float g = this.y;
        float h = this.z;
        float i = this.w;
        this.x = m.a00 * f + m.a01 * g + m.a02 * h + m.a03 * i;
        this.y = m.a10 * f + m.a11 * g + m.a12 * h + m.a13 * i;
        this.z = m.a20 * f + m.a21 * g + m.a22 * h + m.a23 * i;
        this.w = m.a30 * f + m.a31 * g + m.a32 * h + m.a33 * i;
    }

    public void rotate(Quaternion rotation) {
        Quaternion quaternion = new Quaternion(rotation);
        quaternion.hamiltonProduct(new Quaternion(this.getX(), this.getY(), this.getZ(), 0.0F));
        Quaternion quaternion2 = new Quaternion(rotation);
        quaternion2.conjugate();
        quaternion.hamiltonProduct(quaternion2);
        this.set(quaternion.getX(), quaternion.getY(), quaternion.getZ(), this.getW());
    }

    public void normalizeProjectiveCoordinates() {
        this.x /= this.w;
        this.y /= this.w;
        this.z /= this.w;
        this.w = 1.0F;
    }

    public void lerp(Vec4f to, float delta) {
        float f = 1.0F - delta;
        this.x = this.x * f + to.x * delta;
        this.y = this.y * f + to.y * delta;
        this.z = this.z * f + to.z * delta;
        this.w = this.w * f + to.w * delta;
    }

    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
    }
}
