package net.pl3x.bukkit.ridables.entity.controller;

import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.EntityInsentient;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

import java.lang.reflect.Field;

public class ControllerWASD extends ControllerMove {
    protected final RidableEntity ridable;

    public ControllerWASD(EntityInsentient entity) {
        super(entity);
        ridable = ((RidableEntity) entity);
    }

    // isUpdating
    public boolean b() {
        return f != 0 || g != 0;
    }

    // onUpdate
    public void a() {
        EntityPlayer rider = ridable.getRider();
        if (rider == null) {
            ridable.useAIController();
            return;
        }

        // do not target anything while being ridden
        a.setGoalTarget(null, null, false);

        // controls
        float forward = rider.bj * 0.5F;
        float strafe = rider.bh * 0.25F;
        if (forward <= 0.0F) {
            forward *= 0.5F;
        }

        // rotation
        float yaw = rider.yaw;
        if (Config.USE_NEW_WASD_YAW_CALCULATIONS) {
            if (strafe != 0) {
                if (forward == 0) {
                    yaw += strafe > 0 ? -90 : 90;
                    forward = Math.abs(strafe * 2);
                } else {
                    yaw += strafe > 0 ? -30 : 30;
                    strafe /= 2;
                    if (forward < 0) {
                        yaw += strafe > 0 ? -110 : 110;
                        forward *= -1;
                    }
                }
            } else if (forward < 0) {
                yaw -= 180;
                forward *= -1;
            }
        }
        ridable.setRotation(yaw, rider.pitch);

        // jump
        if (isJumping(rider) && !ridable.onSpacebar() && a.onGround && ridable.getJumpPower() > 0) {
            a.getControllerJump().a();
        }

        a.o((float) (e = ridable.getSpeed()));
        if (!Config.USE_NEW_WASD_YAW_CALCULATIONS) {
            a.t(strafe);
        }
        a.r(forward);

        f = a.bj;
        g = a.bh;
    }

    private static Field jumping;

    static {
        try {
            jumping = EntityLiving.class.getDeclaredField("bg");
            jumping.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if entity has their jump flag toggled on
     * <p>
     * This is true for players when they are pressing the spacebar
     *
     * @param entity Living entity to check
     * @return True if jump flag is toggled on
     */
    public static boolean isJumping(EntityLiving entity) {
        try {
            return jumping.getBoolean(entity);
        } catch (IllegalAccessException ignore) {
            return false;
        }
    }

    /**
     * Set the jump flag for an entity
     *
     * @param entity Entity to set
     */
    public static void setJumping(EntityLiving entity) {
        try {
            jumping.set(entity, false);
        } catch (IllegalAccessException ignore) {
        }
    }
}
