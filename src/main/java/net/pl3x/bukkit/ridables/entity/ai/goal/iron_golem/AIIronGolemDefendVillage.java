package net.pl3x.bukkit.ridables.entity.ai.goal.iron_golem;

import net.minecraft.server.v1_13_R2.PathfinderGoalDefendVillage;
import net.pl3x.bukkit.ridables.entity.animal.RidableIronGolem;

public class AIIronGolemDefendVillage extends PathfinderGoalDefendVillage {
    private final RidableIronGolem golem;

    public AIIronGolemDefendVillage(RidableIronGolem golem) {
        super(golem);
        this.golem = golem;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return golem.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return golem.getRider() == null && super.b();
    }
}
