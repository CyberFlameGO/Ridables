package net.pl3x.bukkit.ridables.entity.ai.goal.dolphin;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoalWater;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIDolphinFindWater extends PathfinderGoalWater {
    private final RidableEntity ridable;

    public AIDolphinFindWater(RidableEntity ridable) {
        super((EntityCreature) ridable);
        this.ridable = ridable;
    }

    // shouldExecute
    public boolean a() {
        return ridable.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }
}
