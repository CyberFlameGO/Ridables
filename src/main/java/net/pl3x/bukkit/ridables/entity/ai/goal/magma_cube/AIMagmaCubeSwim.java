package net.pl3x.bukkit.ridables.entity.ai.goal.magma_cube;

import com.destroystokyo.paper.event.entity.SlimeSwimEvent;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.monster.slime.RidableMagmaCube;
import net.pl3x.bukkit.ridables.entity.monster.slime.RidableSlime;
import org.bukkit.entity.Slime;

public class AIMagmaCubeSwim extends PathfinderGoal {
    private final RidableMagmaCube magmaCube;

    public AIMagmaCubeSwim(RidableMagmaCube magmaCube) {
        this.magmaCube = magmaCube;
        a(5); // setMutexBits
        this.magmaCube.getNavigation().d(true);
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (magmaCube.isInWater() || magmaCube.ax()) {
            return magmaCube.canWander() && new SlimeSwimEvent((Slime) magmaCube.getBukkitEntity()).callEvent();
        }
        return false;
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return a();
    }

    // tick
    @Override
    public void e() {
        if (magmaCube.getRandom().nextFloat() < 0.8F) {
            magmaCube.getControllerJump().a();
        }
        ((RidableSlime.SlimeWASDController) magmaCube.getControllerMove()).setSpeed(1.2D);
    }
}
