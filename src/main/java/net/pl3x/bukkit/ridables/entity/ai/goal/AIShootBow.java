package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.EntityMonster;
import net.minecraft.server.v1_13_R2.IRangedEntity;
import net.minecraft.server.v1_13_R2.PathfinderGoalBowShoot;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIShootBow<T extends EntityMonster & IRangedEntity> extends PathfinderGoalBowShoot<T> {
    private final RidableEntity ridable;
    private final T entity;

    public AIShootBow(RidableEntity ridable, double moveSpeedAmp, int attackCooldown, float maxAttackDistance) {
        super((T) ridable, moveSpeedAmp, attackCooldown, maxAttackDistance);
        this.ridable = ridable;
        this.entity = (T) ridable;
    }

    // shouldExecute
    public boolean a() {
        return ridable.getRider() == null && entity.getGoalTarget() != null && g(); // isBowInMainHand
    }

    // shouldContinueExecuting
    public boolean b() {
        return ridable.getRider() == null && (entity.getGoalTarget() != null || !entity.getNavigation().p()) && g(); // noPath isBowInMainHand
    }
}
