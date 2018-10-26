package net.pl3x.bukkit.ridables.entity.ai.goal.zombie;

import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.PathfinderGoalBreakDoor;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIZombieBreakDoor extends PathfinderGoalBreakDoor {
    private final RidableEntity ridable;

    public AIZombieBreakDoor(RidableEntity ridable) {
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
