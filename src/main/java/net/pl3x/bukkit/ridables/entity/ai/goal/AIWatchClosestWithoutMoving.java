package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.PathfinderGoalInteract;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIWatchClosestWithoutMoving extends PathfinderGoalInteract {
    private final RidableEntity ridable;

    public AIWatchClosestWithoutMoving(RidableEntity ridable, Class<? extends Entity> targetClass, float maxDistance, float chance) {
        super((EntityInsentient) ridable, targetClass, maxDistance, chance);
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
