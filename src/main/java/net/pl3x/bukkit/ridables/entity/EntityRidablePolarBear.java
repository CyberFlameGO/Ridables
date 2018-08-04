package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EntityPolarBear;
import net.minecraft.server.v1_13_R1.EnumMoveType;
import net.minecraft.server.v1_13_R1.MathHelper;
import net.minecraft.server.v1_13_R1.MobEffect;
import net.minecraft.server.v1_13_R1.MobEffects;
import net.minecraft.server.v1_13_R1.SoundEffects;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;

import java.lang.reflect.Field;

public class EntityRidablePolarBear extends EntityPolarBear {
    private static Field jumping;
    private boolean isJumping = false;

    public EntityRidablePolarBear(World world) {
        super(world);
        Q = Config.POLAR_BEAR_STEP_HEIGHT; // stepHeight
        persistent = true;

        if (jumping == null) {
            try {
                jumping = EntityLiving.class.getDeclaredField("bg");
                jumping.setAccessible(true);
            } catch (NoSuchFieldException ignore) {
            }
        }
    }

    @Override
    protected boolean isTypeNotPersistent() {
        return false; // we definitely want persistence
    }

    // travel(strafe, vertical, forward)
    @Override
    public void a(float f, float f1, float f2) {
        EntityPlayer rider = getRider();
        if (rider != null) {
            Q = Config.POLAR_BEAR_STEP_HEIGHT; // stepHeight

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
                motY = (double) Config.POLAR_BEAR_JUMP_POWER;
                if (hasEffect(MobEffects.JUMP)) {
                    motY += (double) ((float) (getEffect(MobEffects.JUMP).getAmplifier() + 1) * 0.1F);
                }
                isJumping = true; // setJumping
                impulse = true;
                if (forward > 0.0F) {
                    motX += (double) (-0.4F * MathHelper.sin(yaw * 0.017453292F) * Config.POLAR_BEAR_JUMP_POWER);
                    motZ += (double) (0.4F * MathHelper.cos(yaw * 0.017453292F) * Config.POLAR_BEAR_JUMP_POWER);
                    //a(SoundEffects.ENTITY_POLAR_BEAR_STEP, 0.4F, 1.0F);
                }
            }

            // move
            customMove(strafe, f1, forward);

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
        return null; // aww, lonely turtle is lonely
    }

    private void customMove(float strafe, float vertical, float forward) {
        o((forward != 0 || strafe != 0 ? 0.2F : 0F));
        double gravity = 0.08D;
        if (motY <= 0.0D && hasEffect(MobEffects.SLOW_FALLING)) {
            gravity = 0.01D;
            fallDistance = 0.0F;
        }
        BlockPosition.b blockposition_b = BlockPosition.b.d(locX, getBoundingBox().b - 1.0D, locZ);
        Throwable throwable = null;
        try {
            float blockFriction = onGround ? world.getType(blockposition_b).getBlock().n() * 0.91F : 0.91F;
            float friction = 0.16277137F / (blockFriction * blockFriction * blockFriction);
            a(strafe, vertical, forward, (onGround ? cK() * friction : aU) * Config.POLAR_BEAR_SPEED); // moveRelative
            blockFriction = onGround ? world.getType(blockposition_b.e(locX, getBoundingBox().b - 1.0D, locZ)).getBlock().n() * 0.91F : 0.91F;
            if (z_()) { // isOnLadder
                motX = MathHelper.a(motX, -0.15D, 0.15D);
                motZ = MathHelper.a(motZ, -0.15D, 0.15D);
                fallDistance = 0.0F;
                if (motY < -0.15D) {
                    motY = -0.15D;
                }
            }
            move(EnumMoveType.SELF, motX, motY, motZ);
            if (positionChanged && z_()) { // isOnLadder
                motY = 0.2D;
            }
            MobEffect levitation = getEffect(MobEffects.LEVITATION);
            if (levitation != null) {
                motY += (0.05D * (double) (levitation.getAmplifier() + 1) - motY) * 0.2D;
                fallDistance = 0.0F;
            } else {
                blockposition_b.e(locX, 0.0D, locZ);
                if (world.isClientSide && (!world.isLoaded(blockposition_b) || !world.getChunkAtWorldCoords(blockposition_b).y())) {
                    motY = locY > 0.0D ? -0.1D : 0.0D;
                } else if (!isNoGravity()) {
                    motY -= gravity;
                }
            }

            motY *= 0.98D;
            motX *= (double) blockFriction;
            motZ *= (double) blockFriction;
        } catch (Throwable throwable1) {
            throwable = throwable1;
            throw throwable1;
        } finally {
            if (blockposition_b != null) {
                if (throwable != null) {
                    try {
                        blockposition_b.close();
                    } catch (Throwable throwable2) {
                        throwable.addSuppressed(throwable2);
                    }
                } else {
                    blockposition_b.close();
                }
            }

        }

        aI = aJ; // prevLimbSwingAmount = limbSwingAmount;
        double d0 = locX - lastX;
        double d2 = locZ - lastZ;
        float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2) * 4.0F;
        if (f3 > 1.0F) {
            f3 = 1.0F;
        }
        aJ += (f3 - aJ) * 0.4F; // limbSwingAmount
        aK += aJ; // limbSwing += limbSwingAmount
    }
}
