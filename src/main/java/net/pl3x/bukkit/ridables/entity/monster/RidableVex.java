package net.pl3x.bukkit.ridables.entity.monster;

import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityVex;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.VexConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.vex.AIVexChargeAttack;
import net.pl3x.bukkit.ridables.entity.ai.goal.vex.AIVexCopyOwnerTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.vex.AIVexMoveRandom;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASDFlying;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;

public class RidableVex extends EntityVex implements RidableEntity {
    public static final VexConfig CONFIG = new VexConfig();

    public RidableVex(World world) {
        super(world);
        moveController = new VexWASDController(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.VEX;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
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
    @Override
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    @Override
    public void k() {
        noclip = CONFIG.NO_CLIP;
        super.k();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    @Override
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    // fall
    @Override
    public void c(float f, float f1) {
        // no fall damage
    }

    static class VexWASDController extends ControllerWASDFlying {
        private final RidableVex vex;

        public VexWASDController(RidableVex vex) {
            super(vex);
            this.vex = vex;
        }

        @Override
        public void tick(EntityPlayer rider) {
            super.tick(rider);
            vex.noclip = CONFIG.NO_CLIP;
        }

        @Override
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
