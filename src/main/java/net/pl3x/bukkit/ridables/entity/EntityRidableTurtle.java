package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EntityTurtle;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDWater;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class EntityRidableTurtle extends EntityTurtle implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASD wasdControllerLand;
    private ControllerWASDWater wasdControllerWater;

    public EntityRidableTurtle(World world) {
        super(world);
        persistent = true;
        aiController = moveController;
        wasdControllerLand = new ControllerWASD(this);
        wasdControllerWater = new ControllerWASDWater(this);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return f(CraftItemStack.asNMSCopy(itemstack));
    }

    @Override
    protected boolean isTypeNotPersistent() {
        return false;
    }

    protected void mobTick() {
        EntityPlayer rider = getRider();
        if (rider != null) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
            if (isInWater()) {
                motY += 0.005D;
            }
        }
        super.mobTick();
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return super.cG() * getJumpPower() * 2.2F;
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getJumpPower() {
        return 0.4F;
    }

    public float getSpeed() {
        float speed = (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue();
        return speed * (isInWater() ? Config.TURTLE_SPEED_WATER * 0.5F : Config.TURTLE_SPEED_LAND * 0.075F);
    }

    public EntityPlayer getRider() {
        if (passengers != null && !passengers.isEmpty()) {
            Entity entity = passengers.get(0);
            if (entity instanceof EntityPlayer) {
                return (EntityPlayer) entity;
            }
        }
        return null;
    }

    public void useAIController() {
        if (moveController != aiController) {
            moveController = aiController;
        }
    }

    public void useWASDController() {
        if (isInWater()) {
            if (moveController != wasdControllerWater) {
                moveController = wasdControllerWater;
            }
        } else {
            if (moveController != wasdControllerLand) {
                moveController = wasdControllerLand;
            }
        }
    }

    public boolean onSpacebar() {
        return false;
    }
}
