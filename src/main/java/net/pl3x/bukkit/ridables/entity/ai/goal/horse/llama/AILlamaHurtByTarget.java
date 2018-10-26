package net.pl3x.bukkit.ridables.entity.ai.goal.horse.llama;

import net.minecraft.server.v1_13_R2.PathfinderGoalHurtByTarget;
import net.pl3x.bukkit.ridables.entity.animal.horse.RidableLlama;

public class AILlamaHurtByTarget extends PathfinderGoalHurtByTarget {
    private final RidableLlama llama;

    public AILlamaHurtByTarget(RidableLlama llama) {
        super(llama, false);
        this.llama = llama;
    }

    // shouldExecute
    public boolean a() {
        return llama.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        if (llama.getRider() != null) {
            return false;
        }
        if (llama.didSpit()) {
            llama.setDidSpit(false);
            return false;
        }
        return super.b();
    }
}
