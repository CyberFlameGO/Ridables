package net.pl3x.bukkit.ridables.entity.animal.horse;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLlama;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.World;
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

    public RidableType getType() {
        return RidableType.LLAMA;
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDE_SPEED);
        reloadAttributes();
    }

    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDE_SPEED).setValue(CONFIG.RIDE_SPEED);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(attributeJumpStrength).setValue(CONFIG.JUMP_POWER);
        if (CONFIG.MAX_HEALTH > 0.0D) {
            getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        }
    }

    // initAI - override vanilla AI
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
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        //return getRider() == null ? super.cG() : CONFIG.JUMP_POWER;
        return super.cG(); // TODO
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

    public boolean isLeashed() {
        return (CONFIG.CARAVAN && getRider() != null) || super.isLeashed();
    }

    public Entity getLeashHolder() {
        EntityPlayer rider = getRider();
        return rider != null ? rider : super.getLeashHolder();
    }

    // hasCaravan
    public boolean em() {
        return (CONFIG.CARAVAN && getRider() != null) || super.em();
    }

    protected void mobTick() {
        Q = CONFIG.STEP_HEIGHT;
        super.mobTick();
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    public RidableLlama createChild(EntityAgeable entity) {
        return b(entity);
    }

    // createChild (bukkit's weird duplicate method)
    public RidableLlama b(EntityAgeable entity) {
        RidableLlama baby = new RidableLlama(world);
        a(entity, baby); // setOffspringAttributes
        EntityLlama otherParent = (EntityLlama) entity;
        int strength = random.nextInt(Math.max(getStrength(), otherParent.getStrength())) + 1;
        if (random.nextFloat() < 0.03F) {
            ++strength;
        }
        baby.setStrength(strength);
        baby.setVariant(random.nextBoolean() ? getVariant() : otherParent.getVariant());
        return baby;
    }
}
