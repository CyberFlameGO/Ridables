package net.pl3x.bukkit.ridables.entity.monster;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityArrow;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIllagerIllusioner;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityVillager;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.IllusionerConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIShootBow;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWander;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.illusioner.AIIllusionerBlindnessSpell;
import net.pl3x.bukkit.ridables.entity.ai.goal.illusioner.AIIllusionerCastingSpell;
import net.pl3x.bukkit.ridables.entity.ai.goal.illusioner.AIIllusionerMirrorSpell;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;

public class RidableIllusioner extends EntityIllagerIllusioner implements RidableEntity {
    public static IllusionerConfig CONFIG = new IllusionerConfig();

    public RidableIllusioner(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.ILLUSIONER;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
    }

    @Override
    public void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        reloadAttributes();
        setHealth(getMaxHealth());
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(1, new AIIllusionerCastingSpell(this));
        goalSelector.a(4, new AIIllusionerMirrorSpell(this));
        goalSelector.a(5, new AIIllusionerBlindnessSpell(this));
        goalSelector.a(6, new AIShootBow<>(this, 0.5D, 20, 15.0F));
        goalSelector.a(8, new AIWander(this, 0.6D));
        goalSelector.a(9, new AIWatchClosest(this, EntityHuman.class, 3.0F, 1.0F));
        goalSelector.a(10, new AIWatchClosest(this, EntityInsentient.class, 8.0F));
        targetSelector.a(1, new AIHurtByTarget(this, true, EntityIllagerIllusioner.class));
        targetSelector.a(2, (new AIAttackNearest<>(this, EntityHuman.class, true)).b(300));
        targetSelector.a(3, (new AIAttackNearest<>(this, EntityVillager.class, false)).b(300));
        targetSelector.a(3, (new AIAttackNearest<>(this, EntityIronGolem.class, false)).b(300));
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return getRider() == null ? CONFIG.AI_JUMP_POWER : CONFIG.RIDING_JUMP_POWER;
    }

    public int getSpellTicks() {
        return b;
    }

    public void setSpellTicks(int ticks) {
        b = ticks;
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
        checkMove();
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

    // attackEntityWithRangedAttack
    @Override
    public void a(EntityLiving target, float distanceFactor) {
        EntityArrow arrow = v(distanceFactor); // createArrowEntity
        double x = target.locX - locX;
        double y = target.getBoundingBox().minY + (double) (target.length / 3.0F) - arrow.locY;
        double z = target.locZ - locZ;
        double distance = (double) MathHelper.sqrt(x * x + z * z);
        arrow.shoot(x, y + distance * (double) 0.2F, z, 1.6F, (float) (14 - world.getDifficulty().a() * 4)); // getId
        arrow.setDamage(CONFIG.AI_RANGED_DAMAGE);
        EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(this, getItemInMainHand(), getItemInOffHand(), arrow, 0.8F);
        if (event.isCancelled()) {
            event.getProjectile().remove();
        } else {
            if (event.getProjectile() == arrow.getBukkitEntity()) {
                world.addEntity(arrow);
            }
            a(SoundEffects.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F)); // playSound
        }
    }
}
