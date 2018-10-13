package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTropicalFish;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumMoveType;
import net.minecraft.server.v1_13_R2.IEntitySelector;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.fish.AIFishFollowLeader;
import net.pl3x.bukkit.ridables.entity.ai.fish.AIFishSwim;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableTropicalFish extends EntityTropicalFish implements RidableEntity {
    public RidableTropicalFish(World world) {
        super(world);
        moveController = new RidableCod.FishWASDController(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.TROPICAL_FISH;
    }

    // initAI - override vanilla AI
    protected void n() {
        // from EntityFish
        goalSelector.a(0, new AIPanic(this, 1.25D));
        goalSelector.a(2, new AIAvoidTarget<>(this, EntityPlayer.class, 8.0F, 1.6D, 1.4D, IEntitySelector.f));
        goalSelector.a(4, new AIFishSwim(this));

        // from EntityTropicalFish
        goalSelector.a(5, new AIFishFollowLeader(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return true;
    }

    // onLivingUpdate
    public void k() {
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
        return Config.TROPICAL_FISH_SPEED;
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
