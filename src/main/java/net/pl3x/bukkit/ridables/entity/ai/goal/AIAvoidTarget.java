package net.pl3x.bukkit.ridables.entity.ai.goal;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.IEntitySelector;
import net.minecraft.server.v1_13_R2.PathfinderGoalAvoidTarget;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

import java.util.function.Predicate;

public class AIAvoidTarget<T extends Entity> extends PathfinderGoalAvoidTarget<T> {
    private final RidableEntity ridable;

    public AIAvoidTarget(RidableEntity ridable, Class<T> targetClass, float distance, double farSpeed, double nearSpeed) {
        this(ridable, targetClass, (var0) -> true, distance, farSpeed, nearSpeed, IEntitySelector.e);
    }

    public AIAvoidTarget(RidableEntity ridable, Class<T> targetClass, float distance, double farSpeed, double nearSpeed, Predicate<Entity> var8) {
        this(ridable, targetClass, (var0) -> true, distance, farSpeed, nearSpeed, var8);
    }

    public AIAvoidTarget(RidableEntity ridable, Class<T> targetClass, Predicate<? super Entity> avoidSelector, float distance, double farSpeed, double nearSpeed, Predicate<Entity> entitySelector) {
        super((EntityCreature) ridable, targetClass, avoidSelector, distance, farSpeed, nearSpeed, entitySelector);
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
