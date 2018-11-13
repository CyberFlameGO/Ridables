package net.pl3x.bukkit.ridables.entity.ai.goal.zombie.zombie_pigman;

import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.PathfinderGoalNearestAttackableTarget;
import net.pl3x.bukkit.ridables.entity.monster.zombie.RidableZombiePigman;

public class AIPigmanTargetAggressor extends PathfinderGoalNearestAttackableTarget<EntityHuman> {
    private final RidableZombiePigman pigman;

    public AIPigmanTargetAggressor(RidableZombiePigman pigman) {
        super(pigman, EntityHuman.class, true);
        this.pigman = pigman;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return pigman.getRider() == null && pigman.dF() && super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return pigman.getRider() == null && super.b();
    }
}
