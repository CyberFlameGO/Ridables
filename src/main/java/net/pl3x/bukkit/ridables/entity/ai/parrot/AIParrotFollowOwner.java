package net.pl3x.bukkit.ridables.entity.ai.parrot;

import net.minecraft.server.v1_13_R2.EntityTameableAnimal;
import net.minecraft.server.v1_13_R2.PathfinderGoalFollowOwner;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIParrotFollowOwner extends PathfinderGoalFollowOwner {
    private final RidableEntity ridable;

    public AIParrotFollowOwner(RidableEntity ridable, double speed, float minDistance, float maxDistance) {
        super((EntityTameableAnimal) ridable, speed, minDistance, maxDistance);
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
