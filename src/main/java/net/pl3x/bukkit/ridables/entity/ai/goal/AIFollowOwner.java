package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.EntityTameableAnimal;
import net.minecraft.server.v1_13_R2.PathfinderGoalFollowOwner;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIFollowOwner extends PathfinderGoalFollowOwner {
    private final RidableEntity ridable;

    public AIFollowOwner(RidableEntity ridable, double speed, float minDistance, float maxDistance) {
        super((EntityTameableAnimal) ridable, speed, minDistance, maxDistance);
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
