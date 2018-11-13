package net.pl3x.bukkit.ridables.entity.monster.zombie;

import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPigZombie;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.Navigation;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.ZombiePigmanConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.AIZombieAttack;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.AIZombieAttackTurtleEgg;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.AIZombieBreakDoor;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.zombie_pigman.AIPigmanHurtByAggressor;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.zombie_pigman.AIPigmanTargetAggressor;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.entity.PigZombieAngerEvent;

import java.lang.reflect.Field;

public class RidableZombiePigman extends EntityPigZombie implements RidableEntity {
    public static final ZombiePigmanConfig CONFIG = new ZombiePigmanConfig();

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
        breakDoorAI = new AIZombieBreakDoor(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.ZOMBIE_PIGMAN;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
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
    @Override
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return getRider() == null ? super.cG() : CONFIG.JUMP_POWER;
    }

    // setBreakDoorsAITask
    @Override
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

    @Override
    public boolean damageEntity(DamageSource damagesource, float damage) {
        if (isInvulnerable(damagesource)) {
            return false;
        }
        Entity target = damagesource.getEntity();
        boolean result = super.damageEntity(damagesource, damage);
        if (result && target instanceof EntityHuman && !((EntityHuman) target).u() && getRider() == null) { // isCreative
            becomeAngryAt(target);
        }
        return result;
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
            setLastDamager((EntityLiving) target);
        }
    }
}
