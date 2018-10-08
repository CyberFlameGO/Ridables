package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomStrollLand;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIWanderAvoidWater extends PathfinderGoalRandomStrollLand {
    private final RidableEntity ridable;

    public AIWanderAvoidWater(RidableEntity ridable, double var2) {
        this(ridable, var2, 0.001F);
    }

    public AIWanderAvoidWater(RidableEntity ridable, double speed, float probability) {
        super((EntityCreature) ridable, speed, probability);
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
