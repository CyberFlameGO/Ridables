package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityChicken;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityOcelot;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.PathfinderGoalTempt;
import net.minecraft.server.v1_13_R2.RecipeItemStack;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.OcelotConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIBreed;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIFollowOwner;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILeapAtTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISit;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AITargetNonTamed;
import net.pl3x.bukkit.ridables.entity.ai.goal.AITempt;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.ocelot.AIOcelotAttack;
import net.pl3x.bukkit.ridables.entity.ai.goal.ocelot.AIOcelotSitOnBlock;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class RidableOcelot extends EntityOcelot implements RidableEntity {
    public static final OcelotConfig CONFIG = new OcelotConfig();
    public static final RecipeItemStack TEMPTATION_ITEMS = RecipeItemStack.a(Items.COD, Items.SALMON, Items.TROPICAL_FISH, Items.PUFFERFISH);

    private static Field aiTempt;

    static {
        try {
            aiTempt = EntityOcelot.class.getDeclaredField("bK");
            aiTempt.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    private AIAvoidTarget<EntityHuman> aiAvoidEntity;

    public RidableOcelot(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.OCELOT;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !isTamed() && !hasCustomName() && !isLeashed() && ticksLived > 2400;
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
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        PathfinderGoalTempt goalTempt = new AITempt(this, 0.6D, true, TEMPTATION_ITEMS);
        try {
            aiTempt.set(this, goalTempt);
        } catch (IllegalAccessException ignore) {
        }

        goalSit = new AISit(this);

        goalSelector.a(1, new AISwim(this));
        goalSelector.a(2, goalSit);
        goalSelector.a(3, goalTempt);
        goalSelector.a(5, new AIFollowOwner(this, 1.0D, 10.0F, 5.0F));
        goalSelector.a(6, new AIOcelotSitOnBlock(this, 0.8D));
        goalSelector.a(7, new AILeapAtTarget(this, 0.3F));
        goalSelector.a(8, new AIOcelotAttack(this));
        goalSelector.a(9, new AIBreed(this, 0.8D, EntityChicken.class));
        goalSelector.a(10, new AIWanderAvoidWater(this, 0.8D, 0.00001F));
        goalSelector.a(11, new AIWatchClosest(this, EntityHuman.class, 10.0F));
        targetSelector.a(1, new AITargetNonTamed<>(this, EntityChicken.class, false, null));
        targetSelector.a(1, new AITargetNonTamed<>(this, EntityTurtle.class, false, EntityTurtle.bC));
    }

    // setupTamedAI
    @Override
    protected void dz() {
        if (aiAvoidEntity == null) {
            aiAvoidEntity = new AIAvoidTarget<>(this, EntityHuman.class, 16.0F, 0.8D, 1.33D);
        }
        goalSelector.a(aiAvoidEntity); // removeTask
        if (!isTamed()) {
            goalSelector.a(4, aiAvoidEntity); // addTask
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

    @Override
    public void mobTick() {
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
    public boolean removePassenger(Entity passenger) {
        return (!(passenger instanceof Player) || passengers.isEmpty() || !passenger.equals(passengers.get(0))
                || new RidableDismountEvent(this, (Player) passenger).callEvent()) && super.removePassenger(passenger);
    }
}
