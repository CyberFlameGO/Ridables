package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityVex;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlying;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableVex extends EntityVex implements RidableEntity {
    public RidableVex(World world) {
        super(world);
        moveController = new ControllerWASDFlying(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.VEX;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.VEX_RIDABLE_IN_WATER;
    }

    public float getSpeed() {
        return Config.VEX_SPEED;
    }

    // processInteract
    public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        if (passengers.isEmpty() && !entityhuman.isPassenger() && !entityhuman.isSneaking()) {
            return enumhand == EnumHand.MAIN_HAND && tryRide(entityhuman, entityhuman.b(enumhand));
        }
        return passengers.isEmpty() && super.a(entityhuman, enumhand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    // fall
    public void c(float f, float f1) {
        // no fall damage
    }

    class ControllerWASDVex extends ControllerWASDFlying {
        ControllerWASDVex(RidableEntity entity) {
            super(entity);
        }

        // onUpdate
        public void a() {
            EntityPlayer rider = ridable.getRider();
            if (rider == null) {
                //ridable.useAIController();
                return;
            }

            // do not target anything while being ridden
            a.setGoalTarget(null, null, false);

            // rotation
            //ridable.setRotation(rider.yaw, rider.pitch);

            // controls
            float forward = rider.bj;
            float vertical = forward == 0 ? 0 : -(rider.pitch / 45);
            float strafe = rider.bh;

            if (forward < 0) {
                forward *= 0.5;
                strafe *= 0.5;
                vertical *= -0.25;
            }

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
            if (a.locY <= 0) {
                a.motY = +0.05F;
                vertical = 0;
                forward = 0;
                strafe = 0;
            }

            a.motX *= 0.95F;
            a.motY *= 0.9F;
            a.motZ *= 0.95F;

            float speed = ridable.getSpeed();
            if (a.onGround) {
                speed *= 0.05F;
            }

            a.o((float) (e = speed));
            a.s(vertical);
            a.t(strafe);
            a.r(forward);

            f = a.bj;
            g = a.bh;

            a.noclip = Config.VEX_NOCLIP;
        }
    }
}
