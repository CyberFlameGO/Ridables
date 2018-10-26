package net.pl3x.bukkit.ridables.entity.ai.fish;

import net.minecraft.server.v1_13_R2.EntityFishSchool;
import net.minecraft.server.v1_13_R2.PathfinderGoalFishSchool;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableFishSchool;

public class AIFishFollowLeader extends PathfinderGoalFishSchool {
    private final RidableEntity ridable;

    public AIFishFollowLeader(RidableFishSchool ridable) {
        super((EntityFishSchool) ridable);
        this.ridable = (RidableEntity) ridable;
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
