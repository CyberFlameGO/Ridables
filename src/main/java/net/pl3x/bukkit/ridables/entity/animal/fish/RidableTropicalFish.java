package net.pl3x.bukkit.ridables.entity.animal.fish;

import net.minecraft.server.v1_13_R2.DataWatcherObject;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTropicalFish;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IEntitySelector;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.TropicalFishConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.goal.fish.AIFishFollowLeader;
import net.pl3x.bukkit.ridables.entity.ai.goal.fish.AIFishSwim;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;

public class RidableTropicalFish extends EntityTropicalFish implements RidableEntity, RidableFishSchool {
    public static final TropicalFishConfig CONFIG = new TropicalFishConfig();

    public RidableTropicalFish(World world) {
        super(world);
        moveController = new RidableCod.FishWASDController(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.TROPICAL_FISH;
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

        // from EntityTropicalFish
        if (CONFIG.AI_FOLLOW_SCHOOL) {
            goalSelector.a(5, new AIFishFollowLeader(this));
        }
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return true;
    }

    @Override
    public boolean isFollowing() {
        return dy();
    }

    // travel
    @Override
    public void a(float strafe, float vertical, float forward) {
        super.a(strafe, vertical, forward);
        checkMove();
    }

    // onLivingUpdate
    @Override
    public void movementTick() {
        if (getRider() != null) {
            motY += 0.005D;
        }
        super.movementTick();
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
}
