package net.pl3x.bukkit.ridables.entity.ai.controller;

import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.event.RidableSpacebarEvent;
import org.bukkit.Bukkit;

public class ControllerWASDFlying extends ControllerWASD {
    protected final double groundSpeedModifier;

    public ControllerWASDFlying(RidableEntity entity) {
        this(entity, 1D);
    }

    public ControllerWASDFlying(RidableEntity entity, double groundSpeedModifier) {
        super(entity);
        this.groundSpeedModifier = groundSpeedModifier;
    }

    @Override
    public void tick(EntityPlayer rider) {
        float forward = Math.max(0, rider.bj);
        float vertical = forward == 0 ? 0 : -(rider.pitch / 45);
        float strafe = rider.bh;

        if (isJumping(rider)) {
            RidableSpacebarEvent event = new RidableSpacebarEvent(ridable);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled() && !event.isHandled()) {
                ridable.onSpacebar();
            }
        }

        if (a.locY >= ((EntityInsentient) ridable).getAttributeInstance(RidableType.RIDING_MAX_Y).getValue()) {
            a.motY = -0.05F;
            vertical = 0;
            forward = 0;
            strafe = 0;
        }

        e = ((EntityInsentient) ridable).getAttributeInstance(RidableType.RIDING_SPEED).getValue();

        a.setNoGravity(forward > 0);
        a.o((float) e); // speed
        a.s(vertical);
        a.t(strafe);
        a.r(forward);

        f = a.bj;
        g = a.bh;
    }
}
