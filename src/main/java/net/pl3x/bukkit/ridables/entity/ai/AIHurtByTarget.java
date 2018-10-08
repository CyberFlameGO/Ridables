package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoalHurtByTarget;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIHurtByTarget extends PathfinderGoalHurtByTarget {
    private final RidableEntity ridable;

    public AIHurtByTarget(RidableEntity ridable, boolean flag, Class<?>... aclass) {
        super((EntityCreature) ridable, flag, aclass);
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
