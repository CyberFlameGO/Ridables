package net.pl3x.bukkit.ridables.entity.ai.zombie.zombie_pigman;

import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.PathfinderGoalNearestAttackableTarget;
import net.pl3x.bukkit.ridables.entity.RidableZombiePigman;

public class AIPigmanTargetAggressor extends PathfinderGoalNearestAttackableTarget<EntityHuman> {
    private final RidableZombiePigman pigman;

    public AIPigmanTargetAggressor(RidableZombiePigman pigman) {
        super(pigman, EntityHuman.class, true);
        this.pigman = pigman;
    }

    // shouldExecute
    public boolean a() {
        return pigman.getRider() == null && pigman.dF() && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return pigman.getRider() == null && super.b();
    }
}
