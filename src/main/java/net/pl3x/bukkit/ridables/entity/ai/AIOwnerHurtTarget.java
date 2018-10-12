package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityTameableAnimal;
import net.minecraft.server.v1_13_R2.PathfinderGoalOwnerHurtTarget;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIOwnerHurtTarget extends PathfinderGoalOwnerHurtTarget {
    private final RidableEntity entity;

    public AIOwnerHurtTarget(RidableEntity entity) {
        super((EntityTameableAnimal) entity);
        this.entity = entity;
    }

    // shouldExecute
    public boolean a() {
        return entity.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return entity.getRider() == null && super.b();
    }
}
