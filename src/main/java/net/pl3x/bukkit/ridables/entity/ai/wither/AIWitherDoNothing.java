package net.pl3x.bukkit.ridables.entity.ai.wither;

import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.RidableWither;

public class AIWitherDoNothing extends PathfinderGoal {
    private final RidableWither wither;

    public AIWitherDoNothing(RidableWither wither) {
        this.wither = wither;
        a(7); // setMutexBits
    }

    // shouldExecute
    public boolean a() {
        return wither.getRider() != null || wither.dz() > 0; // getInvulnerableTime
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }
}
