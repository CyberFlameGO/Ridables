package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityPufferFish;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumMoveType;
import net.minecraft.server.v1_13_R2.IEntitySelector;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.fish.AIFishSwim;
import net.pl3x.bukkit.ridables.entity.ai.fish.pufferfish.AIPuffUp;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

import java.lang.reflect.Field;

public class RidablePufferFish extends EntityPufferFish implements RidableEntity {
    private static Field puffTimer;
    private static Field deflateTimer;

    static {
        try {
            puffTimer = EntityPufferFish.class.getDeclaredField("c");
            puffTimer.setAccessible(true);
            deflateTimer = EntityPufferFish.class.getDeclaredField("bC");
            deflateTimer.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    private int spacebarCooldown = 0;

    public RidablePufferFish(World world) {
        super(world);
        moveController = new RidableCod.FishWASDController(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.PUFFERFISH;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        // from EntityFish
        goalSelector.a(0, new AIPanic(this, 1.25D));
        goalSelector.a(2, new AIAvoidTarget<>(this, EntityHuman.class, 8.0F, 1.6D, 1.4D, IEntitySelector.f));
        goalSelector.a(4, new AIFishSwim(this));

        // from EntityPufferfish
        goalSelector.a(5, new AIPuffUp(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return true;
    }

    // onLivingUpdate
    public void k() {
        if (spacebarCooldown > 0) {
            spacebarCooldown--;
        }
        if (getRider() != null) {
            motY += 0.005D;
        }
        super.k();
    }

    // travel
    public void a(float strafe, float vertical, float forward) {
        EntityPlayer rider = getRider();
        if (rider != null) {
            if (!isInWater()) {
                forward = rider.bj;
                strafe = rider.bh;
            }
        }
        if (cP() && this.isInWater()) {
            a(strafe, vertical, forward, rider == null ? 0.01F : getSpeed());
            move(EnumMoveType.SELF, motX, motY, motZ);
            motX *= 0.8999999761581421D;
            motY *= 0.8999999761581421D;
            motZ *= 0.8999999761581421D;
            if (getGoalTarget() == null) {
                motY -= 0.005D;
            }
            return;
        }
        super.a(strafe, vertical, forward);
    }

    public float getSpeed() {
        return Config.PUFFERFISH_SPEED;
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
