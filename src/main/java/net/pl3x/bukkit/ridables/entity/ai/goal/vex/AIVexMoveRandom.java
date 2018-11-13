package net.pl3x.bukkit.ridables.entity.ai.goal.vex;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.monster.RidableVex;

public class AIVexMoveRandom extends PathfinderGoal {
    private final RidableVex vex;

    public AIVexMoveRandom(RidableVex vex) {
        this.vex = vex;
        a(1); // setMutexBits
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (vex.getRider() != null) {
            return false;
        }
        if (vex.getControllerMove().b()) {
            return false;
        }
        return vex.getRandom().nextInt(7) == 0;
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return false;
    }

    // tick
    @Override
    public void e() {
        BlockPosition origin = vex.dz(); // getBoundOrigin
        if (origin == null) {
            origin = new BlockPosition(vex);
        }
        for (int i = 0; i < 3; ++i) {
            BlockPosition pos = origin.a(vex.getRandom().nextInt(15) - 7, vex.getRandom().nextInt(11) - 5, vex.getRandom().nextInt(15) - 7); // add
            if (vex.world.isEmpty(pos)) {
                vex.getControllerMove().a(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0.25D); // setMoveTo
                if (vex.getGoalTarget() == null) {
                    vex.getControllerLook().a(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 180.0F, 20.0F); // setLookPosition
                }
                break;
            }
        }
    }
}
