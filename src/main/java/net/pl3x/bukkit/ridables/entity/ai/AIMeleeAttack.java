package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoalMeleeAttack;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIMeleeAttack extends PathfinderGoalMeleeAttack {
    private final RidableEntity ridable;

    public AIMeleeAttack(RidableEntity ridable, double speed, boolean longMemory) {
        super((EntityCreature) ridable, speed, longMemory);
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
