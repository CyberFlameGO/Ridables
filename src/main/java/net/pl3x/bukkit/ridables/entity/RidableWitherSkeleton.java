package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntitySkeletonAbstract;
import net.minecraft.server.v1_13_R2.EntitySkeletonWither;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EntityWolf;
import net.minecraft.server.v1_13_R2.EnumDifficulty;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.PathfinderGoalBowShoot;
import net.minecraft.server.v1_13_R2.PathfinderGoalMeleeAttack;
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

public class RidableWitherSkeleton extends EntitySkeletonWither implements RidableEntity {
    private final PathfinderGoalBowShoot<EntitySkeletonAbstract> aiArrowAttack = new AIShootBow<>(this, 1.0D, 20, 15.0F);
    private final PathfinderGoalMeleeAttack aiMeleeAttack = new AISkeletonMeleeAttack(this, 1.2D, false);

    public RidableWitherSkeleton(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.WITHER_SKELETON;
    }

    // initAI - override vanilla AI
    protected void n() {
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
        return Config.WITHER_SKELETON_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.WITHER_SKELETON_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.WITHER_SKELETON_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.WITHER_SKELETON_SPEED;
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

    // setCombatTask
    public void dz() {
        if (world != null) {
            goalSelector.a(aiMeleeAttack); // removeTask
            goalSelector.a(aiArrowAttack); // removeTask
            if (getItemInMainHand().getItem() == Items.BOW) {
                aiArrowAttack.b(world.getDifficulty() == EnumDifficulty.HARD ? 40 : 20); // setAttackCooldown
                goalSelector.a(4, aiArrowAttack); // addTask
            } else {
                goalSelector.a(4, aiMeleeAttack); // addTask
            }
        }
    }
}
