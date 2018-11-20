package net.pl3x.bukkit.ridables.entity.monster;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityGiantZombie;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntityVillager;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IWorldReader;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.GiantConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;

public class RidableGiant extends EntityGiantZombie implements RidableEntity {
    public static GiantConfig CONFIG = new GiantConfig();

    public RidableGiant(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.GIANT;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
    }

    @Override
    public void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        reloadAttributes();
        setHealth(getMaxHealth());
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        if (CONFIG.AI_ENABLED) {
            getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
            getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_MELEE_DAMAGE);
            getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
        }
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        if (CONFIG.AI_ENABLED) {
            goalSelector.a(0, new AISwim(this));
            goalSelector.a(2, new AIAttackMelee(this, 1.0D, false));
            goalSelector.a(7, new AIWanderAvoidWater(this, 1.0D));
            goalSelector.a(8, new AIWatchClosest(this, EntityHuman.class, 16.0F));
            goalSelector.a(8, new AILookIdle(this));
            targetSelector.a(1, new AIHurtByTarget(this, true, EntityHuman.class));
            if (CONFIG.AI_HOSTILE) {
                goalSelector.a(5, new AIMoveTowardsRestriction(this, 1.0D));
                targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
                targetSelector.a(3, new AIAttackNearest<>(this, EntityVillager.class, false));
                targetSelector.a(3, new AIAttackNearest<>(this, EntityIronGolem.class, true));
            }
        }
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

    // getBlockPathWeight
    public float a(BlockPosition pos, IWorldReader world) {
        return 0.5F - world.A(pos); // getBrightness
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
            return tryRide(entityhuman, CONFIG.RIDING_SADDLE_REQUIRE, CONFIG.RIDING_SADDLE_CONSUME);
        }
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        return (!(passenger instanceof Player) || passengers.isEmpty() || !passenger.equals(passengers.get(0))
                || new RidableDismountEvent(this, (Player) passenger).callEvent()) && super.removePassenger(passenger);
    }
}
