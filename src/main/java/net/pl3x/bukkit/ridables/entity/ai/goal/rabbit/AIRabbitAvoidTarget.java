package net.pl3x.bukkit.ridables.entity.ai.goal.rabbit;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.PathfinderGoalAvoidTarget;
import net.pl3x.bukkit.ridables.entity.animal.RidableRabbit;

public class AIRabbitAvoidTarget<T extends Entity> extends PathfinderGoalAvoidTarget<T> {
    private final RidableRabbit rabbit;

    public AIRabbitAvoidTarget(RidableRabbit rabbit, Class<T> oclass, float f, double d0, double d1) {
        super(rabbit, oclass, f, d0, d1);
        this.rabbit = rabbit;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return rabbit.getRider() == null && rabbit.getRabbitType() != 99 && super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return rabbit.getRider() == null && super.b();
    }
}
