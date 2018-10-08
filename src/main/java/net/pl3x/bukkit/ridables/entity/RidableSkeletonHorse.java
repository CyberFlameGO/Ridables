package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHorseSkeleton;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;

public class RidableSkeletonHorse extends EntityHorseSkeleton implements RidableEntity {
    public RidableSkeletonHorse(World world) {
        super(world);
        initAI();
    }

    public RidableType getType() {
        return RidableType.SKELETON_HORSE;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.SKELETON_HORSE_RIDABLE_IN_WATER;
    }

    public void mobTick() {
        Q = Config.SKELETON_HORSE_STEP_HEIGHT;
        super.mobTick();
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

    public boolean isTamed() {
        return true;
    }

    // addSwimmingPathfinder
    protected void dI() {
        goalSelector.a(0, new AISwim(this));
    }
}
