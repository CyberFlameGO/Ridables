package net.pl3x.bukkit.ridables.entity.controller;

import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.event.RidableSpacebarEvent;
import org.bukkit.Bukkit;

public class ControllerWASDFlyingWithSpacebar extends ControllerWASD {
    public ControllerWASDFlyingWithSpacebar(RidableEntity entity) {
        super(entity);
    }

    @Override
    public void tick(EntityPlayer rider) {
        float forward = rider.bj * 0.5F;
        float strafe = rider.bh * 0.25F;
        float vertical = 0;

        if (forward <= 0.0F) {
            forward *= 0.5F;
        }

        float speed = (float) (((EntityInsentient) ridable).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * ((EntityInsentient) ridable).getAttributeInstance(RidableType.RIDE_SPEED).getValue());

        if (isJumping(rider)) {
            RidableSpacebarEvent event = new RidableSpacebarEvent(ridable);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled() && !event.isHandled() && !ridable.onSpacebar()) {
                a.setNoGravity(true);
                vertical = speed;
            } else {
                a.setNoGravity(false);
            }
        } else {
            a.setNoGravity(false);
        }

        if (a.locY >= Config.FLYING_MAX_Y) {
            vertical = 0;
            forward = 0;
            strafe = 0;
        }

        a.o((float) (e = speed * (a.onGround ? 0.1F : 2)));
        a.s(vertical);
        a.t(strafe);
        a.r(forward);

        f = a.bj;
        g = a.bh;
    }
}
