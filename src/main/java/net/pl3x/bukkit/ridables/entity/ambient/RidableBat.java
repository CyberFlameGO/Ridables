package net.pl3x.bukkit.ridables.entity.ambient;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityBat;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.BatConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASDFlyingWithSpacebar;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;

public class RidableBat extends EntityBat implements RidableEntity {
    public static final BatConfig CONFIG = new BatConfig();

    public RidableBat(World world) {
        super(world);
        moveController = new ControllerWASDFlyingWithSpacebar(this, 0.2D);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.BAT;
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDE_SPEED); // registerAttribute
        reloadAttributes();
    }

    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDE_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
    }

    // initAI - override vanilla AI
    protected void n() {
        // bat AI is in mobTick()
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    protected void mobTick() {
        if (getRider() != null) {
            motY += bi > 0 ? 0.07D * CONFIG.RIDING_VERTICAL : 0.04704D - CONFIG.RIDING_GRAVITY; // moveVertical
            return;
        }
        super.mobTick(); // <- bat AI here instead of PathfinderGoals
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
