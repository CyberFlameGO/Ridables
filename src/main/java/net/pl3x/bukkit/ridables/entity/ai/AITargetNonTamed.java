package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityTameableAnimal;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomTargetNonTamed;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

import java.util.function.Predicate;

public class AITargetNonTamed<T extends EntityLiving> extends PathfinderGoalRandomTargetNonTamed<T> {
    private final RidableEntity ridable;

    public AITargetNonTamed(RidableEntity ridable, Class<T> targetClass, boolean checkSight, Predicate<? super T> predicate) {
        super((EntityTameableAnimal) ridable, targetClass, checkSight, predicate);
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
