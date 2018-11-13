package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.EntityAnimal;
import net.minecraft.server.v1_13_R2.PathfinderGoalBreed;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIBreed extends PathfinderGoalBreed {
    private final RidableEntity ridable;

    public AIBreed(RidableEntity ridable, double speed, Class<? extends EntityAnimal> targetClass) {
        super((EntityAnimal) ridable, speed, targetClass);
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
