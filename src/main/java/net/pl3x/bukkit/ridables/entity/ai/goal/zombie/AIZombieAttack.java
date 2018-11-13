package net.pl3x.bukkit.ridables.entity.ai.goal.zombie;

import net.minecraft.server.v1_13_R2.EntityZombie;
import net.minecraft.server.v1_13_R2.PathfinderGoalZombieAttack;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIZombieAttack extends PathfinderGoalZombieAttack {
    private RidableEntity ridable;

    public AIZombieAttack(RidableEntity ridable, double d0, boolean flag) {
        super((EntityZombie) ridable, d0, flag);
        this.ridable = ridable;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return ridable.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return ridable.getRider() == null && super.b();
    }
}
