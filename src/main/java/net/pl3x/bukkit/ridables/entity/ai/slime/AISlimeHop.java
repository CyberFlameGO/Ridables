package net.pl3x.bukkit.ridables.entity.ai.slime;

import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.entity.RidableSlime;
import net.pl3x.bukkit.ridables.hook.Paper;

public class AISlimeHop extends PathfinderGoal {
    private final RidableSlime slime;

    public AISlimeHop(RidableSlime slime) {
        this.slime = slime;
        a(5); // setMutexBits
    }

    // shouldExecute
    public boolean a() {
        if (slime.getRider() != null) {
            return false;
        }
        if (Ridables.isPaper()) {
            return slime.canWander() && Paper.CallSlimeWanderEvent(slime);
        }
        return true;
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // tick
    public void e() {
        ((RidableSlime.SlimeWASDController) slime.getControllerMove()).setSpeed(1.0D);
    }
}
