package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerLook;
import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityGuardian;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.BlankLookController;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDWater;

public class EntityRidableGuardian extends EntityGuardian implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASDWater wasdController;
    private ControllerLook defaultLookController;
    private BlankLookController blankLookController;

    public EntityRidableGuardian(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASDWater(this);
        defaultLookController = lookController;
        blankLookController = new BlankLookController(this);
    }

    public boolean aY() {
        return true;
    }

    protected void mobTick() {
        EntityPlayer rider = getRider();
        if (rider != null && getAirTicks() > 150) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
            motY += 0.005F;
        }
        super.mobTick();
    }

    @Override
    public void a(float f, float f1, float f2) {
        EntityPlayer rider = getRider();
        if (rider != null && !isInWater()) {
            f2 = rider.bj;
            f = rider.bh;
        }
        super.a(f, f1, f2);
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.GUARDIAN_SPEED;
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
            lookController = defaultLookController;
        }
    }

    public void useWASDController() {
        if (moveController != wasdController) {
            moveController = wasdController;
            lookController = blankLookController;
        }
    }

    public boolean onSpacebar() {
        world.broadcastEntityEffect(this, (byte) 21);
        return true;
    }
}
