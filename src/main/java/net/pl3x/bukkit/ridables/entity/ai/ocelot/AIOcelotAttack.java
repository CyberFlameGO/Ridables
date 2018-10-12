package net.pl3x.bukkit.ridables.entity.ai.ocelot;

import net.minecraft.server.v1_13_R2.PathfinderGoalOcelotAttack;
import net.pl3x.bukkit.ridables.entity.RidableOcelot;

public class AIOcelotAttack extends PathfinderGoalOcelotAttack {
    private final RidableOcelot ocelot;

    public AIOcelotAttack(RidableOcelot ocelot) {
        super(ocelot);
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
