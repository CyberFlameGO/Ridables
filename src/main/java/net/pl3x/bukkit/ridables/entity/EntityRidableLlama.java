package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityLlama;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.MathHelper;
import net.minecraft.server.v1_13_R1.MobEffect;
import net.minecraft.server.v1_13_R1.MobEffects;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.listener.RideListener;
import net.pl3x.bukkit.ridables.util.Mover;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;

public class EntityRidableLlama extends EntityLlama implements RidableEntity {
    private static Field jumping;
    private boolean isJumping = false;

    public EntityRidableLlama(World world) {
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
        return f(CraftItemStack.asNMSCopy(itemstack));
    }

    @Override
    public void a(float f, float f1, float f2) {
        EntityPlayer rider = getRider();
        if (rider != null) {
            // do not target anything while being ridden
            setGoalTarget(null, null, false);

            // eject rider if in water or lava
            if (isInWater() || ax()) {
                ejectPassengers();
                rider.stopRiding();
                return;
            }

            // rotation
            setYawPitch(lastYaw = yaw = rider.yaw, pitch = rider.pitch * 0.5F);
            aS = aQ = yaw;

            // controls
            float forward = rider.bj;
            float strafe = rider.bh * 0.5F;
            if (forward <= 0.0F) {
                forward *= 0.25F;
            }

            if (jumping != null && !isJumping) {
                try {
                    isJumping = jumping.getBoolean(rider);
                } catch (IllegalAccessException ignore) {
                }
            }

            if (isJumping && onGround) { // !isJumping
                motY = (double) Config.LLAMA_JUMP_POWER;
                MobEffect jump = getEffect(MobEffects.JUMP);
                if (jump != null) {
                    motY += (double) ((float) (jump.getAmplifier() + 1) * 0.1F);
                }
                impulse = true;
                if (forward > 0.0F) {
                    motX += (double) (-0.4F * MathHelper.sin(yaw * 0.017453292F) * Config.LLAMA_JUMP_POWER);
                    motZ += (double) (0.4F * MathHelper.cos(yaw * 0.017453292F) * Config.LLAMA_JUMP_POWER);
                }
            }

            // move
            Mover.moveOnLand(this, strafe, f1, forward, Config.LLAMA_SPEED);

            if (onGround) {
                isJumping = false;
            }
            return;
        }
        super.a(f, f1, f2);
    }

    private EntityPlayer getRider() {
        if (passengers != null && !passengers.isEmpty()) {
            Entity entity = passengers.get(0); // only care about first rider
            if (entity instanceof EntityPlayer) {
                return (EntityPlayer) entity;
            }
        }
        return null; // aww, lonely llama is lonely
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(0.225D);
    }

    // canBeSteered
    public boolean di() {
        return bO() instanceof EntityLiving;
    }

    // isHorseSaddled
    public boolean dW() {
        return true;
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
