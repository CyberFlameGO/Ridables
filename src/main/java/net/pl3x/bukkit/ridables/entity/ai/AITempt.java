package net.pl3x.bukkit.ridables.entity.ai;

import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoalTempt;
import net.minecraft.server.v1_13_R2.RecipeItemStack;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AITempt extends PathfinderGoalTempt {
    private final RidableEntity ridable;

    public AITempt(RidableEntity ridable, double speed, boolean scaredByPlayerMovement, RecipeItemStack temptItem) {
        super((EntityCreature) ridable, speed, scaredByPlayerMovement, temptItem);
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
