package net.pl3x.bukkit.ridables.entity.ai.wolf;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityLlama;
import net.minecraft.server.v1_13_R2.PathfinderGoalAvoidTarget;
import net.pl3x.bukkit.ridables.entity.RidableWolf;

public class AIWolfAvoidEntity<T extends Entity> extends PathfinderGoalAvoidTarget<T> {
    private final RidableWolf wolf;

    public AIWolfAvoidEntity(RidableWolf wolf, Class<T> avoidClass, float avoidDistance, double farSpeed, double nearSpeed) {
        super(wolf, avoidClass, avoidDistance, farSpeed, nearSpeed);
        this.wolf = wolf;
    }

    // shouldExecute
    public boolean a() {
        if (wolf.getRider() != null) {
            return false;
        }
        if (!super.a()) {
            return false;
        }
        if (!(b instanceof EntityLlama)) { // closestLivingEntity
            return false;
        }
        if (wolf.isTamed()) {
            return false;
        }
        return ((EntityLlama) b).getStrength() >= wolf.getRandom().nextInt(5);
    }

    // shouldContinueExecuting
    public boolean b() {
        return wolf.getRider() == null && super.b();
    }

    // startExecuting
    public void c() {
        wolf.setGoalTarget(null);
        super.c();
    }

    // tick
    public void e() {
        wolf.setGoalTarget(null);
        super.e();
    }
}
