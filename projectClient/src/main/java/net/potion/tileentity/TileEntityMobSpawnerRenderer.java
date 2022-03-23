package net.potion.tileentity;

import net.potion.client.render.RenderManager;
import net.potion.entity.Entity;
import net.potion.entity.EntityList;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.Map;

public class TileEntityMobSpawnerRenderer extends TileEntitySpecialRenderer {
    private final Map<String, Entity> entityHashMap = new HashMap<>();

    public void renderTileEntityMobSpawner(TileEntityMobSpawner spawner, double x, double y, double z, float delta) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y, (float) z + 0.5F);
        Entity entity = this.entityHashMap.get(spawner.getMobID());
        if (entity == null) {
            entity = EntityList.createEntityInWorld(spawner.getMobID(), null);
            this.entityHashMap.put(spawner.getMobID(), entity);
        }

        if (entity != null) {
            entity.setWorld(spawner.world);
            float scale = 0.4375F;
            GL11.glTranslatef(0.0F, 0.4F, 0.0F);
            GL11.glRotatef((float) (spawner.yaw2 + (spawner.yaw - spawner.yaw2) * (double) delta) * 10.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-30.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(0.0F, -0.4F, 0.0F);
            GL11.glScalef(scale, scale, scale);
            entity.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
            RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, delta);
        }

        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity spawner, double x, double y, double z, float delta) {
        this.renderTileEntityMobSpawner((TileEntityMobSpawner) spawner, x, y, z, delta);
    }
}
