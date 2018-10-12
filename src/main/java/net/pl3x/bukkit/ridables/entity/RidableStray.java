package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntitySkeletonStray;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EntityWolf;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIFleeSun;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIRestrictSun;
import net.pl3x.bukkit.ridables.entity.ai.AIShootBow;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.skeleton.AISkeletonMeleeAttack;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableStray extends EntitySkeletonStray implements RidableEntity {
    public RidableStray(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);

        try {
            // remove default combat ai tasks
            goalSelector.a((PathfinderGoal) RidableSkeleton.aiShootBow.get(this));
            goalSelector.a((PathfinderGoal) RidableSkeleton.aiMeleeAttack.get(this));

            // add new combat ai tasks
            RidableSkeleton.aiShootBow.set(this, new AIShootBow<>(this, 1.0D, 20, 15.0F));
            RidableSkeleton.aiMeleeAttack.set(this, new AISkeletonMeleeAttack(this, 1.2D, false));
        } catch (IllegalAccessException ignore) {
        }

        initAI();
        dz(); // setCombatTask
    }

    public RidableType getType() {
        return RidableType.STRAY;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        // from EntitySkeletonAbstract
        goalSelector.a(2, new AIRestrictSun(this));
        goalSelector.a(3, new AIFleeSun(this, 1.0D));
        goalSelector.a(3, new AIAvoidTarget<>(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
        goalSelector.a(5, new AIWanderAvoidWater(this, 1.0D));
        goalSelector.a(6, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(6, new AILookIdle(this));
        targetSelector.a(1, new AIHurtByTarget(this, false));
        targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
        targetSelector.a(3, new AIAttackNearest<>(this, EntityIronGolem.class, true));
        targetSelector.a(3, new AIAttackNearest<>(this, EntityTurtle.class, 10, true, false, EntityTurtle.bC));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.STRAY_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.STRAY_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.STRAY_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.STRAY_SPEED;
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
