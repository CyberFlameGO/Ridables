package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoalMoveTowardsTarget;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIMoveTowardsTarget extends PathfinderGoalMoveTowardsTarget {
    private final RidableEntity ridable;

    public AIMoveTowardsTarget(RidableEntity ridable, double speed, float targetMaxDistance) {
        super((EntityCreature) ridable, speed, targetMaxDistance);
        this.ridable = ridable;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return ridable.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return ridable.getRider() == null && super.b();
    }
}
