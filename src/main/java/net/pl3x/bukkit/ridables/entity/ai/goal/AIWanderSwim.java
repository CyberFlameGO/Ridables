package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomSwim;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIWanderSwim extends PathfinderGoalRandomSwim {
    private final RidableEntity ridable;

    public AIWanderSwim(RidableEntity ridable, double speed) {
        this(ridable, speed, 120);
    }

    public AIWanderSwim(RidableEntity ridable, double speed, int chance) {
        super((EntityCreature) ridable, speed, chance);
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
