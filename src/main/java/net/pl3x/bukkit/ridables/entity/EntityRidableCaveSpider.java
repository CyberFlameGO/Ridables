package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerLook;
import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityCaveSpider;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.BlankLookController;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;

public class EntityRidableCaveSpider extends EntityCaveSpider implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASD wasdController;
    private ControllerLook defaultLookController;
    private BlankLookController blankLookController;
    private EntityPlayer rider;

    public EntityRidableCaveSpider(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASD(this);
        defaultLookController = lookController;
        blankLookController = new BlankLookController(this);
    }

    public RidableType getType() {
        return RidableType.CAVE_SPIDER;
    }

    public boolean aY() {
        return true; // dont eject passengers when in water
    }

    protected void mobTick() {
        EntityPlayer rider = updateRider();
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
        return Config.CAVE_SPIDER_JUMP_POWER;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.CAVE_SPIDER_SPEED;
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

    // isOnLadder
    public boolean z_() {
        if (getRider() == null) {
            return l(); // isBesideClimbableBlock
        }
        return Config.CAVE_SPIDER_CLIMB_WALLS && l();
    }

    public void a(float f, float f1, float f2) {
        super.a(f, f1, f2);
        if (positionChanged && z_() && getRider() != null) {
            motY = 0.2D * Config.CAVE_SPIDER_CLIMB_SPEED;
        }
    }
}
