package net.pl3x.bukkit.ridables.entity.controller;

import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.event.RidableSpacebarEvent;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public class ControllerWASD extends ControllerMove {
    protected final RidableEntity ridable;
    public EntityPlayer rider;
    public boolean override;

    public ControllerWASD(RidableEntity ridable) {
        super((EntityInsentient) ridable);
        this.ridable = ridable;
    }

    EntityPlayer updateRider() {
        if (a.passengers.isEmpty()) {
            return rider = null;
        }
        Entity entity = a.passengers.get(0);
        return rider = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
    }

    // isUpdating
    public boolean b() {
        return rider != null || super.b();
    }

    // tick
    public void a() {
        if (updateRider() != null && !override) {
            tick(rider);
        } else {
            tick();
        }
    }

    public void tick() {
        super_tick();
    }

    public void super_tick() {
        super.a();
    }

    public void tick(EntityPlayer rider) {
        float forward = rider.bj * 0.5F;
        float strafe = rider.bh * 0.25F;
        if (forward <= 0.0F) {
            forward *= 0.5F;
        }

        float yaw = rider.yaw;
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
        ((LookController) a.getControllerLook()).setOffsets(yaw - rider.yaw, 0);

        if (isJumping(rider)) {
            RidableSpacebarEvent event = new RidableSpacebarEvent(ridable);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled() && !event.isHandled() && !ridable.onSpacebar() && a.onGround) {
                a.getControllerJump().a();
            }
        }

        a.o((float) (e = a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * ridable.getSpeed()));
        a.r(forward);

        f = a.bj; // forward
        g = a.bh; // strafe
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
     * Reset the jump flag for an entity
     *
     * @param entity Entity to reset
     */
    public static void resetJumping(EntityLiving entity) {
        try {
            jumping.set(entity, false);
        } catch (IllegalAccessException ignore) {
        }
    }
}
