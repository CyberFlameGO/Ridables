package net.pl3x.bukkit.ridables.entity.ai.ghast;

import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.RidableGhast;

public class AIGhastLookAround extends PathfinderGoal {
    private final RidableGhast ghast;

    public AIGhastLookAround(RidableGhast ghast) {
        this.ghast = ghast;
        a(2); // setMutexBits
    }

    // shouldExecute
    public boolean a() {
        return ghast.getRider() == null;
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // tick
    public void e() {
        EntityLiving target = ghast.getGoalTarget();
        if (target == null) {
            ghast.aQ = ghast.yaw = -((float) MathHelper.c(ghast.motX, ghast.motZ)) * (180F / (float) Math.PI);
        } else if (target.h(ghast) < 4096.0D) { // getDistanceSq
            ghast.aQ = ghast.yaw = -((float) MathHelper.c(target.locX - ghast.locX, target.locZ - ghast.locZ)) * (180F / (float) Math.PI);
        }
    }
}
