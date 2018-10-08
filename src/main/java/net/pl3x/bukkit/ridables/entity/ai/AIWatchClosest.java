package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.PathfinderGoalLookAtPlayer;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIWatchClosest extends PathfinderGoalLookAtPlayer {
    private final RidableEntity ridable;

    public AIWatchClosest(RidableEntity ridable, Class<? extends Entity> targetClass, float maxDistance) {
        this(ridable, targetClass, maxDistance, 0.02F);
    }

    public AIWatchClosest(RidableEntity ridable, Class<? extends Entity> targetClass, float maxDistance, float chance) {
        super((EntityInsentient) ridable, targetClass, maxDistance, chance);
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
