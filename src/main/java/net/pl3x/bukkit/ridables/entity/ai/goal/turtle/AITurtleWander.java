package net.pl3x.bukkit.ridables.entity.ai.goal.turtle;

import net.minecraft.server.v1_13_R2.PathfinderGoalRandomStroll;
import net.pl3x.bukkit.ridables.entity.animal.RidableTurtle;

public class AITurtleWander extends PathfinderGoalRandomStroll {
    private final RidableTurtle turtle;

    public AITurtleWander(RidableTurtle turtle, double speed, int chance) {
        super(turtle, speed, chance);
        this.turtle = turtle;
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (turtle.getRider() != null) {
            return false;
        }
        if (turtle.isInWater()) {
            return false;
        }
        if (turtle.isGoingHome()) {
            return false;
        }
        if (turtle.hasEgg()) {
            return false;
        }
        return super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return turtle.getRider() == null && super.b();
    }
}
