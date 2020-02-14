package net.potion.client.model;

import net.hypnosis.render.Tessellator;
import net.potion.client.render.GLAllocation;
import net.potion.client.render.texture.PositionTextureVertex;
import net.potion.client.render.texture.TexturedQuad;
import org.lwjgl.opengl.GL11;

public class ModelRenderer {
    public float rotationPointX;
    public float rotationPointY;
    public float rotationPointZ;
    public float rotateAngleX;
    public float rotateAngleY;
    public float rotateAngleZ;
    public boolean mirror = false;
    public boolean showModel = true;
    public boolean isHidden = false;
    private PositionTextureVertex[] corners;
    private TexturedQuad[] faces;
    private int textureOffsetX;
    private int textureOffsetY;
    private boolean compiled = false;
    private int displayList = 0;

    public ModelRenderer(int textureOffsetX, int textureOffsetY) {
        this.textureOffsetX = textureOffsetX;
        this.textureOffsetY = textureOffsetY;
    }

    public void addBox(float minX, float minY, float minZ, int maxX, int maxY, int maxZ) {
        this.addBox(minX, minY, minZ, maxX, maxY, maxZ, 0.0F);
    }

    public void addBox(float minX, float minY, float minZ, int maxX, int maxY, int maxZ, float addition) {
        this.corners = new PositionTextureVertex[8];
        this.faces = new TexturedQuad[6];

        float endX = minX + (float) maxX;
        float endY = minY + (float) maxY;
        float endZ = minZ + (float) maxZ;

        minX = minX - addition;
        minY = minY - addition;
        minZ = minZ - addition;

        endX = endX + addition;
        endY = endY + addition;
        endZ = endZ + addition;

        if (this.mirror) {
            float tempEndX = endX;
            endX = minX;
            minX = tempEndX;
        }

        PositionTextureVertex vertex1 = new PositionTextureVertex(minX, minY, minZ, 0.0F, 0.0F);
        PositionTextureVertex vertex2 = new PositionTextureVertex(endX, minY, minZ, 0.0F, 8.0F);
        PositionTextureVertex vertex3 = new PositionTextureVertex(endX, endY, minZ, 8.0F, 8.0F);
        PositionTextureVertex vertex4 = new PositionTextureVertex(minX, endY, minZ, 8.0F, 0.0F);
        PositionTextureVertex vertex5 = new PositionTextureVertex(minX, minY, endZ, 0.0F, 0.0F);
        PositionTextureVertex vertex6 = new PositionTextureVertex(endX, minY, endZ, 0.0F, 8.0F);
        PositionTextureVertex vertex7 = new PositionTextureVertex(endX, endY, endZ, 8.0F, 8.0F);
        PositionTextureVertex vertex8 = new PositionTextureVertex(minX, endY, endZ, 8.0F, 0.0F);

        this.corners[0] = vertex1;
        this.corners[1] = vertex2;
        this.corners[2] = vertex3;
        this.corners[3] = vertex4;
        this.corners[4] = vertex5;
        this.corners[5] = vertex6;
        this.corners[6] = vertex7;
        this.corners[7] = vertex8;

        this.faces[0] = new TexturedQuad(new PositionTextureVertex[]{vertex6, vertex2, vertex3, vertex7},
                this.textureOffsetX + maxZ + maxX, this.textureOffsetY + maxZ, this.textureOffsetX + maxZ + maxX + maxZ, this.textureOffsetY + maxZ + maxY);
        this.faces[1] = new TexturedQuad(new PositionTextureVertex[]{vertex1, vertex5, vertex8, vertex4},
                this.textureOffsetX, this.textureOffsetY + maxZ, this.textureOffsetX + maxZ, this.textureOffsetY + maxZ + maxY);
        this.faces[2] = new TexturedQuad(new PositionTextureVertex[]{vertex6, vertex5, vertex1, vertex2},
                this.textureOffsetX + maxZ, this.textureOffsetY, this.textureOffsetX + maxZ + maxX, this.textureOffsetY + maxZ);
        this.faces[3] = new TexturedQuad(new PositionTextureVertex[]{vertex3, vertex4, vertex8, vertex7},
                this.textureOffsetX + maxZ + maxX, this.textureOffsetY, this.textureOffsetX + maxZ + maxX + maxX, this.textureOffsetY + maxZ);
        this.faces[4] = new TexturedQuad(new PositionTextureVertex[]{vertex2, vertex1, vertex4, vertex3},
                this.textureOffsetX + maxZ, this.textureOffsetY + maxZ, this.textureOffsetX + maxZ + maxX, this.textureOffsetY + maxZ + maxY);
        this.faces[5] = new TexturedQuad(new PositionTextureVertex[]{vertex5, vertex6, vertex7, vertex8},
                this.textureOffsetX + maxZ + maxX + maxZ, this.textureOffsetY + maxZ, this.textureOffsetX + maxZ + maxX + maxZ + maxX, this.textureOffsetY + maxZ + maxY);

        if (this.mirror)
            for (int i = 0; i < this.faces.length; ++i)
                this.faces[i].flipFace();
    }

