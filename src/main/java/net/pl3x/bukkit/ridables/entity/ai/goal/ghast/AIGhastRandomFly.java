package net.pl3x.bukkit.ridables.entity.ai.goal.ghast;

import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.monster.RidableGhast;

public class AIGhastRandomFly extends PathfinderGoal {
    private final RidableGhast ghast;

    public AIGhastRandomFly(RidableGhast ghast) {
        this.ghast = ghast;
        a(1); // setMutexBits
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (ghast.getRider() != null) {
            return false;
        }
        ControllerMove controllermove = ghast.getControllerMove();
        if (!controllermove.b()) { // isUpdating
            return true;
        }
        double x = controllermove.d() - ghast.locX;
        double y = controllermove.e() - ghast.locY;
        double z = controllermove.f() - ghast.locZ;
        double distance = x * x + y * y + z * z;
        return distance < 1.0D || distance > 3600.0D;
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return false;
    }

    // startExecuting
    @Override
    public void c() {
        ghast.getControllerMove().a(
                ghast.locX + (double) ((ghast.getRandom().nextFloat() * 2.0F - 1.0F) * 16.0F),
                ghast.locY + (double) ((ghast.getRandom().nextFloat() * 2.0F - 1.0F) * 16.0F),
                ghast.locZ + (double) ((ghast.getRandom().nextFloat() * 2.0F - 1.0F) * 16.0F),
                1.0D);
    }
}
