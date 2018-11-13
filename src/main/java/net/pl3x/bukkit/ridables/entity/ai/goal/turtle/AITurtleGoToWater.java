package net.pl3x.bukkit.ridables.entity.ai.goal.turtle;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.IWorldReader;
import net.minecraft.server.v1_13_R2.PathfinderGoalGotoTarget;
import net.pl3x.bukkit.ridables.entity.animal.RidableTurtle;

public class AITurtleGoToWater extends PathfinderGoalGotoTarget {
    private final RidableTurtle turtle;

    public AITurtleGoToWater(RidableTurtle turtle, double speed) {
        super(turtle, turtle.isBaby() ? 2.0D : speed, 24);
        this.turtle = turtle;
        this.e = -1;
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (turtle.getRider() != null) {
            return false;
        }
        if (turtle.isBaby() && !turtle.isInWater()) {
            return super.a();
        }
        if (turtle.isGoingHome()) {
            return false;
        }
        if (turtle.isInWater()) {
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
        if (turtle.getRider() != null) {
            return false;
        }
        if (turtle.isInWater()) {
            return false;
        }
        if (c > 1200) { // timeoutCounter
            return false;
        }
        return a(turtle.world, d); // shouldMoveTo destinationBlock
    }

    // getTargetYOffset
    @Override
    public int j() {
        return 1;
    }

    // shouldMove
    @Override
    public boolean i() {
        return c % 160 == 0; // timeoutCounter
    }

    // shouldMoveTo
    @Override
    protected boolean a(IWorldReader world, BlockPosition pos) {
        return world.getType(pos).getBlock() == Blocks.WATER;
    }
}
