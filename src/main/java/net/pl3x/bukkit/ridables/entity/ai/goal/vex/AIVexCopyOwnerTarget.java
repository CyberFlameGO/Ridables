package net.pl3x.bukkit.ridables.entity.ai.goal.vex;

import net.minecraft.server.v1_13_R2.PathfinderGoalTarget;
import net.pl3x.bukkit.ridables.entity.monster.RidableVex;
import org.bukkit.event.entity.EntityTargetEvent;

public class AIVexCopyOwnerTarget extends PathfinderGoalTarget {
    private final RidableVex vex;

    public AIVexCopyOwnerTarget(RidableVex vex) {
        super(vex, false);
        this.vex = vex;
    }

    // shouldExecute
    public boolean a() {
        if (vex.getRider() != null) {
            return false;
        }
        if (vex.getOwner() == null) {
            return false;
        }
        if (vex.getOwner().getGoalTarget() == null) {
            return false;
        }
        return a(vex.getOwner().getGoalTarget(), false); // isSuitableTarget
    }

    // shouldContinueExecuting
    public boolean b() {
        return vex.getRider() == null && super.b();
    }

    // startExecuting
    public void c() {
        vex.setGoalTarget(vex.getOwner().getGoalTarget(), EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET, true);
        super.c();
    }
}
