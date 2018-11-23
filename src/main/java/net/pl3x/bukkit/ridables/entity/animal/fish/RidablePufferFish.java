package net.pl3x.bukkit.ridables.entity.animal.fish;

import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityPufferFish;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumMonsterType;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IEntitySelector;
import net.minecraft.server.v1_13_R2.MobEffect;
import net.minecraft.server.v1_13_R2.MobEffects;
import net.minecraft.server.v1_13_R2.PacketPlayOutGameStateChange;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.PufferfishConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.goal.fish.AIFishSwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.fish.pufferfish.AIPuffUp;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPotionEffectEvent;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class RidablePufferFish extends EntityPufferFish implements RidableEntity {
    public static final PufferfishConfig CONFIG = new PufferfishConfig();
    public static final Predicate<EntityLiving> ENEMY_MATCHER = (target) ->
            target != null && target.getMonsterType() != EnumMonsterType.e // EnumMonsterType.WATER
                    && IEntitySelector.canAITarget().test(target);

    private static Field puffTimer;
    private static Field deflateTimer;

    static {
        try {
            puffTimer = EntityPufferFish.class.getDeclaredField("b");
            puffTimer.setAccessible(true);
            deflateTimer = EntityPufferFish.class.getDeclaredField("c");
            deflateTimer.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    private boolean fakePuffState = false;
    private int spacebarCooldown = 0;

    public RidablePufferFish(World world) {
        super(world);
        moveController = new RidableCod.FishWASDController(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.PUFFERFISH;
    }

    // isNoDespawnRequired
    @Override
    public boolean isPersistent() {
        return isFromBucket() || persistent;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !isFromBucket() && !hasCustomName() && !isLeashed();
    }

    @Override
    public void setFromBucket(boolean flag) {
        try {
            datawatcher.set((DataWatcherObject<? super Boolean>) RidableCod.fromBucket.get(this), flag);
        } catch (IllegalAccessException e) {
            super.setFromBucket(flag);
        }
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
        // from EntityFish
        if (CONFIG.AI_PANIC_SPEED > 0) {
            goalSelector.a(0, new AIPanic(this, CONFIG.AI_PANIC_SPEED));
        }
        if (CONFIG.AI_AVOID_PLAYER_DISTANCE > 0) {
            goalSelector.a(2, new AIAvoidTarget<>(this, EntityHuman.class, CONFIG.AI_AVOID_PLAYER_DISTANCE, CONFIG.AI_AVOID_PLAYER_SPEED_FAR, CONFIG.AI_AVOID_PLAYER_SPEED_NEAR, IEntitySelector.notSpectator()));
        }
        goalSelector.a(4, new AIFishSwim(this));

        // from EntityPufferfish
        if (CONFIG.AI_PUFF_UP_RADIUS > 0) {
            goalSelector.a(5, new AIPuffUp(this));
        }
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return true;
    }

    // travel
    @Override
    public void a(float strafe, float vertical, float forward) {
        super.a(strafe, vertical, forward);
        checkMove();
    }

    // onLivingUpdate
    @Override
    public void k() {
        if (spacebarCooldown > 0) {
            spacebarCooldown--;
        }
        EntityPlayer rider = getRider();
        if (rider != null) {
            motY += 0.005D;
        }
        int puffState = getPuffState();
        if (puffState > 0) {
            final float damage = getDamage(rider, puffState);
            final int poisonTicks = getPoisonDuration(rider, puffState);
            final int poisonAmplifier = getPoisonAmplifier(rider, puffState);
            world.a(EntityInsentient.class, getBoundingBox().g(0.3D), ENEMY_MATCHER).stream() // getEntitiesWithinAABB grow
                    .filter(EntityLiving::isAlive)
                    .forEach(target -> {
                        if (target.damageEntity(DamageSource.mobAttack(this), damage)) {
                            inflictPoison(target, poisonTicks, poisonAmplifier, true);
                        }
                    });
        }
        fakePuffState = true;
        super.k();
        fakePuffState = false;
    }

    @Override
    public int getPuffState() {
        return fakePuffState ? -1 : super.getPuffState();
    }

    // onCollideWithPlayer
    @Override
    public void d(EntityHuman player) {
        EntityPlayer rider = getRider();
        if (player == rider) {
            return; // do not damage rider
        }
        int puffState = getPuffState();
        float damage = getDamage(rider, puffState);
        if (player instanceof EntityPlayer && damage > 0 && player.damageEntity(DamageSource.mobAttack(this), damage)) {
            ((EntityPlayer) player).playerConnection.sendPacket(new PacketPlayOutGameStateChange(9, 0.0F));
            inflictPoison(player, getPoisonDuration(rider, puffState), getPoisonAmplifier(rider, puffState), false);
        }
    }

    public void inflictPoison(EntityLiving target, int poisonTicks, int poisonAmplifier, boolean playSound) {
        if (poisonTicks > 0) {
            target.addEffect(new MobEffect(MobEffects.POISON, poisonTicks, poisonAmplifier), EntityPotionEffectEvent.Cause.ATTACK);
            if (playSound) {
                a(SoundEffects.ENTITY_PUFFER_FISH_STING, 1.0F, 1.0F); // playSound
            }
        }
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
        if (spacebarCooldown == 0) {
            spacebarCooldown = 20;
            if (getPuffState() > 0) {
                setPuffState(0);
                setPuffTimer(0);
            } else {
                setPuffState(1);
                setPuffTimer(1);
            }
            return true;
        }
        return false;
    }

    public float getDamage(EntityPlayer rider, int puffState) {
        return puffState == 1 ? (rider == null ? CONFIG.AI_DAMAGE_HALF_PUFF : CONFIG.RIDING_DAMAGE_HALF_PUFF)
                : (puffState == 2 ? (rider == null ? CONFIG.AI_DAMAGE_FULL_PUFF : CONFIG.RIDING_DAMAGE_FULL_PUFF) : 0);
    }

    public int getPoisonDuration(EntityPlayer rider, int puffState) {
        return puffState == 1 ? (rider == null ? CONFIG.AI_POISON_DURATION_HALF_PUFF : CONFIG.RIDING_POISON_DURATION_HALF_PUFF)
                : (puffState == 2 ? (rider == null ? CONFIG.AI_POISON_DURATION_FULL_PUFF : CONFIG.RIDING_POISON_DURATION_FULL_PUFF) : 0);
    }

    public int getPoisonAmplifier(EntityPlayer rider, int puffState) {
        return puffState == 1 ? (rider == null ? CONFIG.AI_POISON_AMPLIFIER_HALF_PUFF : CONFIG.RIDING_POISON_AMPLIFIER_HALF_PUFF)
                : (puffState == 2 ? (rider == null ? CONFIG.AI_POISON_AMPLIFIER_FULL_PUFF : CONFIG.RIDING_POISON_AMPLIFIER_FULL_PUFF) : 0);
    }

    /**
     * Get puff timer
     *
     * @return Puff timer
     */
    public int getPuffTimer() {
        try {
            return puffTimer.getInt(this);
        } catch (IllegalAccessException ignore) {
            return 0;
        }
    }

    /**
     * Set puff timer
     *
     * @param time New puff timer
     */
    public void setPuffTimer(int time) {
        try {
            puffTimer.set(this, time);
        } catch (IllegalAccessException ignore) {
        }
    }

    /**
     * Get the deflate timer
     *
     * @return Deflate timer
     */
    public int getDeflateTimer() {
        try {
            return deflateTimer.getInt(this);
        } catch (IllegalAccessException ignore) {
            return 0;
        }
    }

    /**
     * Set the deflate timer
     *
     * @param time New deflate timer
     */
    public void setDeflateTimer(int time) {
        try {
            deflateTimer.setInt(this, time);
        } catch (IllegalAccessException ignore) {
        }
    }
}
