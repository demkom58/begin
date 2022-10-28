package net.potion.client.render.texture;

import net.hypnosis.render.Tessellator;
import net.hypnosis.util.math.MathHelper;
import net.hypnosis.util.math.Vec3d;

public class TexturedQuad {
    public PositionTextureVertex[] vertexPositions;
    public int nVertices;
    private boolean invertNormal;

    public TexturedQuad(PositionTextureVertex[] vertices) {
        this.invertNormal = false;
        this.vertexPositions = vertices;
        this.nVertices = vertices.length;
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
        Vec3d var3 = new Vec3d(this.vertexPositions[1].vec).subtract(this.vertexPositions[0].vec);
        Vec3d var4 = new Vec3d(this.vertexPositions[1].vec).subtract(this.vertexPositions[2].vec);
        Vec3d var5 = MathHelper.normalizeOrZero(new Vec3d(var4).crossProduct(var3));

        tess.startDrawingQuads();
        if (this.invertNormal) {
            tess.setNormal(-((float) var5.x), -((float) var5.y), -((float) var5.z));
        } else {
            tess.setNormal((float) var5.x, (float) var5.y, (float) var5.z);
        }

        for (int renderPass = 0; renderPass < 4; ++renderPass) {
            PositionTextureVertex vertex = this.vertexPositions[renderPass];
            tess.addVertexWithUV(
                    (float) vertex.vec.x * delta,
                    (float) vertex.vec.y * delta,
                    (float) vertex.vec.z * delta,
                    vertex.ux,
                    vertex.uv
            );
        }

        tess.draw();
    }
}
