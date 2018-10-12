package net.pl3x.bukkit.ridables.entity.ai.villager;

import net.minecraft.server.v1_13_R2.PathfinderGoalTakeFlower;
import net.pl3x.bukkit.ridables.entity.RidableVillager;

public class AIVillagerTakeFlower extends PathfinderGoalTakeFlower {
    private final RidableVillager villager;

    public AIVillagerTakeFlower(RidableVillager villager) {
        super(villager);
        this.villager = villager;
    }

    // shouldExecute
    public boolean a() {
        return villager.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return villager.getRider() == null && super.b();
    }
}
