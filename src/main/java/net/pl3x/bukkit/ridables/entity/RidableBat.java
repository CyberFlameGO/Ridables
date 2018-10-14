package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityBat;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.BatConfig;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlyingWithSpacebar;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableBat extends EntityBat implements RidableEntity {
    public static final BatConfig CONFIG = new BatConfig();

    public RidableBat(World world) {
        super(world);
        moveController = new ControllerWASDFlyingWithSpacebar(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.BAT;
    }

    // initAI - override vanilla AI
    protected void n() {
        // bat AI is in mobTick()
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    protected void mobTick() {
        if (getRider() != null) {
            motY += bi > 0 ? 0.07F * CONFIG.VERTICAL : 0.04704F - CONFIG.GRAVITY;
            return;
        }
        super.mobTick(); // <- bat AI here instead of PathfinderGoals
    }

    public float getSpeed() {
        return CONFIG.SPEED;
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }
}
