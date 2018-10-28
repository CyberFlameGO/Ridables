package net.pl3x.bukkit.ridables.entity.animal.horse;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHorseAbstract;
import net.minecraft.server.v1_13_R2.EntityHorseDonkey;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.DonkeyConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIBreed;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIFollowParent;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.horse.AIHorseBucking;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.entity.Player;

public class RidableDonkey extends EntityHorseDonkey implements RidableEntity {
    public static final DonkeyConfig CONFIG = new DonkeyConfig();

    public RidableDonkey(World world) {
        super(world);
    }

    public RidableType getType() {
        return RidableType.DONKEY;
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        reloadAttributes();
    }

    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(attributeJumpStrength).setValue(CONFIG.RIDING_JUMP_POWER);
        if (CONFIG.MAX_HEALTH > 0.0D) {
            getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        }
    }

    // initAI - override vanilla AI
    protected void n() {
        // from EntityHorseAbstract
        goalSelector.a(1, new AIPanic(this, 1.2D));
        goalSelector.a(1, new AIHorseBucking(this, 1.2D));
        goalSelector.a(2, new AIBreed(this, 1.0D, EntityHorseAbstract.class));
        goalSelector.a(4, new AIFollowParent(this, 1.0D));
        goalSelector.a(6, new AIWanderAvoidWater(this, 0.7D));
        goalSelector.a(7, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(8, new AILookIdle(this));
        dI(); // initExtraAI
    }

    // initExtraAI
    protected void dI() {
        goalSelector.a(0, new AISwim(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return getRider() == null ? CONFIG.AI_JUMP_POWER : CONFIG.RIDING_JUMP_POWER;
    }

    public boolean isTamed() {
        return true;
    }

    public void mobTick() {
        Q = getRider() == null ? CONFIG.AI_STEP_HEIGHT : CONFIG.RIDING_STEP_HEIGHT;
        super.mobTick();
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
            return tryRide(entityhuman, false, false);
        }
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger) {
        return (!(passenger instanceof Player) || passengers.isEmpty() || !passenger.equals(passengers.get(0))
                || new RidableDismountEvent(this, (Player) passenger).callEvent()) && super.removePassenger(passenger);
    }
}
