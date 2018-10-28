package net.pl3x.bukkit.ridables.entity.ai.controller;

import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.event.RidableSpacebarEvent;

public class ControllerWASDFlyingWithSpacebar extends ControllerWASDFlying {
    public ControllerWASDFlyingWithSpacebar(RidableEntity entity) {
        super(entity);
    }

    public ControllerWASDFlyingWithSpacebar(RidableEntity entity, double groundSpeedModifier) {
        super(entity, groundSpeedModifier);
    }

    @Override
    public void tick(EntityPlayer rider) {
        float forward = rider.bj * 0.5F;
        float strafe = rider.bh * 0.25F;
        float vertical = 0;

        if (forward <= 0.0F) {
            forward *= 0.5F;
        }

        float speed = (float) (((EntityInsentient) ridable).getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * ((EntityInsentient) ridable).getAttributeInstance(RidableType.RIDING_SPEED).getValue());

        RidableSpacebarEvent event = new RidableSpacebarEvent(ridable);
        if (isJumping(rider) && event.callEvent() && !event.isHandled() && !ridable.onSpacebar()) {
            a.setNoGravity(true);
            vertical = speed;
        } else {
            a.setNoGravity(false);
        }

        if (a.locY >= ((EntityInsentient) ridable).getAttributeInstance(RidableType.RIDING_MAX_Y).getValue()) {
            a.motY -= 0.2D;
            vertical = -speed;
            forward = 0;
            strafe = 0;
        }

        if (a.onGround) {
            speed *= groundSpeedModifier;
        }

        a.o((float) (e = speed));
        a.s(vertical);
        a.t(strafe);
        a.r(forward);

        f = a.bj;
        g = a.bh;

        if (a.motY > 0D) {
            a.motY *= 0.8D;
        }
        if (a.motY > 0.2D) {
            a.motY = 0.2D;
        }
    }
}
