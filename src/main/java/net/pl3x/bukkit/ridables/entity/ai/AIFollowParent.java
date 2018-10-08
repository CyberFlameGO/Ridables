package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityAnimal;
import net.minecraft.server.v1_13_R2.PathfinderGoalFollowParent;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIFollowParent extends PathfinderGoalFollowParent {
    private final RidableEntity ridable;

    public AIFollowParent(RidableEntity ridable, double speed) {
        super((EntityAnimal) ridable, speed);
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
