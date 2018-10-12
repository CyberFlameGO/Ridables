package net.pl3x.bukkit.ridables.entity.ai.zombie;

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
    public boolean a() {
        return ridable.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return ridable.getRider() == null && super.b();
    }
}
