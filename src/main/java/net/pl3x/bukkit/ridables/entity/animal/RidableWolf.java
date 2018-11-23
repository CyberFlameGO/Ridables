package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAnimal;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLlama;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityRabbit;
import net.minecraft.server.v1_13_R2.EntitySheep;
import net.minecraft.server.v1_13_R2.EntitySkeletonAbstract;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EntityWolf;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
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
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;

public class RidableWolf extends EntityWolf implements RidableEntity {
    public static final WolfConfig CONFIG = new WolfConfig();

    public RidableWolf(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.WOLF;
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        reloadAttributes();
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
        if (isTamed()) {
            getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH_TAMED);
            getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_MELEE_DAMAGE_TAMED);
        } else {
            getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH_UNTAMED);
            getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_MELEE_DAMAGE_UNTAMED);
        }
    }

    // initAI - override vanilla AI
    @Override
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
    @Override
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return getRider() == null ? CONFIG.AI_JUMP_POWER : CONFIG.RIDING_JUMP_POWER;
    }

    @Override
    protected void mobTick() {
        Q = getRider() == null ? CONFIG.AI_STEP_HEIGHT : CONFIG.RIDING_STEP_HEIGHT;
        super.mobTick();
    }

    // travel
    @Override
    public void a(float strafe, float vertical, float forward) {
        super.a(strafe, vertical, forward);
        checkMove();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            if (!CONFIG.RIDING_BABIES && isBaby()) {
                return false; // do not ride babies
            }
            if (CONFIG.RIDING_ONLY_OWNER_CAN_RIDE && isTamed() && getOwner() != entityhuman) {
                return false; // only owner can ride
            }
            return tryRide(entityhuman, CONFIG.RIDING_SADDLE_REQUIRE, CONFIG.RIDING_SADDLE_CONSUME);
        }
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger, boolean notCancellable) {
        if (passenger instanceof EntityPlayer && !passengers.isEmpty() && passenger == passengers.get(0)) {
            if (!new RidableDismountEvent(this, (Player) passenger.getBukkitEntity(), notCancellable).callEvent() && !notCancellable) {
                return false; // cancelled
            }
        }
        if (super.removePassenger(passenger, notCancellable)) {
            passenger.locY += 0.5; // dont let rider get stuck in a block
            return true;
        }
        return false;
    }

    @Override
    public void setTamed(boolean tamed) {
        super.setTamed(tamed);
        if (isTamed()) {
            getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH_TAMED);
            getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_MELEE_DAMAGE_TAMED);
        } else {
            getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH_UNTAMED);
            getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_MELEE_DAMAGE_UNTAMED);
        }
    }
}
