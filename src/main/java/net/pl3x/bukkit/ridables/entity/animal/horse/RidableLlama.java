package net.pl3x.bukkit.ridables.entity.animal.horse;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLlama;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.configuration.mob.LlamaConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackRanged;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIBreed;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIFollowParent;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.horse.AIHorseBucking;
import net.pl3x.bukkit.ridables.entity.ai.goal.horse.llama.AILlamaDefendTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.horse.llama.AILlamaFollowCaravan;
import net.pl3x.bukkit.ridables.entity.ai.goal.horse.llama.AILlamaHurtByTarget;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import net.pl3x.bukkit.ridables.event.RidableMountEvent;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class RidableLlama extends EntityLlama implements RidableEntity {
    public static final LlamaConfig CONFIG = new LlamaConfig();

    private static Field didSpit;

    static {
        try {
            didSpit = EntityLlama.class.getDeclaredField("bP");
            didSpit.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    public RidableLlama(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.LLAMA;
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED);
        reloadAttributes();
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH > 0.0D ? CONFIG.MAX_HEALTH : ec()); // getModifiedMaxHealth
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(attributeJumpStrength).setValue(CONFIG.AI_JUMP_POWER);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(1, new AIHorseBucking(this, 1.2D));
        goalSelector.a(2, new AILlamaFollowCaravan(this, (double) 2.1F));
        goalSelector.a(3, new AIAttackRanged(this, 1.25D, 40, 20.0F));
        goalSelector.a(3, new AIPanic(this, 1.2D));
        goalSelector.a(4, new AIBreed(this, 1.0D, EntityLlama.class));
        goalSelector.a(5, new AIFollowParent(this, 1.0D));
        goalSelector.a(6, new AIWanderAvoidWater(this, 0.7D));
        goalSelector.a(7, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(8, new AILookIdle(this));
        targetSelector.a(1, new AILlamaHurtByTarget(this));
        targetSelector.a(2, new AILlamaDefendTarget(this));
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    @Override
    public boolean isTamed() {
        return p(2) || (CONFIG.RIDING_BABIES && isBaby()); // getHorseWatchableBoolean
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return getRider() == null ? CONFIG.AI_JUMP_POWER : CONFIG.RIDING_JUMP_POWER;
    }

    public boolean didSpit() {
        try {
            return didSpit.getBoolean(this);
        } catch (IllegalAccessException ignore) {
        }
        return false;
    }

    public void setDidSpit(boolean spit) {
        try {
            didSpit.setBoolean(this, spit);
        } catch (IllegalAccessException ignore) {
        }
    }

    @Override
    public boolean isLeashed() {
        return (CONFIG.RIDING_STARTS_CARAVAN && getRider() != null) || super.isLeashed();
    }

    @Override
    public Entity getLeashHolder() {
        EntityPlayer rider = getRider();
        return rider != null ? rider : super.getLeashHolder();
    }

    // hasCaravan
    @Override
    public boolean em() {
        return (CONFIG.RIDING_STARTS_CARAVAN && getRider() != null) || super.em();
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
        //checkMove(); // not needed
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (isBaby() && CONFIG.RIDING_BABIES && hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            g(entityhuman); // mountTo
            return true;
        }
        return false;
    }

    // mountTo
    @Override
    public void g(EntityHuman entityhuman) {
        Player player = (Player) entityhuman.getBukkitEntity();
        if (!player.hasPermission("allow.ride.llama")) {
            Lang.send(player, Lang.RIDE_NO_PERMISSION);
            return;
        }
        if (new RidableMountEvent(this, player).callEvent()) {
            super.g(entityhuman);
            entityhuman.o(false); // setJumping - fixes jump on mount
        }
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
