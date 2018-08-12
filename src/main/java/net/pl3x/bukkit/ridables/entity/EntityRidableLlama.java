package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityLlama;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.listener.RideListener;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class EntityRidableLlama extends EntityLlama implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASD wasdController;

    public EntityRidableLlama(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASD(this);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return f(CraftItemStack.asNMSCopy(itemstack));
    }

    protected void mobTick() {
        EntityPlayer rider = getRider();
        if (rider != null) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
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
        return Config.LLAMA_JUMP_POWER;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.LLAMA_SPEED;
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
        if (moveController != wasdController) {
            moveController = wasdController;
        }
    }

    public boolean onSpacebar() {
        return false;
    }

    // mountTo
    protected void g(EntityHuman entityhuman) {
        RideListener.override.add(entityhuman.getUniqueID());
        super.g(entityhuman);
        RideListener.override.remove(entityhuman.getUniqueID());
    }

    public boolean isLeashed() {
        return getRider() != null || super.isLeashed();
    }

    public Entity getLeashHolder() {
        EntityPlayer rider = getRider();
        return rider != null ? rider : super.getLeashHolder();
    }

    // hasCaravan
    public boolean en() {
        return (getRider() != null && Config.LLAMA_CARAVAN) || super.en();
    }
}
