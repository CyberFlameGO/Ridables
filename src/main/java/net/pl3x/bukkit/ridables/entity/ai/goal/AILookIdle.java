package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomLookaround;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AILookIdle extends PathfinderGoalRandomLookaround {
    private final RidableEntity ridable;

    public AILookIdle(RidableEntity ridable) {
        super((EntityInsentient) ridable);
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
