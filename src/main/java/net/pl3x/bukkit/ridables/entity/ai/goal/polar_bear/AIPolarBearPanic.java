package net.pl3x.bukkit.ridables.entity.ai.goal.polar_bear;

import net.minecraft.server.v1_13_R2.PathfinderGoalPanic;
import net.pl3x.bukkit.ridables.entity.animal.RidablePolarBear;

public class AIPolarBearPanic extends PathfinderGoalPanic {
    private final RidablePolarBear bear;

    public AIPolarBearPanic(RidablePolarBear bear) {
        super(bear, 2.0D);
        this.bear = bear;
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (bear.getRider() != null) {
            return false;
        }
        if (!bear.isBaby() && !bear.isBurning()) {
            return false;
        }
        return super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return bear.getRider() == null && super.b();
    }
}
