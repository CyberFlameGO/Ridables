package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityZombieHusk;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableHusk extends EntityZombieHusk implements RidableEntity {
    public RidableHusk(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.HUSK;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.HUSK_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.HUSK_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.HUSK_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.HUSK_SPEED;
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
