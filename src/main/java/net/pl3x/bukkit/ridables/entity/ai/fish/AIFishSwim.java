package net.pl3x.bukkit.ridables.entity.ai.fish;

import net.minecraft.server.v1_13_R2.EntityFish;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomSwim;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIFishSwim extends PathfinderGoalRandomSwim {
    private final RidableEntity ridable;

    public AIFishSwim(RidableEntity ridable) {
        super((EntityFish) ridable, 1.0D, 40);
        this.ridable = ridable;
    }

    // shouldExecute
    public boolean a() {
        return ridable.getRider() == null && !((EntityFish) a).dz() && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return ridable.getRider() == null && super.b();
    }
}
