package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityCod;
import net.minecraft.server.v1_13_R2.EntityFish;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumMoveType;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IEntitySelector;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.TagsFluid;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.fish.AIFishFollowLeader;
import net.pl3x.bukkit.ridables.entity.ai.fish.AIFishSwim;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDWater;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableCod extends EntityCod implements RidableEntity {
    public RidableCod(World world) {
        super(world);
        moveController = new FishWASDController(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.COD;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        goalSelector.a(0, new AIPanic(this, 1.25D));
        goalSelector.a(2, new AIAvoidTarget<>(this, EntityHuman.class, 8.0F, 1.6D, 1.4D, IEntitySelector.f));
        goalSelector.a(4, new AIFishSwim(this));
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
        return Config.COD_SPEED;
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

    public static class FishWASDController extends ControllerWASDWater {
        private final EntityFish fish;

        public FishWASDController(RidableEntity ridable) {
            super(ridable);
            this.fish = (EntityFish) ridable;
        }

        public void tick() {
            if (fish.a(TagsFluid.WATER)) {
                fish.motY += 0.005D;
            }
            if (this.h == ControllerMove.Operation.MOVE_TO && !fish.getNavigation().p()) {
                double x = b - fish.locX;
                double y = c - fish.locY;
                double z = d - fish.locZ;
                y /= (double) MathHelper.sqrt(x * x + y * y + z * z);
                fish.yaw = a(fish.yaw, (float) (MathHelper.c(z, x) * (double) (180F / (float) Math.PI)) - 90.0F, 90.0F);
                fish.aQ = fish.yaw;
                fish.o(fish.cK() + ((float) (e * fish.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()) - fish.cK()) * 0.125F);
                fish.motY += (double) fish.cK() * y * 0.1D;
            } else {
                fish.o(0.0F);
            }
        }
    }
}
