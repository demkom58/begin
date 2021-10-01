package net.potion.client.render.texture;

import org.joml.Vector3d;

public class PositionTextureVertex {
    public Vector3d vec;
    public float ux;
    public float uv;

    public PositionTextureVertex(float x, float y, float z, float ux, float uy) {
        this(new Vector3d(x, y, z), ux, uy);
    }

    public PositionTextureVertex(PositionTextureVertex vertex, float ux, float uy) {
        this.vec = vertex.vec;
        this.ux = ux;
        this.uv = uy;
    }

    public PositionTextureVertex(Vector3d vector3d, float ux, float uv) {
        this.vec = vector3d;
        this.ux = ux;
        this.uv = uv;
    }

    public PositionTextureVertex setTexturePosition(float ux, float uy) {
        return new PositionTextureVertex(this, ux, uy);
    }
}
