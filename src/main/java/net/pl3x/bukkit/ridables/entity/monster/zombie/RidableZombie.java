package net.pl3x.bukkit.ridables.entity.monster.zombie;

import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntityPigZombie;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EntityVillager;
import net.minecraft.server.v1_13_R2.EntityZombie;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.Navigation;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.ZombieConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveThroughVillage;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.AIZombieAttack;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.AIZombieAttackTurtleEgg;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.AIZombieBreakDoor;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class RidableZombie extends EntityZombie implements RidableEntity {
    public static final ZombieConfig CONFIG = new ZombieConfig();

    private static Field isBreakDoorsTaskSet;

    static {
        try {
            isBreakDoorsTaskSet = EntityZombie.class.getDeclaredField("bH");
            isBreakDoorsTaskSet.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    public static void setBreakDoorsTask(Entity entity, boolean enabled) {
        try {
            isBreakDoorsTaskSet.setBoolean(entity, enabled);
        } catch (IllegalAccessException ignore) {
        }
    }

    private final AIZombieBreakDoor breakDoorAI;

    public RidableZombie(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        breakDoorAI = new AIZombieBreakDoor(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.ZOMBIE;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
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
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_MELEE_DAMAGE);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
        getAttributeInstance(GenericAttributes.h).setValue(CONFIG.AI_ARMOR); // ARMOR
        getAttributeInstance(c).setValue(random.nextDouble() * CONFIG.AI_SPAWN_REINFORCEMENTS_CHANCE); // SPAWN_REINFORCEMENTS_CHANCE
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        // from EntityZombie
        goalSelector.a(4, new AIZombieAttackTurtleEgg(Blocks.TURTLE_EGG, this, 1.0D, 3));
        goalSelector.a(5, new AIMoveTowardsRestriction(this, 1.0D));
        goalSelector.a(8, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(8, new AILookIdle(this));

        // also from EntityZombie
        goalSelector.a(2, new AIZombieAttack(this, 1.0D, false));
        goalSelector.a(6, new AIMoveThroughVillage(this, 1.0D, false));
        goalSelector.a(7, new AIWanderAvoidWater(this, 1.0D));
        targetSelector.a(1, new AIHurtByTarget(this, true, EntityPigZombie.class));
        targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
        if (world.spigotConfig.zombieAggressiveTowardsVillager) {
            targetSelector.a(3, new AIAttackNearest<>(this, EntityVillager.class, false));
        }
        targetSelector.a(3, new AIAttackNearest<>(this, EntityIronGolem.class, true));
        targetSelector.a(5, new AIAttackNearest<>(this, EntityTurtle.class, 10, true, false, EntityTurtle.bC));
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

    // setBreakDoorsAITask
    @Override
    public void t(boolean enabled) {
        if (dz()) { // canBreakDoors
            if (dH() != enabled) { // isBreakDoorsTaskSet
                setBreakDoorsTask(this, enabled);
                ((Navigation) this.getNavigation()).a(enabled); // setBreakDoors
                if (enabled) {
                    goalSelector.a(1, breakDoorAI); // addTask
                } else {
                    goalSelector.a(breakDoorAI); // removeTask
                }
            }
        } else if (dH()) { // isBreakDoorsTaskSet
            goalSelector.a(breakDoorAI); // removeTask
            setBreakDoorsTask(this, false);
        }
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
        return super.removePassenger(passenger, notCancellable);
    }
}
