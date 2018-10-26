package net.pl3x.bukkit.ridables.entity.ai.goal.turtle;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.RandomPositionGenerator;
import net.minecraft.server.v1_13_R2.StructureBoundingBox;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.pl3x.bukkit.ridables.entity.animal.RidableTurtle;

public class AITurtleTravel extends PathfinderGoal {
    private final RidableTurtle turtle;
    private final double speed;
    private boolean stop;

    public AITurtleTravel(RidableTurtle turtle, double d0) {
        this.turtle = turtle;
        this.speed = d0;
    }

    // shouldExecute
    public boolean a() {
        if (turtle.getRider() != null) {
            return false;
        }
        if (turtle.isGoingHome()) {
            return false;
        }
        if (turtle.hasEgg()) {
            return false;
        }
        return turtle.isInWater();
    }

    // shouldContinueExecuting
    public boolean b() {
        if (turtle.getRider() != null) {
            return false;
        }
        if (stop) {
            return false;
        }
        if (turtle.isGoingHome()) {
            return false;
        }
        if (turtle.isInLove()) {
            return false;
        }
        if (turtle.hasEgg()) {
            return false;
        }
        return turtle.c(turtle.getTravelPos()) >= 36.0D;
    }

    // startExecuting
    public void c() {
        int y = turtle.getRandom().nextInt(9) - 4;
        if (y + turtle.locY > turtle.world.getSeaLevel() - 1) {
            y = 0;
        }
        turtle.setTravelPos(new BlockPosition(turtle.getRandom().nextInt(1025) - 512 + turtle.locX, y + turtle.locY, turtle.getRandom().nextInt(1025) - 512 + turtle.locZ));
        turtle.setTravelling(true);
        stop = false;
    }

    // resetTask
    public void d() {
        turtle.setTravelling(false);
        super.d();
    }

    // tick
    public void e() {
        if (/*turtle.dA() ||*/ turtle.getNavigation().p()) { // Paper - Fix GH-1501 // noPath
            BlockPosition pos = turtle.getTravelPos();
            Vec3D vec3d = RandomPositionGenerator.a(turtle, 16, 3, new Vec3D(pos.getX(), pos.getY(), pos.getZ()), Math.PI / 10D);
            if (vec3d == null) {
                vec3d = RandomPositionGenerator.a(turtle, 8, 7, new Vec3D(pos.getX(), pos.getY(), pos.getZ())); // findRandomTargetBlockTowards
            }
            if (vec3d != null) {
                int x = MathHelper.floor(vec3d.x);
                int z = MathHelper.floor(vec3d.z);
                if (!turtle.world.a(new StructureBoundingBox(x - 34, 0, z - 34, x + 34, 0, z + 34))) { // isAreaLoaded
                    vec3d = null;
                }
            }
            if (vec3d == null) {
                stop = true;
                return;
            }
            turtle.getNavigation().a(vec3d.x, vec3d.y, vec3d.z, speed);
        }
    }
}
