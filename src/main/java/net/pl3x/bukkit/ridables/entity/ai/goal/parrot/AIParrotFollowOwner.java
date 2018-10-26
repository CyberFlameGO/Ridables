package net.pl3x.bukkit.ridables.entity.ai.goal.parrot;

import net.minecraft.server.v1_13_R2.PathfinderGoalFollowOwnerParrot;
import net.pl3x.bukkit.ridables.entity.animal.RidableParrot;

public class AIParrotFollowOwner extends PathfinderGoalFollowOwnerParrot {
    private final RidableParrot parrot;

    public AIParrotFollowOwner(RidableParrot parrot, double speed, float minDistance, float maxDistance) {
        super(parrot, speed, minDistance, maxDistance);
        this.parrot = parrot;
    }

    // shouldExecute
    public boolean a() {
        return parrot.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return parrot.getRider() == null && super.b();
    }
}
