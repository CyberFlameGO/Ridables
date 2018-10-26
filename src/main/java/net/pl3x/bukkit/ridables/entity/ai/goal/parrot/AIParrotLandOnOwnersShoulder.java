package net.pl3x.bukkit.ridables.entity.ai.goal.parrot;

import net.minecraft.server.v1_13_R2.EntityPerchable;
import net.minecraft.server.v1_13_R2.PathfinderGoalPerch;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIParrotLandOnOwnersShoulder extends PathfinderGoalPerch {
    private final RidableEntity ridable;

    public AIParrotLandOnOwnersShoulder(RidableEntity ridable) {
        super((EntityPerchable) ridable);
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
