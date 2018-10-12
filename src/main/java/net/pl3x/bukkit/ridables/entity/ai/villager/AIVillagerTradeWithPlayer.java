package net.pl3x.bukkit.ridables.entity.ai.villager;

import net.minecraft.server.v1_13_R2.PathfinderGoalTradeWithPlayer;
import net.pl3x.bukkit.ridables.entity.RidableVillager;

public class AIVillagerTradeWithPlayer extends PathfinderGoalTradeWithPlayer {
    private final RidableVillager villager;

    public AIVillagerTradeWithPlayer(RidableVillager villager) {
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
