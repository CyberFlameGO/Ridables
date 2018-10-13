package net.pl3x.bukkit.ridables.entity.ai.horse.skeleton_horse;

import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.RidableSkeletonHorse;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;

public class AISkeletonHorseSwim extends AISwim {
    public AISkeletonHorseSwim(RidableSkeletonHorse horse) {
        super(horse);
    }

    // shouldExecute
    public boolean a() {
        return Config.SKELETON_HORSE_FLOATS_IN_WATER && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }
}
