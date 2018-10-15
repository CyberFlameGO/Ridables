package net.pl3x.bukkit.ridables.entity.controller;

import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.event.RidableSpacebarEvent;
import org.bukkit.Bukkit;

public class ControllerWASDWater extends ControllerWASD {
    public ControllerWASDWater(RidableEntity entity) {
        super(entity);
    }

    public void tick(EntityPlayer rider) {
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

        if (isJumping(rider)) {
            RidableSpacebarEvent event = new RidableSpacebarEvent(ridable);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled() && !event.isHandled()) {
                ridable.onSpacebar();
            }
        }

        a.o((float) (e = ridable.getSpeed()));
        a.s(vertical * 1.5F);
        a.t(strafe);
        a.r(forward);

        f = a.bj;
        g = a.bh;
    }
}
