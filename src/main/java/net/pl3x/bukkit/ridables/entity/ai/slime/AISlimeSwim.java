package net.pl3x.bukkit.ridables.entity.ai.slime;

import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.entity.RidableSlime;
import net.pl3x.bukkit.ridables.util.PaperOnly;

public class AISlimeSwim extends PathfinderGoal {
    private final RidableSlime slime;

    public AISlimeSwim(RidableSlime slime) {
        this.slime = slime;
        a(5); // setMutexBits
        slime.getNavigation().d(true);
    }

    // shouldExecute
    public boolean a() {
        if (slime.isInWater() || slime.ax()) {
            if (Ridables.isPaper()) {
                return slime.canWander() && PaperOnly.CallSlimeSwimEvent(slime);
            }
            return true;
        }
        return false;
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // tick
    public void e() {
        if (slime.getRandom().nextFloat() < 0.8F) {
            slime.getControllerJump().a();
        }
        ((RidableSlime.SlimeWASDController) slime.getControllerMove()).setSpeed(1.2D);
    }
}
