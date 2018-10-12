package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.IRangedEntity;
import net.minecraft.server.v1_13_R2.PathfinderGoalArrowAttack;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIAttackRanged extends PathfinderGoalArrowAttack {
    private final RidableEntity ridable;

    public AIAttackRanged(RidableEntity ridable, double moveSpeed, int maxAttackTime, float maxAttackDistance) {
        super((IRangedEntity) ridable, moveSpeed, maxAttackTime, maxAttackDistance);
        this.ridable = ridable;
    }

    // shouldExecute
    public boolean a() {
        return ridable.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return ridable.getRider() == null && super.b();
    }
}
