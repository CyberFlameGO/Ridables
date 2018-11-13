package net.pl3x.bukkit.ridables.entity.ai.goal.parrot;

import net.minecraft.server.v1_13_R2.EntityTameableAnimal;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomFly;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIParrotWanderAvoidWater extends PathfinderGoalRandomFly {
    private final RidableEntity ridable;

    public AIParrotWanderAvoidWater(RidableEntity ridable, double speed) {
        super((EntityTameableAnimal) ridable, speed);
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
