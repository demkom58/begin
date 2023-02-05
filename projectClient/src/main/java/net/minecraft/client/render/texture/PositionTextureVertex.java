package net.minecraft.client.render.texture;

import net.hypnosis.util.math.Vec3d;

public class PositionTextureVertex {
    public Vec3d vec;
    public float ux;
    public float uv;

    public PositionTextureVertex(float x, float y, float z, float ux, float uy) {
        this(new Vec3d(x, y, z), ux, uy);
    }

    public PositionTextureVertex(PositionTextureVertex vertex, float ux, float uy) {
        this.vec = vertex.vec;
        this.ux = ux;
        this.uv = uy;
    }

    public PositionTextureVertex(Vec3d vector3d, float ux, float uv) {
        this.vec = vector3d;
        this.ux = ux;
        this.uv = uv;
    }

    public PositionTextureVertex setTexturePosition(float ux, float uy) {
        return new PositionTextureVertex(this, ux, uy);
    }
}
