package net.pl3x.bukkit.ridables.entity.ai.goal.squid;

import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.animal.RidableSquid;

public class AISquidMoveRandom extends PathfinderGoal {
    private final RidableSquid squid;

    public AISquidMoveRandom(RidableSquid squid) {
        this.squid = squid;
    }

    // shouldExecute
    @Override
    public boolean a() {
        return squid.getRider() == null;
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return a();
    }

    // tick
    @Override
    public void e() {
        if (squid.cj() > 100) { // getIdleTime
            squid.c(0.0F, 0.0F, 0.0F); // setMovementVector
        } else if (squid.getRandom().nextInt(50) == 0 || !squid.inWater || !squid.l()) { // hasMovementVector
            float randYaw = squid.getRandom().nextFloat() * ((float) Math.PI * 2F);
            squid.c(MathHelper.cos(randYaw) * 0.2F, -0.1F + squid.getRandom().nextFloat() * 0.2F, MathHelper.sin(randYaw) * 0.2F); // setMovementVector
        }
    }
}
