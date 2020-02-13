package net.minecraft.client.render.entity;

import net.minecraft.client.render.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderEntity extends Render {
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float delta) {
        GL11.glPushMatrix();
        renderOffsetAABB(entity.boundingBox, x - entity.lastTickPosX, y - entity.lastTickPosY, z - entity.lastTickPosZ);
        GL11.glPopMatrix();
    }
}
