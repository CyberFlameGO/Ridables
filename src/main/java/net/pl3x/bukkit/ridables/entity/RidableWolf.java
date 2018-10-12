package net.pl3x.bukkit.ridables.entity;

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
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIBreed;
import net.pl3x.bukkit.ridables.entity.ai.AIFollowOwner;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILeapAtTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIMeleeAttack;
import net.pl3x.bukkit.ridables.entity.ai.AIOwnerHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIOwnerHurtTarget;
import net.pl3x.bukkit.ridables.entity.ai.AISit;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AITargetNonTamed;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.wolf.AIWolfAvoidEntity;
import net.pl3x.bukkit.ridables.entity.ai.wolf.AIWolfBeg;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableWolf extends EntityWolf implements RidableEntity {
    public RidableWolf(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.WOLF;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        goalSit = new AISit(this);

        goalSelector.a(1, new AISwim(this));
        goalSelector.a(2, goalSit);
        goalSelector.a(3, new AIWolfAvoidEntity<>(this, EntityLlama.class, 24.0F, 1.5D, 1.5D));
        goalSelector.a(4, new AILeapAtTarget(this, 0.4F));
        goalSelector.a(5, new AIMeleeAttack(this, 1.0D, true));
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
        return Config.WOLF_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.WOLF_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.WOLF_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.WOLF_SPEED;
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

    public RidableWolf createChild(EntityAgeable entity) {
        return b(entity);
    }

    // createChild (bukkit's weird duplicate method)
    public RidableWolf b(EntityAgeable entity) {
        return new RidableWolf(world);
    }
}
