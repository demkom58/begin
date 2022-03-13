package net.hypnosis.entity;

import net.hypnosis.util.math.Position;
import net.hypnosis.util.math.Vec3d;

public interface Rotatable extends Position {

    void setRotation(float yaw, float pitch);

    float getYaw();

    void setYaw(float yaw);

    float getPitch();

    void setPitch(float pitch);

    Vec3d getRotationVec(float tickDelta);

    float getPitch(float tickDelta);

    float getYaw(float tickDelta);
}
