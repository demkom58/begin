package net.minecraft.client.render.texture;

import net.hypnosis.render.Tessellator;
import net.minecraft.util.Vec3D;

public class TexturedQuad {
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;

    public TexturedQuad(PositionTextureVertex[] var1) {
        this.nVertices = 0;
        this.invertNormal = false;
        this.vertexPositions = var1;
        this.nVertices = var1.length;
    }

    public TexturedQuad(PositionTextureVertex[] vertices, int l1, int r1, int l2, int r2) {
        this(vertices);

        vertices[0] = vertices[0].setTexturePosition((float) l2 / 64.0F, (float) r1 / 32.0F);
        vertices[1] = vertices[1].setTexturePosition((float) l1 / 64.0F, (float) r1 / 32.0F);
        vertices[2] = vertices[2].setTexturePosition((float) l1 / 64.0F, (float) r2 / 32.0F);
        vertices[3] = vertices[3].setTexturePosition((float) l2 / 64.0F, (float) r2 / 32.0F);
    }

    public void flipFace() {
        PositionTextureVertex[] vertices = new PositionTextureVertex[this.vertexPositions.length];

        for (int i = 0; i < this.vertexPositions.length; ++i)
            vertices[i] = this.vertexPositions[this.vertexPositions.length - i - 1];

        this.vertexPositions = vertices;
    }

    public void draw(Tessellator tess, float delta) {
        Vec3D var3 = this.vertexPositions[1].vector3D.subtract(this.vertexPositions[0].vector3D);
        Vec3D var4 = this.vertexPositions[1].vector3D.subtract(this.vertexPositions[2].vector3D);
        Vec3D var5 = var4.crossProduct(var3).normalize();

        tess.startDrawingQuads();
        if (this.invertNormal) {
            tess.setNormal(-((float) var5.xCoord), -((float) var5.yCoord), -((float) var5.zCoord));
        } else {
            tess.setNormal((float) var5.xCoord, (float) var5.yCoord, (float) var5.zCoord);
        }

        for (int renderPass = 0; renderPass < 4; ++renderPass) {
            PositionTextureVertex vertex = this.vertexPositions[renderPass];
            tess.addVertexWithUV(
                    (float) vertex.vector3D.xCoord * delta,
                    (float) vertex.vector3D.yCoord * delta,
                    (float) vertex.vector3D.zCoord * delta,
                    vertex.texturePositionX,
                    vertex.texturePositionY
            );
        }

        tess.draw();
    }
}
