package net.pl3x.bukkit.ridables.entity.ai.wolf;

import net.minecraft.server.v1_13_R2.PathfinderGoalBeg;
import net.pl3x.bukkit.ridables.entity.RidableWolf;

public class AIWolfBeg extends PathfinderGoalBeg {
    private final RidableWolf wolf;

    public AIWolfBeg(RidableWolf wolf, float maxDistance) {
        super(wolf, maxDistance);
        this.wolf = wolf;
    }

    // shouldExecute
    public boolean a() {
        return wolf.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return wolf.getRider() == null && super.b();
    }
}
