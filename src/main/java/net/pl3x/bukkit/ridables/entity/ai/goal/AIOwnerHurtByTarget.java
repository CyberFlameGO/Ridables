package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.EntityTameableAnimal;
import net.minecraft.server.v1_13_R2.PathfinderGoalOwnerHurtByTarget;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIOwnerHurtByTarget extends PathfinderGoalOwnerHurtByTarget {
    private final RidableEntity entity;

    public AIOwnerHurtByTarget(RidableEntity entity) {
        super((EntityTameableAnimal) entity);
        this.entity = entity;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return entity.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return entity.getRider() == null && super.b();
    }
}
