package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAreaEffectCloud;
import net.minecraft.server.v1_13_R2.EntityCreeper;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityOcelot;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.MobEffect;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.creeper.AICreeperSwell;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import net.pl3x.bukkit.ridables.hook.Paper;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.lang.reflect.Field;
import java.util.Collection;

public class RidableCreeper extends EntityCreeper implements RidableEntity {
    public RidableCreeper(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.CREEPER;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(1, new AISwim(this));
        goalSelector.a(2, new AICreeperSwell(this));
        goalSelector.a(3, new AIAvoidTarget<>(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        goalSelector.a(4, new AIAttackMelee(this, 1.0D, false));
        goalSelector.a(5, new AIWanderAvoidWater(this, 0.8D));
        goalSelector.a(6, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(6, new AILookIdle(this));
        targetSelector.a(1, new AIAttackNearest<>(this, EntityHuman.class, true));
        targetSelector.a(2, new AIHurtByTarget(this, false));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.CREEPER_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return isIgnited() ? 0 : Config.CREEPER_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.CREEPER_STEP_HEIGHT;
        if (getRider() != null) {
            if (getRider().bj != 0 || getRider().bh != 0) {
                setIgnitedFlag(false);
            }
        }
        super.mobTick();
    }

    public void tick() {
        if (isAlive()) {
            int fuse = getFuseTicks();
            int state = dz();
            fuse += state;
            if (fuse >= maxFuseTicks) {
                fuse = maxFuseTicks;
                explode();
            }
            fuse -= state; // dont let super.tick() double count the fuse
            setFuseTicks(fuse);
        }
        super.tick();
    }

    public float getSpeed() {
        return Config.CREEPER_SPEED;
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

    public boolean onSpacebar() {
        if (!isIgnited()) {
            EntityPlayer rider = getRider();
            if (rider != null && rider.bj == 0 && rider.bh == 0 && hasSpecialPerm(rider.getBukkitEntity())) {
                setIgnitedFlag(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Make the creeper explode
     */
    public void explode() {
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(), Config.CREEPER_EXPLOSION_RADIUS * (isPowered() ? 2.0F : 1.0F), false);
        world.getServer().getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            aX = true; // isDying
            boolean flag = getRider() == null ? world.getGameRules().getBoolean("mobGriefing") : Config.CREEPER_EXPLOSION_GRIEF;
            world.createExplosion(this, locX, locY, locZ, event.getRadius(), event.getFire(), flag);
            die();
            spawnCloud();
        } else {
            setFuseTicks(0);
            setIgnitedFlag(false);
        }
    }

    private void spawnCloud() {
        Collection<MobEffect> collection = getEffects();
        if (collection.isEmpty()) {
            return;
        }
        if (Ridables.isPaper() && Paper.DisableCreeperLingeringEffect(world)) {
            return;
        }
        EntityAreaEffectCloud cloud = new EntityAreaEffectCloud(world, locX, locY, locZ);
        cloud.setSource(this);
        cloud.setRadius(2.5F);
        cloud.setRadiusOnUse(-0.5F);
        cloud.setWaitTime(10);
        cloud.setDuration(cloud.getDuration() / 2);
        cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
        for (MobEffect mobEffect : collection) {
            cloud.a(new MobEffect(mobEffect));
        }
        world.addEntity(cloud);
    }

    // Use reflection for non-Paper environments :sad-face:

    private static Field ignited_field;
    private static Field fuseTicks_field;

    static {
        try {
            ignited_field = EntityCreeper.class.getDeclaredField("c");
            ignited_field.setAccessible(true);
            fuseTicks_field = EntityCreeper.class.getDeclaredField("fuseTicks");
            fuseTicks_field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the ignited datawatcher flag
     *
     * @param ignited True to mark as ignited
     */
    public void setIgnitedFlag(boolean ignited) {
        if (ignited) {
            dB(); // setIgnited()
        } else {
            try {
                getDataWatcher().set((DataWatcherObject<Boolean>) ignited_field.get(this), false);
            } catch (IllegalAccessException ignore) {
            }
        }
    }

    /**
     * Get the current fuseTicks
     *
     * @return Current fuseTicks
     */
    public int getFuseTicks() {
        try {
            return fuseTicks_field.getInt(this);
        } catch (IllegalAccessException ignore) {
        }
        return 0;
    }

    /**
     * Set the current fuseTicks
     *
     * @param fuseTicks New fuseTicks
     */
    public void setFuseTicks(int fuseTicks) {
        try {
            fuseTicks_field.set(this, fuseTicks);
        } catch (IllegalAccessException ignore) {
        }
    }
}
