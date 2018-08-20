package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EntityTropicalFish;
import net.minecraft.server.v1_13_R1.EnumMoveType;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDWater;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class EntityRidableTropicalFish extends EntityTropicalFish implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASDWater wasdController;
    private EntityPlayer rider;

    public EntityRidableTropicalFish(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASDWater(this);
    }

    public RidableType getType() {
        return RidableType.TROPICAL_FISH;
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return itemstack.getType() == Material.WATER_BUCKET;
    }

    public boolean aY() {
        return true;
    }

    public void k() {
        EntityPlayer rider = updateRider();
        if (rider != null) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
            motY += 0.005D;
        }
        super.k();
    }

    public void a(float f, float f1, float f2) {
        EntityPlayer rider = getRider();
        if (rider != null) {
            if (!isInWater()) {
                f2 = rider.bj;
                f = rider.bh;
            }
        }
        if (cP() && this.isInWater()) {
            a(f, f1, f2, rider == null ? 0.01F : getSpeed());
            move(EnumMoveType.SELF, motX, motY, motZ);
            motX *= 0.8999999761581421D;
            motY *= 0.8999999761581421D;
            motZ *= 0.8999999761581421D;
            if (getGoalTarget() == null) {
                motY -= 0.005D;
            }
            return;
        }
        super.a(f, f1, f2);
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.TROPICAL_FISH_SPEED * 0.25F;
    }

    public EntityPlayer getRider() {
        return rider;
    }

    public EntityPlayer updateRider() {
        if (passengers == null || passengers.isEmpty()) {
            rider = null;
        } else {
            Entity entity = passengers.get(0);
            rider = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
        }
        return rider;
    }

    public void useAIController() {
        if (moveController != aiController) {
            moveController = aiController;
        }
    }

    public void useWASDController() {
        if (moveController != wasdController) {
            moveController = wasdController;
        }
    }
}
