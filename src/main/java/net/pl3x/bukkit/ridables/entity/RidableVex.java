package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityVex;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.vex.AIVexChargeAttack;
import net.pl3x.bukkit.ridables.entity.ai.vex.AIVexCopyOwnerTarget;
import net.pl3x.bukkit.ridables.entity.ai.vex.AIVexMoveRandom;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlying;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableVex extends EntityVex implements RidableEntity {
    public RidableVex(World world) {
        super(world);
        moveController = new VexWASDController(this);
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
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(4, new AIVexChargeAttack(this));
        goalSelector.a(8, new AIVexMoveRandom(this));
        goalSelector.a(9, new AIWatchClosest(this, EntityHuman.class, 3.0F, 1.0F));
        goalSelector.a(10, new AIWatchClosest(this, EntityInsentient.class, 8.0F));
        targetSelector.a(1, new AIHurtByTarget(this, true, EntityVex.class));
        targetSelector.a(2, new AIVexCopyOwnerTarget(this));
        targetSelector.a(3, new AIAttackNearest<>(this, EntityHuman.class, true));
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

    static class VexWASDController extends ControllerWASDFlying {
        private final RidableVex vex;

        public VexWASDController(RidableVex vex) {
            super(vex);
            this.vex = vex;
        }

        public void tick() {
            if (this.h == ControllerMove.Operation.MOVE_TO) {
                double x = b - vex.locX;
                double y = c - vex.locY;
                double z = d - vex.locZ;
                double distance = MathHelper.sqrt(x * x + y * y + z * z);
                if (distance < vex.getBoundingBox().a()) { // getAverageEdgeLength
                    h = ControllerMove.Operation.WAIT;
                    vex.motX *= 0.5D;
                    vex.motY *= 0.5D;
                    vex.motZ *= 0.5D;
                } else {
                    vex.motX += x / distance * 0.05D * e;
                    vex.motY += y / distance * 0.05D * e;
                    vex.motZ += z / distance * 0.05D * e;
                    if (vex.getGoalTarget() == null) {
                        vex.aQ = vex.yaw = -((float) MathHelper.c(vex.motX, vex.motZ)) * (180F / (float) Math.PI);
                    } else {
                        vex.aQ = vex.yaw = -((float) MathHelper.c(vex.getGoalTarget().locX - vex.locX, vex.getGoalTarget().locZ - vex.locZ)) * (180F / (float) Math.PI);
                    }
                }
            }
        }
    }
}
