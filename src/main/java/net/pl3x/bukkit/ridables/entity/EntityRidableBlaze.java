package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerLook;
import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityBlaze;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.BlankLookController;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlyingWithSpacebar;

public class EntityRidableBlaze extends EntityBlaze implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASDFlyingWithSpacebar wasdController;
    private ControllerLook defaultLookController;
    private BlankLookController blankLookController;
    private EntityPlayer rider;

    public EntityRidableBlaze(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASDFlyingWithSpacebar(this);
        defaultLookController = lookController;
        blankLookController = new BlankLookController(this);
    }

    public RidableType getType() {
        return RidableType.BLAZE;
    }

    public boolean aY() {
        return true;
    }

    protected void mobTick() {
        EntityPlayer rider = updateRider();
        if (rider != null) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
            motY += bi > 0 ? 0.07F * Config.BLAZE_VERTICAL : 0.04704F - Config.BLAZE_GRAVITY;
            return;
        }
        super.mobTick();
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.BLAZE_SPEED * 2;
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
            lookController = defaultLookController;
        }
    }

    public void useWASDController() {
        if (moveController != wasdController) {
            moveController = wasdController;
            lookController = blankLookController;
        }
    }
}
