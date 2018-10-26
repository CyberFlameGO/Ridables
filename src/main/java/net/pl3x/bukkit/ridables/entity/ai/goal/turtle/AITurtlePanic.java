package net.pl3x.bukkit.ridables.entity.ai.goal.turtle;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.PathfinderGoalPanic;
import net.pl3x.bukkit.ridables.entity.animal.RidableTurtle;

public class AITurtlePanic extends PathfinderGoalPanic {
    private final RidableTurtle turtle;

    public AITurtlePanic(RidableTurtle turtle, double speed) {
        super(turtle, speed);
        this.turtle = turtle;
    }

    // shouldExecute
    public boolean a() {
        if (turtle.getRider() != null) {
            return false;
        }
        if (turtle.getLastDamager() == null && !turtle.isBurning()) {
            return false;
        }
        BlockPosition pos = a(turtle.world, turtle, 7, 4); // getRandPos
        if (pos != null) {
            c = (double) pos.getX(); // randPosX
            d = (double) pos.getY(); // randPosY
            e = (double) pos.getZ(); // randPosZ
            return true;
        }
        return g(); // findRandomPosition
    }

    // shouldContinueExecuting
    public boolean b() {
        return turtle.getRider() == null && super.b();
    }
}
