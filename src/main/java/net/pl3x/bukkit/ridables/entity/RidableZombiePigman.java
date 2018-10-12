package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPigZombie;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.Navigation;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.zombie.AIZombieAttack;
import net.pl3x.bukkit.ridables.entity.ai.zombie.AIZombieAttackTurtleEgg;
import net.pl3x.bukkit.ridables.entity.ai.zombie.AIZombieBreakDoor;
import net.pl3x.bukkit.ridables.entity.ai.zombie.zombie_pigman.AIPigmanHurtByAggressor;
import net.pl3x.bukkit.ridables.entity.ai.zombie.zombie_pigman.AIPigmanTargetAggressor;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.entity.PigZombieAngerEvent;

import java.lang.reflect.Field;

public class RidableZombiePigman extends EntityPigZombie implements RidableEntity {
    private static Field soundDelay;

    static {
        try {
            soundDelay = EntityPigZombie.class.getDeclaredField("soundDelay");
            soundDelay.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    private final AIZombieBreakDoor breakDoorAI;

    public RidableZombiePigman(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
        breakDoorAI = new AIZombieBreakDoor(this);
    }

    public RidableType getType() {
        return RidableType.ZOMBIE_PIGMAN;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        // from EntityZombie
        goalSelector.a(4, new AIZombieAttackTurtleEgg(Blocks.TURTLE_EGG, this, 1.0D, 3));
        goalSelector.a(5, new AIMoveTowardsRestriction(this, 1.0D));
        goalSelector.a(8, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(8, new AILookIdle(this));

        // from EntityPigZombie
        goalSelector.a(2, new AIZombieAttack(this, 1.0D, false));
        goalSelector.a(7, new AIWanderAvoidWater(this, 1.0D));
        targetSelector.a(1, new AIPigmanHurtByAggressor(this));
        targetSelector.a(2, new AIPigmanTargetAggressor(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.ZOMBIE_PIGMAN_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.ZOMBIE_PIGMAN_JUMP_POWER;
    }

    // setBreakDoorsAITask
    public void t(boolean enabled) {
        if (dz()) { // canBreakDoors
            if (dH() != enabled) {
                RidableZombie.setBreakDoorsTask(this, enabled);
                ((Navigation) this.getNavigation()).a(enabled); // setBreakDoors
                if (enabled) {
                    goalSelector.a(1, breakDoorAI); // addTask
                } else {
                    goalSelector.a(breakDoorAI); // removeTask
                }
            }
        } else if (dH()) {
            goalSelector.a(breakDoorAI); // removeTask
            RidableZombie.setBreakDoorsTask(this, false);
        }
    }

    protected void mobTick() {
        Q = Config.ZOMBIE_PIGMAN_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.ZOMBIE_PIGMAN_SPEED;
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

    public void becomeAngryAt(Entity target) {
        PigZombieAngerEvent event = new PigZombieAngerEvent((PigZombie) getBukkitEntity(), (target == null) ? null : target.getBukkitEntity(), 400 + random.nextInt(400));
        world.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return;
        }
        angerLevel = event.getNewAnger();
        try {
            soundDelay.setInt(this, random.nextInt(40));
        } catch (IllegalAccessException ignore) {
        }
        if (target instanceof EntityLiving) {
            a((EntityLiving) target); //setRevengeTarget
        }
    }
}
