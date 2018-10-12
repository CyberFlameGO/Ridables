package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntitySilverfish;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIMeleeAttack;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.silverfish.AISilverfishHideInBlock;
import net.pl3x.bukkit.ridables.entity.ai.silverfish.AISilverfishWakeOthers;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

import java.lang.reflect.Field;

public class RidableSilverfish extends EntitySilverfish implements RidableEntity {
    private static Field summonSilverfish;

    static {
        try {
            summonSilverfish = EntitySilverfish.class.getDeclaredField("a");
            summonSilverfish.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    public RidableSilverfish(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.SILVERFISH;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        AISilverfishWakeOthers wakeOthers = new AISilverfishWakeOthers(this);

        try {
            summonSilverfish.set(this, wakeOthers);
        } catch (IllegalAccessException ignore) {
        }

        goalSelector.a(1, new AISwim(this));
        goalSelector.a(3, wakeOthers);
        goalSelector.a(4, new AIMeleeAttack(this, 1.0D, false));
        goalSelector.a(5, new AISilverfishHideInBlock(this));
        targetSelector.a(1, new AIHurtByTarget(this, true));
        targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.SILVERFISH_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.SILVERFISH_JUMP_POWER;
    }

    protected void mobTick() {
        Q = Config.SILVERFISH_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.SILVERFISH_SPEED;
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