    public void setRotationPoint(float x, float y, float z) {
        this.rotationPointX = x;
        this.rotationPointY = y;
        this.rotationPointZ = z;
    }

    public void render(float delta) {
        if (this.isHidden || !this.showModel)
            return;

        if (!this.compiled)
            this.compileDisplayList(delta);

        if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
                GL11.glCallList(this.displayList);
                return;
            }

            GL11.glTranslatef(this.rotationPointX * delta, this.rotationPointY * delta, this.rotationPointZ * delta);
            GL11.glCallList(this.displayList);
            GL11.glTranslatef(-this.rotationPointX * delta, -this.rotationPointY * delta, -this.rotationPointZ * delta);
            return;
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(this.rotationPointX * delta, this.rotationPointY * delta, this.rotationPointZ * delta);
        if (this.rotateAngleZ != 0.0F)
            GL11.glRotatef(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);

        if (this.rotateAngleY != 0.0F)
            GL11.glRotatef(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);

        if (this.rotateAngleX != 0.0F)
            GL11.glRotatef(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);

        GL11.glCallList(this.displayList);
        GL11.glPopMatrix();
    }

    public void renderWithRotation(float delta) {
        if (this.isHidden || !this.showModel)
            return;

        if (!this.compiled)
            this.compileDisplayList(delta);

        GL11.glPushMatrix();
        GL11.glTranslatef(this.rotationPointX * delta, this.rotationPointY * delta, this.rotationPointZ * delta);
        if (this.rotateAngleY != 0.0F)
            GL11.glRotatef(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);

        if (this.rotateAngleX != 0.0F)
            GL11.glRotatef(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);

        if (this.rotateAngleZ != 0.0F)
            GL11.glRotatef(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);

        GL11.glCallList(this.displayList);
        GL11.glPopMatrix();
    }

    public void postRender(float delta) {
        if (this.isHidden || !this.showModel)
            return;

        if (!this.compiled)
            this.compileDisplayList(delta);

        if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
            if (this.rotationPointX != 0.0F || this.rotationPointY != 0.0F || this.rotationPointZ != 0.0F)
                GL11.glTranslatef(this.rotationPointX * delta, this.rotationPointY * delta, this.rotationPointZ * delta);
            return;
        }

        GL11.glTranslatef(this.rotationPointX * delta, this.rotationPointY * delta, this.rotationPointZ * delta);
        if (this.rotateAngleZ != 0.0F)
            GL11.glRotatef(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);

        if (this.rotateAngleY != 0.0F)
            GL11.glRotatef(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);

        if (this.rotateAngleX != 0.0F)
            GL11.glRotatef(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);

    }

    private void compileDisplayList(float delta) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(this.displayList, GL11.GL_COMPILE);
        Tessellator tess = Tessellator.INSTANCE;

        for (int i = 0; i < this.faces.length; ++i)
            this.faces[i].draw(tess, delta);

        GL11.glEndList();
        this.compiled = true;
    }
}
