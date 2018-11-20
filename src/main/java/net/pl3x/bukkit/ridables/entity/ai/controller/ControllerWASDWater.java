package net.pl3x.bukkit.ridables.entity.ai.controller;

import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.event.RidableSpacebarEvent;
import org.bukkit.Bukkit;

public class ControllerWASDWater extends ControllerWASD {
    public ControllerWASDWater(RidableEntity entity) {
        super(entity);
    }

    @Override
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

        float speed = (float) (e = ((EntityInsentient) ridable).getAttributeInstance(RidableType.RIDING_SPEED).getValue());

        a.o(speed * 0.1F);
        a.s(vertical * 1.5F * speed);
        a.t(strafe * speed);
        a.r(forward * speed);

        f = a.bj;
        g = a.bh;
    }
}
