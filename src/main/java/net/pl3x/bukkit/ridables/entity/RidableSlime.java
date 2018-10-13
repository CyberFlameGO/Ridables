package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntitySlime;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIFindNearestEntity;
import net.pl3x.bukkit.ridables.entity.ai.AIFindNearestPlayer;
import net.pl3x.bukkit.ridables.entity.ai.slime.AISlimeAttack;
import net.pl3x.bukkit.ridables.entity.ai.slime.AISlimeFaceRandom;
import net.pl3x.bukkit.ridables.entity.ai.slime.AISlimeHop;
import net.pl3x.bukkit.ridables.entity.ai.slime.AISlimeSwim;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableSlime extends EntitySlime implements RidableEntity {
    private int spacebarCharge = 0;
    private int prevSpacebarCharge = 0;
    private float fallDistanceCharge = 0;

    public RidableSlime(World world) {
        this(EntityTypes.SLIME, world);
    }

    public RidableSlime(EntityTypes entityTypes, World world) {
        super(entityTypes, world);
        moveController = new SlimeWASDController(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.SLIME;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(1, new AISlimeSwim(this));
        goalSelector.a(2, new AISlimeAttack(this));
        goalSelector.a(3, new AISlimeFaceRandom(this));
        goalSelector.a(5, new AISlimeHop(this));
        targetSelector.a(1, new AIFindNearestPlayer(this));
        targetSelector.a(3, new AIFindNearestEntity(this, EntityIronGolem.class));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.SLIME_RIDABLE_IN_WATER;
    }

    public boolean canDamagePlayer() {
        return dt();
    }

    protected void mobTick() {
        if (spacebarCharge == prevSpacebarCharge) {
            spacebarCharge = 0;
        }
        prevSpacebarCharge = spacebarCharge;
        super.mobTick();
    }

    public float getJumpCharge() {
        float charge = 1F;
        if (getRider() != null && spacebarCharge > 0) {
            charge += 1F * (fallDistanceCharge = (spacebarCharge / 72F));
        } else {
            fallDistanceCharge = 0;
        }
        return charge;
    }

    // jump
    protected void cH() {
        motY = 0.42D * getJumpCharge();
        impulse = true;
    }

    // fall
    public void c(float distance, float damageMultiplier) {
        if (getRider() != null && fallDistanceCharge > 0) {
            distance = distance - fallDistanceCharge;
        }
        super.c(distance, damageMultiplier);
    }

    public float getSpeed() {
        return Config.SLIME_SPEED;
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

    public boolean onSpacebar() {
        if (hasSpecialPerm(getRider().getBukkitEntity())) {
            spacebarCharge++;
            if (spacebarCharge > 50) {
                spacebarCharge -= 2;
            }
        }
        return false;
    }

    public static class SlimeWASDController extends ControllerWASD {
        private final RidableSlime slime;
        private float yRot;
        private int jumpDelay;
        private boolean isAggressive;

        public SlimeWASDController(RidableSlime slime) {
            super(slime);
            this.slime = slime;
            yRot = 180.0F * slime.yaw / (float) Math.PI;
        }

        public void setDirection(float yRot, boolean isAggressive) {
            this.yRot = yRot;
            this.isAggressive = isAggressive;
        }

        public void setSpeed(double speed) {
            e = speed;
            h = ControllerMove.Operation.MOVE_TO;
        }

        public void tick() {
            slime.aQ = slime.aS = slime.yaw = a(slime.yaw, yRot, 90.0F);
            if (h != ControllerMove.Operation.MOVE_TO) {
                slime.r(0.0F); // forward
                return;
            }
            h = ControllerMove.Operation.WAIT;
            if (slime.onGround) {
                slime.o((float) (e * a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
                if (jumpDelay-- <= 0) {
                    jumpDelay = slime.dr(); // getJumpDelay
                    if (isAggressive) {
                        jumpDelay /= 3;
                    }
                    slime.getControllerJump().a(); // setJumping
                    if (slime.dz()) { // makeSoundOnJump
                        slime.a(slime.dw(), slime.cD(), ((slime.getRandom().nextFloat() - slime.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F); // playSound
                    }
                } else {
                    slime.bh = 0.0F; // moveStrafing
                    slime.bj = 0.0F; // moveForward
                    slime.o(0.0F); // setSpeed
                }
                return;
            }
            slime.o((float) (e * a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
        }
    }
}
