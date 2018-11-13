package net.pl3x.bukkit.ridables.entity.ai.goal.fish;

import net.minecraft.server.v1_13_R2.EntityFish;
import net.minecraft.server.v1_13_R2.PathfinderGoalRandomSwim;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.animal.fish.RidableFishSchool;

public class AIFishSwim extends PathfinderGoalRandomSwim {
    private final RidableEntity ridable;
    private final boolean isSchoolFish;

    public AIFishSwim(RidableEntity ridable) {
        super((EntityFish) ridable, 1.0D, 40);
        this.ridable = ridable;
        this.isSchoolFish = ridable instanceof RidableFishSchool;
        System.out.println(isSchoolFish);
    }

    // shouldExecute
    @Override
    public boolean a() {
        return ridable.getRider() == null && (!isSchoolFish || !((RidableFishSchool) ridable).isFollowing()) && super.a();
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return ridable.getRider() == null && super.b();
    }
}
