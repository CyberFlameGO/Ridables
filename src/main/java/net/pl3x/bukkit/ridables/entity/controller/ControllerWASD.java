package net.pl3x.bukkit.ridables.entity.controller;

import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.EntityInsentient;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.util.ReflectionUtil;

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

        // eject rider if in water or lava
        if (a.isInWater() || a.ax()) {
            a.ejectPassengers();
            rider.stopRiding();
            return;
        }

        // rotation
        ridable.setRotation(rider.yaw, rider.pitch);

        // controls
        float forward = rider.bj * 0.5F;
        float strafe = rider.bh * 0.25F;
        if (forward <= 0.0F) {
            forward *= 0.5F;
        }

        // jump
        if (ReflectionUtil.isJumping(rider) && !ridable.onSpacebar() && a.onGround && ridable.getJumpPower() > 0) {
            a.getControllerJump().a();
        }

        a.o((float) (e = ridable.getSpeed()));
        a.t(strafe);
        a.r(forward);

        f = a.bj;
        g = a.bh;
    }
}
