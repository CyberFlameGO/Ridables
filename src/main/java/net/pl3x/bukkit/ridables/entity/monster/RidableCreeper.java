package net.pl3x.bukkit.ridables.entity.monster;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAreaEffectCloud;
import net.minecraft.server.v1_13_R2.EntityCreeper;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityOcelot;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.MobEffect;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.CreeperConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.creeper.AICreeperSwell;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.Collection;

public class RidableCreeper extends EntityCreeper implements RidableEntity {
    public static final CreeperConfig CONFIG = new CreeperConfig();

    private int spacebarCharge = 0;
    private int prevSpacebarCharge = 0;
    private int powerToggleDelay = 0;

    public RidableCreeper(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.CREEPER;
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
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
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
    @Override
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return getRider() == null ? CONFIG.AI_JUMP_POWER : (isIgnited() ? 0 : CONFIG.RIDING_JUMP_POWER);
    }

    @Override
    protected void mobTick() {
        if (powerToggleDelay > 0) {
            powerToggleDelay--;
        }
        if (getRider() != null) {
            Q = CONFIG.RIDING_STEP_HEIGHT;
            if (getRider().bj != 0 || getRider().bh != 0) {
                spacebarCharge = 0;
                setIgnited(false);
            }
            if (spacebarCharge == prevSpacebarCharge) {
                spacebarCharge = 0;
            }
            prevSpacebarCharge = spacebarCharge;
        } else {
            Q = CONFIG.AI_STEP_HEIGHT;
        }
        super.mobTick();
    }

    // travel
    @Override
    public void a(float strafe, float vertical, float forward) {
        super.a(strafe, vertical, forward);
        checkMove();
    }

    @Override
    public void tick() {
        if (isAlive()) {
            // we only want to know when to explode(), rest of logic is still in super.tick()
            int state = dz();
            fuseTicks += state;
            if (fuseTicks >= maxFuseTicks) {
                fuseTicks = maxFuseTicks;
                explode();
            }
            fuseTicks -= state; // dont let super.tick() double count the fuse
        }
        super.tick();
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
    public boolean removePassenger(Entity passenger, boolean notCancellable) {
        if (passenger instanceof EntityPlayer && !passengers.isEmpty() && passenger == passengers.get(0)) {
            if (!new RidableDismountEvent(this, (Player) passenger.getBukkitEntity(), notCancellable).callEvent() && !notCancellable) {
                return false; // cancelled
            }
        }
        return super.removePassenger(passenger, notCancellable);
    }

    @Override
    public boolean onSpacebar() {
        if (powerToggleDelay > 0) {
            return true; // just toggled power, do not jump
        }
        spacebarCharge++;
        if (spacebarCharge > maxFuseTicks - 1) {
            spacebarCharge = 0;
            if (getRider().getBukkitEntity().hasPermission("allow.powered.creeper")) {
                powerToggleDelay = 20;
                setPowered(!isPowered());
                setIgnited(false);
                return true;
            }
        }
        if (!isIgnited()) {
            EntityPlayer rider = getRider();
            if (rider != null && rider.bj == 0 && rider.bh == 0 && rider.getBukkitEntity().hasPermission("allow.special.creeper")) {
                setIgnited(true);
                return true;
            }
        }
        return false;
    }

    /**
     * Make the creeper explode
     */
    @Override
    public void explode() {
        boolean hasRider = getRider() != null;
        ExplosionPrimeEvent event = new ExplosionPrimeEvent(getBukkitEntity(),
                (hasRider ? CONFIG.RIDING_EXPLOSION_RADIUS : CONFIG.AI_EXPLOSION_RADIUS) * (isPowered() ? 2.0F : 1.0F),
                hasRider ? CONFIG.RIDING_EXPLOSION_FIRE : CONFIG.AI_EXPLOSION_FIRE);
        if (!event.callEvent()) {
            fuseTicks = 0;
            setIgnited(false);
            return;
        }

        aX = true; // isDying
        world.createExplosion(this, locX, locY, locZ, event.getRadius(), event.getFire(),
                hasRider ? CONFIG.RIDING_EXPLOSION_GRIEF : (CONFIG.AI_EXPLOSION_GRIEF && world.getGameRules().getBoolean("mobGriefing")));
        die();
        if (hasRider) {
            if (CONFIG.RIDING_EXPLOSION_LINGERING_CLOUD) {
                spawnCloud();
            }
        } else {
            if (CONFIG.AI_EXPLOSION_LINGERING_CLOUD) {
                spawnCloud();
            }
        }
    }

    private void spawnCloud() {
        Collection<MobEffect> collection = getEffects();
        if (collection.isEmpty()) {
            return;
        }
        if (world.paperConfig.disableCreeperLingeringEffect) {
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
            cloud.a(new MobEffect(mobEffect)); // addEffect
        }
        world.addEntity(cloud);
    }
}
