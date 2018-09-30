package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.DragonControllerPhase;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityEnderDragon;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumMoveType;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlying;
import net.pl3x.bukkit.ridables.util.ItemUtil;

public class RidableEnderDragon extends EntityEnderDragon implements RidableEntity {
    private EntityPlayer rider;
    private boolean dirty;

    public RidableEnderDragon(World world) {
        super(world);
        moveController = new ControllerWASDFlying(this);
    }

    public RidableType getType() {
        return RidableType.ENDER_DRAGON;
    }

    // canBeRiddenInWater
    public boolean aY() {
        return false;
    }

    // onLivingUpdate
    @Override
    public void k() {
        EntityPlayer rider = updateRider();
        if (rider != null) {
            if (!dirty) {
                dirty = true;
                noclip = false;
                setSize(4.0F, 2.0F);
            }

            setRotation(rider.yaw, rider.pitch);
            useWASDController();

            moveController.a(); // ender dragon doesnt use the controller so call manually

            a(-bh, bi, -bj, getSpeed() * 0.1F); // moveRelative
            move(EnumMoveType.PLAYER, motX, motY, motZ);

            motX *= 0.9F;
            motY *= 0.9F;
            motZ *= 0.9F;

            // control wing flap speed
            getDragonControllerManager().setControllerPhase(motX * motX + motZ * motZ < 0.005F ? DragonControllerPhase.k : DragonControllerPhase.a);

            return;
        } else if (dirty) {
            dirty = false;
            noclip = true;
            setSize(16.0F, 8.0F);
            getDragonControllerManager().setControllerPhase(DragonControllerPhase.a); // HoldingPattern
        }
        super.k();
    }

    public void setRotation(float yaw, float pitch) {
        setYawPitch(lastYaw = this.yaw = yaw - 180, this.pitch = pitch * 0.5F);
        aS = aQ = this.yaw;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.DRAGON_SPEED;
    }

    public EntityPlayer getRider() {
        return rider;
    }

    public EntityPlayer updateRider() {
        if (passengers == null || passengers.isEmpty()) {
            return rider = null;
        }
        Entity entity = passengers.get(0);
        return rider = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
    }

    public void useAIController() {
    }

    public void useWASDController() {
    }

    public boolean onSpacebar() {
        // TODO flames!
        return true;
    }

    // processInteract
    public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        if (passengers.isEmpty() && !entityhuman.isPassenger() && !entityhuman.isSneaking() && ItemUtil.isEmptyOrSaddle(entityhuman)) {
            return enumhand == EnumHand.MAIN_HAND && tryRide(entityhuman);
        }
        return passengers.isEmpty() && super.a(entityhuman, enumhand);
    }
}
