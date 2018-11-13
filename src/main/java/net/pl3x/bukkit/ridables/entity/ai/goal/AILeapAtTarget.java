package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.PathfinderGoalLeapAtTarget;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AILeapAtTarget extends PathfinderGoalLeapAtTarget {
    private final RidableEntity ridable;

    public AILeapAtTarget(RidableEntity ridable, float motion) {
        super((EntityInsentient) ridable, motion);
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
