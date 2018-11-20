package net.pl3x.bukkit.ridables.entity.ai.goal.ghast;

import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.monster.RidableGhast;
import net.pl3x.bukkit.ridables.util.Const;

public class AIGhastLookAround extends PathfinderGoal {
    private final RidableGhast ghast;

    public AIGhastLookAround(RidableGhast ghast) {
        this.ghast = ghast;
        a(2); // setMutexBits
    }

    // shouldExecute
    @Override
    public boolean a() {
        return ghast.getRider() == null;
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return a();
    }

    // tick
    @Override
    public void e() {
        EntityLiving target = ghast.getGoalTarget();
        if (target == null) {
            ghast.aQ = ghast.yaw = -((float) MathHelper.c(ghast.motX, ghast.motZ)) * Const.RAD2DEG_FLOAT;
        } else if (target.h(ghast) < 4096.0D) { // getDistanceSq
            ghast.aQ = ghast.yaw = -((float) MathHelper.c(target.locX - ghast.locX, target.locZ - ghast.locZ)) * Const.RAD2DEG_FLOAT;
        }
    }
}
