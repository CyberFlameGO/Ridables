package net.pl3x.bukkit.ridables.entity.ai.goal.zombie.drowned;

import net.minecraft.server.v1_13_R2.PathfinderGoalZombieAttack;
import net.pl3x.bukkit.ridables.entity.monster.zombie.RidableDrowned;

public class AIDrownedAttack extends PathfinderGoalZombieAttack {
    private final RidableDrowned drowned;

    public AIDrownedAttack(RidableDrowned drowned, double speed, boolean longMemory) {
        super(drowned, speed, longMemory);
        this.drowned = drowned;
    }

    // shouldExecute
    public boolean a() {
        return drowned.getRider() == null && super.a() && drowned.f(drowned.getGoalTarget()); // should attack
    }

    // shouldContinueExecuting
    public boolean b() {
        return drowned.getRider() == null && super.b() && drowned.f(drowned.getGoalTarget()); // should attack
    }
}
