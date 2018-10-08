package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.PathfinderGoalNearestAttackableTarget;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class AIAttackNearest<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget<T> {
    private final RidableEntity ridable;

    public AIAttackNearest(RidableEntity ridable, Class<T> targetClass, boolean checkSight) {
        this(ridable, targetClass, checkSight, false);
    }

    public AIAttackNearest(RidableEntity ridable, Class<T> targetClass, boolean checkSight, boolean onlyNearby) {
        this(ridable, targetClass, 10, checkSight, onlyNearby, null);
    }

    public AIAttackNearest(RidableEntity ridable, Class<T> targetClass, int chance, boolean checkSight, boolean onlyNearby, @Nullable Predicate<? super T> predicate) {
        super((EntityCreature) ridable, targetClass, chance, checkSight, onlyNearby, predicate);
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
