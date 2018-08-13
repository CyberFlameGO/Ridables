package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EntityRabbit;
import net.minecraft.server.v1_13_R1.EnumHand;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class EntityRidableRabbit extends EntityRabbit implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASD wasdController;
    private boolean wasOnGround;

    public EntityRidableRabbit(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASD(this);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return f(CraftItemStack.asNMSCopy(itemstack));
    }

    public void mobTick() {
        EntityPlayer rider = getRider();
        if (rider != null) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
            handleJumping();
            return;
        }
        super.mobTick();
    }

    private void handleJumping() {
        if (onGround) {
            ControllerJumpRabbit jumpHelper = (ControllerJumpRabbit) h;
            if (!wasOnGround) {
                o(false); // setJumping
                jumpHelper.a(false); // setCanJump
            }
            if (!jumpHelper.c()) { // getIsJumping
                if (moveController.b()) { // isUpdating
                    dz(); // startJumping
                }
            } else if (!jumpHelper.d()) { // canJump
                jumpHelper.a(true); // setCanJump
            }
        }
        wasOnGround = onGround;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        if (getRider() == null) {
            return super.cG();
        }

        if (bj < 0) {
            r(bj * 2F);
        }
        return getJumpPower();
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getJumpPower() {
        return Config.RABBIT_JUMP_POWER * 0.75F;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.RABBIT_SPEED;
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

    public boolean onClick(org.bukkit.entity.Entity entity, EnumHand hand) {
        return false;
    }

    public boolean onClick(Block block, EnumHand hand) {
        return false;
    }

    public boolean onClick(EnumHand hand) {
        return false;
    }
}
