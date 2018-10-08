package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityBat;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlyingWithSpacebar;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableBat extends EntityBat implements RidableEntity {
    public RidableBat(World world) {
        super(world);
        moveController = new ControllerWASDFlyingWithSpacebar(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.BAT;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        // bat AI is in mobTick()
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.BAT_RIDABLE_IN_WATER;
    }

    protected void mobTick() {
        if (getRider() != null) {
            motY += bi > 0 ? 0.07F * Config.BAT_VERTICAL : 0.04704F - Config.BAT_GRAVITY;
            return;
        }
        super.mobTick(); // <- bat AI here instead of PathfinderGoals
    }

    public float getSpeed() {
        return Config.BAT_SPEED;
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
}
