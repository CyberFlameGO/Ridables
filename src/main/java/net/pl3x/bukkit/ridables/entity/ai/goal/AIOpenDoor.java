package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.PathfinderGoalOpenDoor;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIOpenDoor extends PathfinderGoalOpenDoor {
    private final RidableEntity ridable;

    public AIOpenDoor(RidableEntity ridable, boolean shouldClose) {
        super((EntityInsentient) ridable, shouldClose);
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
