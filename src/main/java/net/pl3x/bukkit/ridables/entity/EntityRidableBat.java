package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityBat;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EnumMoveType;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class EntityRidableBat extends EntityBat implements RidableEntity {
    private static Field jumping;

    public EntityRidableBat(World world) {
        super(world);

        if (jumping == null) {
            try {
                jumping = EntityLiving.class.getDeclaredField("bg");
                jumping.setAccessible(true);
            } catch (NoSuchFieldException ignore) {
            }
        }
    }

    public boolean isFood(ItemStack itemstack) {
        return false;
    }

    protected void mobTick() {
        EntityPlayer rider = getRider();
        if (rider != null) {
            if (isAsleep()) {
                setAsleep(false);
            }

            // rotation
            setYawPitch(lastYaw = yaw = rider.yaw, pitch = rider.pitch * 0.5F);
            aS = aQ = yaw;

            // controls
            float forward = rider.bj;
            float strafe = rider.bh * 0.5F;
            float vertical = 0;
            if (forward <= 0.0F) {
                forward *= 0.25F;
            }

            if (jumping != null) {
                try {
                    if (jumping.getBoolean(rider)) {
                        vertical = 0.98F;
                    }
                } catch (IllegalAccessException ignore) {
                }
            }

            if (motY == -0.04704000278472904) {
                a(strafe, vertical, forward, 0.04F * Config.BAT_SPEED);
            } else {
                a(strafe, vertical, forward * (vertical > 0 ? 1F : 0.5F), 0.025F * Config.BAT_SPEED);
            }
            motY += vertical > 0 ? 0.07F * Config.BAT_VERTICAL : 0.04704F - Config.BAT_GRAVITY;
            move(EnumMoveType.PLAYER, motX, motY, motZ);
            return;
        }
        super.mobTick();
    }

    private EntityPlayer getRider() {
        if (passengers != null && !passengers.isEmpty()) {
            Entity entity = passengers.get(0); // only care about first rider
            if (entity instanceof EntityPlayer) {
                return (EntityPlayer) entity;
            }
        }
        return null; // aww, lonely bat is lonely
    }
}
