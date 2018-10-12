package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoalRestrictOpenDoor;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIRestrictOpenDoor extends PathfinderGoalRestrictOpenDoor {
    private final RidableEntity ridable;

    public AIRestrictOpenDoor(RidableEntity ridable) {
        super((EntityCreature) ridable);
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
