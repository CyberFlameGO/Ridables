package net.pl3x.bukkit.ridables.entity.ai.polar_bear;

import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPolarBear;
import net.minecraft.server.v1_13_R2.PathfinderGoalNearestAttackableTarget;
import net.pl3x.bukkit.ridables.entity.RidablePolarBear;

public class AIPolarBearAttackPlayer extends PathfinderGoalNearestAttackableTarget<EntityHuman> {
    private final RidablePolarBear bear;

    public AIPolarBearAttackPlayer(RidablePolarBear bear) {
        super(bear, EntityHuman.class, 20, true, true, null);
        this.bear = bear;
    }

    // shouldExecute
    public boolean a() {
        if (bear.getRider() != null) {
            return false;
        }
        if (bear.isBaby()) {
            return false;
        }
        if (super.a()) {
            for (EntityPolarBear nearbyBear : bear.world.a(EntityPolarBear.class, bear.getBoundingBox().grow(8.0D, 4.0D, 8.0D))) {
                if (nearbyBear.isBaby()) {
                    return true;
                }
            }
        }
        bear.setGoalTarget(null);
        return false;
    }

    // shouldContinueExecuting
    public boolean b() {
        return bear.getRider() == null && super.b();
    }

    // getTargetDistance
    protected double i() {
        return super.i() * 0.5D;
    }
}
