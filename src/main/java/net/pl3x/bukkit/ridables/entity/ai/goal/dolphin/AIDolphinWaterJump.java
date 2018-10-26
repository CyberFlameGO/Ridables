package net.pl3x.bukkit.ridables.entity.ai.goal.dolphin;

import net.minecraft.server.v1_13_R2.EntityDolphin;
import net.minecraft.server.v1_13_R2.PathfinderGoalWaterJump;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIDolphinWaterJump extends PathfinderGoalWaterJump {
    private final RidableEntity ridable;

    public AIDolphinWaterJump(RidableEntity ridable, int speed) {
        super((EntityDolphin) ridable, speed);
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
