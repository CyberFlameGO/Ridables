package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityVillager;
import net.minecraft.server.v1_13_R2.EntityVindicator;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWander;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.vindicator.AIVindicatorJohnnyAttack;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

import java.lang.reflect.Field;
import java.util.function.Predicate;

public class RidableVindicator extends EntityVindicator implements RidableEntity {
    public static final Predicate<Entity> JOHNNY_SELECTOR = (target) -> target instanceof EntityLiving && ((EntityLiving) target).df(); // attackable

    private static Field johnny;

    static {
        try {
            johnny = EntityVindicator.class.getDeclaredField("b");
            johnny.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    public RidableVindicator(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.VINDICATOR;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(4, new AIAttackMelee(this, 1.0D, false));
        goalSelector.a(8, new AIWander(this, 0.6D));
        goalSelector.a(9, new AIWatchClosest(this, EntityHuman.class, 3.0F, 1.0F));
        goalSelector.a(10, new AIWatchClosest(this, EntityInsentient.class, 8.0F));
        targetSelector.a(1, new AIHurtByTarget(this, true, EntityVindicator.class));
        targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
        targetSelector.a(3, new AIAttackNearest<>(this, EntityVillager.class, true));
        targetSelector.a(3, new AIAttackNearest<>(this, EntityIronGolem.class, true));
        targetSelector.a(4, new AIVindicatorJohnnyAttack(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.VINDICATOR_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.VINDICATOR_JUMP_POWER;
    }

    public boolean isJohnnyMode() {
        try {
            return johnny.getBoolean(this);
        } catch (IllegalAccessException ignore) {
        }
        return false;
    }

    protected void mobTick() {
        Q = Config.VINDICATOR_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.VINDICATOR_SPEED;
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
}
