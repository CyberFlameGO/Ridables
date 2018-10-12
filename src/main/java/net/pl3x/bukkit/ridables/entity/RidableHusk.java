package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntityPigZombie;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EntityVillager;
import net.minecraft.server.v1_13_R2.EntityZombieHusk;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.Navigation;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.zombie.AIZombieAttack;
import net.pl3x.bukkit.ridables.entity.ai.zombie.AIZombieAttackTurtleEgg;
import net.pl3x.bukkit.ridables.entity.ai.zombie.AIZombieBreakDoor;
import net.pl3x.bukkit.ridables.entity.ai.AIMoveThroughVillage;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableHusk extends EntityZombieHusk implements RidableEntity {
    private final AIZombieBreakDoor breakDoorAI;

    public RidableHusk(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
        breakDoorAI = new AIZombieBreakDoor(this);
    }

    public RidableType getType() {
        return RidableType.HUSK;
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

        // also from EntityZombie
        goalSelector.a(2, new AIZombieAttack(this, 1.0D, false));
        goalSelector.a(6, new AIMoveThroughVillage(this, 1.0D, false));
        goalSelector.a(7, new AIWanderAvoidWater(this, 1.0D));
        targetSelector.a(1, new AIHurtByTarget(this, true, EntityPigZombie.class));
        targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
        if ((Ridables.isSpigot() || Ridables.isPaper()) && world.spigotConfig.zombieAggressiveTowardsVillager) {
            targetSelector.a(3, new AIAttackNearest<>(this, EntityVillager.class, false));
        }
        targetSelector.a(3, new AIAttackNearest<>(this, EntityIronGolem.class, true));
        targetSelector.a(5, new AIAttackNearest<>(this, EntityTurtle.class, 10, true, false, EntityTurtle.bC));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.HUSK_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.HUSK_JUMP_POWER;
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
        Q = Config.HUSK_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.HUSK_SPEED;
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
