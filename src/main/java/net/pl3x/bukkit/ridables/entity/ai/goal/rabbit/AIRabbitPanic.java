package net.pl3x.bukkit.ridables.entity.ai.goal.rabbit;

import net.minecraft.server.v1_13_R2.PathfinderGoalPanic;
import net.pl3x.bukkit.ridables.entity.animal.RidableRabbit;

public class AIRabbitPanic extends PathfinderGoalPanic {
    private final RidableRabbit rabbit;

    public AIRabbitPanic(RidableRabbit rabbit, double speed) {
        super(rabbit, speed);
        this.rabbit = rabbit;
    }

    // shouldExecute
    public boolean a() {
        return rabbit.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return rabbit.getRider() == null && super.b();
    }

    // tick
    public void e() {
        super.e();
        rabbit.c(b); // setMovementSpeed speed
    }
}
