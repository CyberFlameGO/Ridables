package net.pl3x.bukkit.ridables.entity.monster.skeleton;

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
import net.pl3x.bukkit.ridables.configuration.mob.WitherSkeletonConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIFleeSun;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIRestrictSun;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIShootBow;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.skeleton.AISkeletonMeleeAttack;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;

public class RidableWitherSkeleton extends EntitySkeletonWither implements RidableEntity {
    public static final WitherSkeletonConfig CONFIG = new WitherSkeletonConfig();

    private final PathfinderGoalBowShoot<EntitySkeletonAbstract> aiArrowAttack = new AIShootBow<>(this, 1.0D, 20, 15.0F);
    private final PathfinderGoalMeleeAttack aiMeleeAttack = new AISkeletonMeleeAttack(this, 1.2D, false);

    public RidableWitherSkeleton(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.WITHER_SKELETON;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
    }

    // initAI - override vanilla AI
    @Override
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
    @Override
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return getRider() == null ? super.cG() : CONFIG.JUMP_POWER;
    }

    @Override
    protected void mobTick() {
        Q = CONFIG.STEP_HEIGHT;
        super.mobTick();
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

    // setCombatTask
    @Override
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
