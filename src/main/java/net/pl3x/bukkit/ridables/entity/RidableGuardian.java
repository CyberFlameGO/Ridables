package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.ControllerLook;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityGuardian;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntitySquid;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.GuardianConfig;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.AIWander;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.guardian.AIGuardianAttack;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDWater;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RidableGuardian extends EntityGuardian implements RidableEntity {
    public static final GuardianConfig CONFIG = new GuardianConfig();

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

    public RidableGuardian(World world) {
        this(EntityTypes.GUARDIAN, world);
    }

    public RidableGuardian(EntityTypes<?> entityTypes, World world) {
        super(entityTypes, world);
        moveController = new GuardianWASDController(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.GUARDIAN;
    }

    // initAI - override vanilla AI
    protected void n() {
        AIMoveTowardsRestriction moveTowardsRestriction = new AIMoveTowardsRestriction(this, 1.0D);
        goalRandomStroll = new AIWander(this, 1.0D, 80);

        goalSelector.a(4, new AIGuardianAttack(this));
        goalSelector.a(5, moveTowardsRestriction);
        goalSelector.a(7, goalRandomStroll);
        goalSelector.a(8, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(8, new AIWatchClosest(this, EntityGuardian.class, 12.0F, 0.01F));
        goalSelector.a(9, new AILookIdle(this));
        goalRandomStroll.a(3);

        moveTowardsRestriction.a(3); // setMutexBits

        targetSelector.a(1, new AIAttackNearest<>(this, EntityLiving.class, 10, true, false, target ->
                (target instanceof EntityHuman || target instanceof EntitySquid) && target.h(RidableGuardian.this) > 9.0D));

        a(0);
    }

    // canBeRiddenInWater
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

    protected void mobTick() {
        if (getRider() != null && getAirTicks() > 150) {
            motY += 0.005F;
        }
        super.mobTick();
    }

    public float getSpeed() {
        return CONFIG.SPEED;
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    static class GuardianWASDController extends ControllerWASDWater {
        private final RidableGuardian guardian;

        public GuardianWASDController(RidableGuardian guardian) {
            super(guardian);
            this.guardian = guardian;
        }

        public void tick() {
            if (h == Operation.MOVE_TO && !guardian.getNavigation().p()) {
                double x = b - guardian.locX;
                double y = c - guardian.locY;
                double z = d - guardian.locZ;
                double distance = (double) MathHelper.sqrt(x * x + y * y + z * z);
                y /= distance;
                guardian.aQ = guardian.yaw = a(guardian.yaw, (float) (MathHelper.c(z, x) * (double) (180F / (float) Math.PI)) - 90.0F, 90.0F);
                guardian.o(guardian.cK() + ((float) (e * guardian.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()) - guardian.cK()) * 0.125F);
                double d0 = Math.sin((double) (guardian.ticksLived + guardian.getId()) * 0.5D) * 0.05D;
                double d1 = Math.cos((double) (guardian.yaw * ((float) Math.PI / 180F)));
                double d2 = Math.sin((double) (guardian.yaw * ((float) Math.PI / 180F)));
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
