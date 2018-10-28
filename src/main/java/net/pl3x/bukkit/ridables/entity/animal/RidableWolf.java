package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityAnimal;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLlama;
import net.minecraft.server.v1_13_R2.EntityRabbit;
import net.minecraft.server.v1_13_R2.EntitySheep;
import net.minecraft.server.v1_13_R2.EntitySkeletonAbstract;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EntityWolf;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.WolfConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIBreed;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIFollowOwner;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILeapAtTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIOwnerHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIOwnerHurtTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISit;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AITargetNonTamed;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.wolf.AIWolfAvoidEntity;
import net.pl3x.bukkit.ridables.entity.ai.goal.wolf.AIWolfBeg;

public class RidableWolf extends EntityWolf implements RidableEntity {
    public static final WolfConfig CONFIG = new WolfConfig();

    public RidableWolf(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.WOLF;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSit = new AISit(this);

        goalSelector.a(1, new AISwim(this));
        goalSelector.a(2, goalSit);
        goalSelector.a(3, new AIWolfAvoidEntity<>(this, EntityLlama.class, 24.0F, 1.5D, 1.5D));
        goalSelector.a(4, new AILeapAtTarget(this, 0.4F));
        goalSelector.a(5, new AIAttackMelee(this, 1.0D, true));
        goalSelector.a(6, new AIFollowOwner(this, 1.0D, 10.0F, 2.0F));
        goalSelector.a(7, new AIBreed(this, 1.0D, EntityWolf.class));
        goalSelector.a(8, new AIWanderAvoidWater(this, 1.0D));
        goalSelector.a(9, new AIWolfBeg(this, 8.0F));
        goalSelector.a(10, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(10, new AILookIdle(this));
        targetSelector.a(1, new AIOwnerHurtByTarget(this));
        targetSelector.a(2, new AIOwnerHurtTarget(this));
        targetSelector.a(3, new AIHurtByTarget(this, true));
        targetSelector.a(4, new AITargetNonTamed<>(this, EntityAnimal.class, false,
                (target) -> target instanceof EntitySheep || target instanceof EntityRabbit));
        targetSelector.a(4, new AITargetNonTamed<>(this, EntityTurtle.class, false, EntityTurtle.bC));
        targetSelector.a(5, new AIAttackNearest<>(this, EntitySkeletonAbstract.class, false));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return getRider() == null ? super.cG() : CONFIG.JUMP_POWER;
    }

    protected void mobTick() {
        Q = CONFIG.STEP_HEIGHT;
        super.mobTick();
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    public RidableWolf createChild(EntityAgeable entity) {
        return b(entity);
    }

    // createChild (bukkit's weird duplicate method)
    public RidableWolf b(EntityAgeable entity) {
        return new RidableWolf(world);
    }
}