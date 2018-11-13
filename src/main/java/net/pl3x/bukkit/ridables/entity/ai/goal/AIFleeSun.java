package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoalFleeSun;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIFleeSun extends PathfinderGoalFleeSun {
    private final RidableEntity ridable;

    public AIFleeSun(RidableEntity ridable, double speed) {
        super((EntityCreature) ridable, speed);
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
