package net.pl3x.bukkit.ridables.entity.ai.blaze;

import net.minecraft.server.v1_13_R2.AttributeInstance;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntitySmallFireball;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.RidableBlaze;

public class AIBlazeFireballAttack extends PathfinderGoal {
    private final RidableBlaze blaze;
    private int attackStep;
    private int attackTime;

    public AIBlazeFireballAttack(RidableBlaze blaze) {
        this.blaze = blaze;
        a(3); // setMutexBits
    }

    // shouldExecute
    public boolean a() {
        if (blaze.getRider() != null) {
            return false;
        }
        EntityLiving target = blaze.getGoalTarget();
        return target != null && target.isAlive();
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // startExecuting
    public void c() {
        attackStep = 0;
    }

    // resetTask
    public void d() {
        blaze.a(false); // setOnFire
    }

    // tick
    public void e() {
        --attackTime;
        EntityLiving target = blaze.getGoalTarget();
        double distance = blaze.h(target);
        if (distance < 4.0D) {
            if (attackTime <= 0) {
                attackTime = 20;
                blaze.B(target); // attackEnemyAsMob
            }

            blaze.getControllerMove().a(target.locX, target.locY, target.locZ, 1.0D);
        } else if (distance < g() * g()) {
            double x = target.locX - blaze.locX;
            double y = target.getBoundingBox().b + (double) (target.length / 2.0F) - (blaze.locY + (double) (blaze.length / 2.0F));
            double z = target.locZ - blaze.locZ;
            if (attackTime <= 0) {
                ++attackStep;
                if (attackStep == 1) {
                    attackTime = 60;
                    blaze.a(true); // setOnFire
                } else if (attackStep <= 4) {
                    attackTime = 6;
                } else {
                    attackTime = 100;
                    attackStep = 0;
                    blaze.a(false); // setOnFire
                }
                if (attackStep > 1) {
                    float f = MathHelper.c(MathHelper.sqrt(distance)) * 0.5F;
                    blaze.world.a(null, 1018, new BlockPosition((int) blaze.locX, (int) blaze.locY, (int) blaze.locZ), 0);
                    for (int i = 0; i < 1; ++i) {
                        EntitySmallFireball fireball = new EntitySmallFireball(blaze.world, blaze, x + blaze.getRandom().nextGaussian() * (double) f, y, z + blaze.getRandom().nextGaussian() * (double) f);
                        fireball.locY = blaze.locY + (double) (blaze.length / 2.0F) + 0.5D;
                        blaze.world.addEntity(fireball);
                    }
                }
            }
            blaze.getControllerLook().a(target, 10.0F, 10.0F);
        } else {
            blaze.getNavigation().q(); // clearPath
            blaze.getControllerMove().a(target.locX, target.locY, target.locZ, 1.0D);
        }
        super.e();
    }

    // getFollowDistance
    private double g() {
        AttributeInstance range = blaze.getAttributeInstance(GenericAttributes.FOLLOW_RANGE);
        return range == null ? 16.0D : range.getValue();
    }
}