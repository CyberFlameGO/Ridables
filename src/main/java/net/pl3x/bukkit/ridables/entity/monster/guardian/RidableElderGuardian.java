package net.pl3x.bukkit.ridables.entity.monster.guardian;

import net.minecraft.server.v1_13_R2.ControllerLook;
import net.minecraft.server.v1_13_R2.CriterionTriggers;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityGuardian;
import net.minecraft.server.v1_13_R2.EntityGuardianElder;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntitySquid;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.configuration.mob.ElderGuardianConfig;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASDWater;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWander;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.elder_guardian.AIElderGuardianAttack;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import net.pl3x.bukkit.ridables.util.Const;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RidableElderGuardian extends EntityGuardianElder implements RidableEntity {
    public static final ElderGuardianConfig CONFIG = new ElderGuardianConfig();

    private static Method setMoving;
    private static Method setTargetedEntity;

    static {
        try {
            setMoving = EntityGuardian.class.getDeclaredMethod("a", boolean.class);
            setMoving.setAccessible(true);
            setTargetedEntity = EntityGuardian.class.getDeclaredMethod("a", int.class);
            setTargetedEntity.setAccessible(true);
        } catch (NoSuchMethodException ignore) {
        }
    }

    public RidableElderGuardian(World world) {
        super(world);
        moveController = new ElderGuardianWASDController(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.ELDER_GUARDIAN;
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
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_ATTACK_DAMAGE);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        AIMoveTowardsRestriction moveTowardsRestriction = new AIMoveTowardsRestriction(this, 1.0D);
        goalRandomStroll = new AIWander(this, 1.0D, 80);

        goalSelector.a(4, new AIElderGuardianAttack(this));
        goalSelector.a(5, moveTowardsRestriction);
        goalSelector.a(7, goalRandomStroll);
        goalSelector.a(8, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(8, new AIWatchClosest(this, EntityGuardian.class, 12.0F, 0.01F));
        goalSelector.a(9, new AILookIdle(this));
        goalRandomStroll.a(3);

        moveTowardsRestriction.a(3); // setMutexBits

        targetSelector.a(1, new AIAttackNearest<>(this, EntityLiving.class, 10, true, false, target ->
                (target instanceof EntityHuman || target instanceof EntitySquid) && target.h(RidableElderGuardian.this) > 9.0D));

        a(0);
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return true;
    }

    public boolean isMoving() {
        return dB();
    }

    public void setMoving(boolean moving) {
        try {
            setMoving.invoke(this, moving);
        } catch (IllegalAccessException | InvocationTargetException ignore) {
        }
    }

    public boolean hasTargetedEntity() {
        return dC();
    }

    public void setTargetedEntity(int id) {
        try {
            setTargetedEntity.invoke(this, id);
        } catch (IllegalAccessException | InvocationTargetException ignore) {
        }
    }

    @Override
    protected void mobTick() {
        if (getRider() != null && getAirTicks() > 150) {
            motY += 0.005F;
        }
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
        if (collectInWaterBucket(entityhuman, hand)) {
            return true; // handled
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

    static class ElderGuardianWASDController extends ControllerWASDWater {
        private final RidableElderGuardian guardian;

        public ElderGuardianWASDController(RidableElderGuardian guardian) {
            super(guardian);
            this.guardian = guardian;
        }

        @Override
        public void tick() {
            if (h == Operation.MOVE_TO && !guardian.getNavigation().p()) {
                double x = b - guardian.locX;
                double y = c - guardian.locY;
                double z = d - guardian.locZ;
                double distance = (double) MathHelper.sqrt(x * x + y * y + z * z);
                y /= distance;
                guardian.aQ = guardian.yaw = a(guardian.yaw, (float) (MathHelper.c(z, x) * Const.RAD2DEG) - 90.0F, 90.0F);
                guardian.o(guardian.cK() + ((float) (e * guardian.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()) - guardian.cK()) * 0.125F);
                double d0 = Math.sin((double) (guardian.ticksLived + guardian.getId()) * 0.5D) * 0.05D;
                double d1 = Math.cos((double) (guardian.yaw * Const.DEG2RAD_FLOAT));
                double d2 = Math.sin((double) (guardian.yaw * Const.DEG2RAD_FLOAT));
                guardian.motX += d0 * d1;
                guardian.motZ += d0 * d2;
                guardian.motY += Math.sin((double) (guardian.ticksLived + guardian.getId()) * 0.75D) * 0.05D * (d2 + d1) * 0.25D;
                guardian.motY += (double) guardian.cK() * y * 0.1D;
                ControllerLook lookController = guardian.getControllerLook();
                double x2 = guardian.locX + x / distance * 2.0D;
                double y2 = (double) guardian.getHeadHeight() + guardian.locY + y / distance;
                double z2 = guardian.locZ + z / distance * 2.0D;
                double lx = lookController.e();
                double ly = lookController.f();
                double lz = lookController.g();
                if (!lookController.b()) { // getIsLooking
                    lx = x2;
                    ly = y2;
                    lz = z2;
                }
                guardian.getControllerLook().a(lx + (x2 - lx) * 0.125D, ly + (y2 - ly) * 0.125D, lz + (z2 - lz) * 0.125D, 10.0F, 40.0F);
                guardian.setMoving(true);
            } else {
                guardian.o(0.0F);
                guardian.setMoving(false);
            }
        }
    }
}
