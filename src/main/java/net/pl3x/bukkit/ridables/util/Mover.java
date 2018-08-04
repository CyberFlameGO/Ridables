package net.pl3x.bukkit.ridables.util;

import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EnumMoveType;
import net.minecraft.server.v1_13_R1.MathHelper;
import net.minecraft.server.v1_13_R1.MobEffect;
import net.minecraft.server.v1_13_R1.MobEffects;

public class Mover {
    public static void moveOnLand(EntityLiving creature, float strafe, float vertical, float forward, float speed) {
        creature.o((forward != 0 || strafe != 0 ? 0.2F : 0F));
        double gravity = 0.08D;
        if (creature.motY <= 0.0D && creature.hasEffect(MobEffects.SLOW_FALLING)) {
            gravity = 0.01D;
            creature.fallDistance = 0.0F;
        }
        BlockPosition.b blockposition_b = BlockPosition.b.d(creature.locX, creature.getBoundingBox().b - 1.0D, creature.locZ);
        Throwable throwable = null;
        try {
            float blockFriction = creature.onGround ? creature.world.getType(blockposition_b).getBlock().n() * 0.91F : 0.91F;
            float friction = 0.16277137F / (blockFriction * blockFriction * blockFriction);
            creature.a(strafe, vertical, forward, (creature.onGround ? creature.cK() * friction : creature.aU) * speed); // moveRelative
            blockFriction = creature.onGround ? creature.world.getType(blockposition_b.e(creature.locX, creature.getBoundingBox().b - 1.0D, creature.locZ)).getBlock().n() * 0.91F : 0.91F;
            if (creature.z_()) { // isOnLadder
                creature.motX = MathHelper.a(creature.motX, -0.15D, 0.15D);
                creature.motZ = MathHelper.a(creature.motZ, -0.15D, 0.15D);
                creature.fallDistance = 0.0F;
                if (creature.motY < -0.15D) {
                    creature.motY = -0.15D;
                }
            }
            creature.move(EnumMoveType.SELF, creature.motX, creature.motY, creature.motZ);
            if (creature.positionChanged && creature.z_()) { // isOnLadder
                creature.motY = 0.2D;
            }
            MobEffect levitation = creature.getEffect(MobEffects.LEVITATION);
            if (levitation != null) {
                creature.motY += (0.05D * (double) (levitation.getAmplifier() + 1) - creature.motY) * 0.2D;
                creature.fallDistance = 0.0F;
            } else {
                blockposition_b.e(creature.locX, 0.0D, creature.locZ);
                if (creature.world.isClientSide && (!creature.world.isLoaded(blockposition_b) || !creature.world.getChunkAtWorldCoords(blockposition_b).y())) {
                    creature.motY = creature.locY > 0.0D ? -0.1D : 0.0D;
                } else if (!creature.isNoGravity()) {
                    creature.motY -= gravity;
                }
            }

            creature.motY *= 0.98D;
            creature.motX *= (double) blockFriction;
            creature.motZ *= (double) blockFriction;
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

        creature.aI = creature.aJ; // prevLimbSwingAmount = limbSwingAmount;
        double d0 = creature.locX - creature.lastX;
        double d2 = creature.locZ - creature.lastZ;
        float f3 = MathHelper.sqrt(d0 * d0 + d2 * d2) * 4.0F;
        if (f3 > 1.0F) {
            f3 = 1.0F;
        }
        creature.aJ += (f3 - creature.aJ) * 0.4F; // limbSwingAmount
        creature.aK += creature.aJ; // limbSwing += limbSwingAmount
    }
}
