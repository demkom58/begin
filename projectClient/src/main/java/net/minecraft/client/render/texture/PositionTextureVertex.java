package net.minecraft.client.render.texture;

import net.minecraft.util.Vec3D;

public class PositionTextureVertex {
    public Vec3D vector3D;
    public float texturePositionX;
    public float texturePositionY;

    public PositionTextureVertex(float x, float y, float z, float ux, float uy) {
        this(Vec3D.createVectorHelper(x, y, z), ux, uy);
    }

    public PositionTextureVertex(PositionTextureVertex vertex, float ux, float uy) {
        this.vector3D = vertex.vector3D;
        this.texturePositionX = ux;
        this.texturePositionY = uy;
    }

    public PositionTextureVertex(Vec3D vector3d, float ux, float uv) {
        this.vector3D = vector3d;
        this.texturePositionX = ux;
        this.texturePositionY = uv;
    }

    public PositionTextureVertex setTexturePosition(float ux, float uy) {
        return new PositionTextureVertex(this, ux, uy);
    }
}
