package net.pl3x.bukkit.ridables.entity.ai.goal.ocelot;

import net.minecraft.server.v1_13_R2.PathfinderGoalJumpOnBlock;
import net.pl3x.bukkit.ridables.entity.animal.RidableOcelot;

public class AIOcelotSitOnBlock extends PathfinderGoalJumpOnBlock {
    private final RidableOcelot ocelot;

    public AIOcelotSitOnBlock(RidableOcelot ocelot, double speed) {
        super(ocelot, speed);
        this.ocelot = ocelot;
    }

    // shouldExecute
    public boolean a() {
        return ocelot.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return ocelot.getRider() == null && super.b();
    }
}
