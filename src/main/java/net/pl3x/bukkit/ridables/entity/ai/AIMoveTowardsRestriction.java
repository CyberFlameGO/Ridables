package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoalMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIMoveTowardsRestriction extends PathfinderGoalMoveTowardsRestriction {
    private final RidableEntity ridable;

    public AIMoveTowardsRestriction(RidableEntity ridable, double speed) {
        super((EntityCreature) ridable, speed);
        this.ridable = ridable;
    }

    // shouldExecute
    public boolean a() {
        return ridable.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return ridable.getRider() == null && super.b();
    }
}
