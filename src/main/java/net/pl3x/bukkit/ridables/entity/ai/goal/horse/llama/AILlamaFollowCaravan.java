package net.pl3x.bukkit.ridables.entity.ai.goal.horse.llama;

import net.minecraft.server.v1_13_R2.PathfinderGoalLlamaFollow;
import net.pl3x.bukkit.ridables.entity.animal.horse.RidableLlama;

public class AILlamaFollowCaravan extends PathfinderGoalLlamaFollow {
    private final RidableLlama llama;

    public AILlamaFollowCaravan(RidableLlama llama, double speed) {
        super(llama, speed);
        this.llama = llama;
    }

    // shouldExecute
    public boolean a() {
        return llama.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return llama.getRider() == null && super.b();
    }
}
