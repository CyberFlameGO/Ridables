package net.pl3x.bukkit.ridables.entity.ai.horse;

import net.minecraft.server.v1_13_R2.EntityHorseAbstract;
import net.minecraft.server.v1_13_R2.PathfinderGoalTame;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIHorseBucking extends PathfinderGoalTame {
    public AIHorseBucking(RidableEntity ridable, double speed) {
        super((EntityHorseAbstract) ridable, speed);
    }
}
