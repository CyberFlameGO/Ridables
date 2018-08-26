package net.pl3x.bukkit.ridables.entity.controller;

import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;

public class ControllerWASDWater extends ControllerWASD {
    public ControllerWASDWater(EntityInsentient entity) {
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
        float forward = rider.bj;
        float strafe = rider.bh;
        float vertical = -(rider.pitch / 90);
        if (forward < 0.0F) {
            forward *= 0.25F;
            vertical = -vertical * 0.1F;
            strafe *= 0.25F;
        } else if (forward == 0) {
            vertical = 0F;
        }

        // jump
        if (isJumping(rider)) {
            ridable.onSpacebar();
        }

        float speed = ridable.getSpeed();

        a.o(speed);
        a.s(vertical * speed * 1.5F);
        a.t(strafe * speed);
        a.r(forward * speed);

        f = a.bj;
        g = a.bh;
    }
}
