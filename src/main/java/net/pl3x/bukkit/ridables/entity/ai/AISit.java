package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityTameableAnimal;
import net.minecraft.server.v1_13_R2.PathfinderGoalSit;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AISit extends PathfinderGoalSit {
    private final RidableEntity ridable;

    public AISit(RidableEntity ridable) {
        super((EntityTameableAnimal) ridable);
        this.ridable = ridable;
    }

    // shouldExecute
    public boolean a() {
        return ridable.getRider() == null && super.a();
    }
}
