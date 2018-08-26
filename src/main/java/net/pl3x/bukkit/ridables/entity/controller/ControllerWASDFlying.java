package net.pl3x.bukkit.ridables.entity.controller;

import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.pl3x.bukkit.ridables.configuration.Config;

public class ControllerWASDFlying extends ControllerWASD {
    public ControllerWASDFlying(EntityInsentient entity) {
        super(entity);
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

        // rotation
        ridable.setRotation(rider.yaw, rider.pitch);

        // controls
        float forward = Math.max(0, rider.bj);
        float vertical = forward == 0 ? 0 : -(rider.pitch / 45);
        float strafe = rider.bh;

        // jump
        if (isJumping(rider)) {
            ridable.onSpacebar();
        }

        if (a.locY >= Config.FLYING_MAX_Y) {
            a.motY = -0.05F;
            vertical = 0;
            forward = 0;
            strafe = 0;
        }

        a.setNoGravity(forward > 0);
        a.s(vertical);
        a.t(strafe);
        a.r(forward);

        f = a.bj;
        g = a.bh;
    }
}
