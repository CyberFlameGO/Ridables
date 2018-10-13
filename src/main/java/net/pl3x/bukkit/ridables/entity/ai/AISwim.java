package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.PathfinderGoalFloat;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AISwim extends PathfinderGoalFloat {
    public AISwim(RidableEntity ridable) {
        super((EntityInsentient) ridable);
    }
}
