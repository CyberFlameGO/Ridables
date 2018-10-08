package net.pl3x.bukkit.ridables.entity.ai.phantom;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.HeightMap;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.PathfinderGoalTarget;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.pl3x.bukkit.ridables.entity.RidablePhantom;

public class AIPhantomPickAttack extends PathfinderGoal {
    private final RidablePhantom phantom;
    private int ticks;

    public AIPhantomPickAttack(RidablePhantom phantom) {
        this.phantom = phantom;
    }

    // shouldExecute
    public boolean a() {
        return phantom.getRider() == null && PathfinderGoalTarget.a(phantom, phantom.getGoalTarget(), false, false);
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // startExecuting
    public void c() {
        ticks = 10;
        phantom.phase = RidablePhantom.AttackPhase.CIRCLE;
        update();
    }

    // resetTask
    public void d() {
        phantom.orbitPosition = phantom.world.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, phantom.orbitPosition).up(10 + phantom.getRandom().nextInt(20));
    }

    // tick
    public void e() {
        if (phantom.phase == RidablePhantom.AttackPhase.CIRCLE) {
            --ticks;
            if (ticks <= 0 && phantom.canAttack()) {
                phantom.phase = RidablePhantom.AttackPhase.SWOOP;
                update();
                ticks = (8 + phantom.getRandom().nextInt(4)) * 20;
                phantom.a(SoundEffects.ENTITY_PHANTOM_SWOOP, 10.0F, 0.95F + phantom.getRandom().nextFloat() * 0.1F);
            }
        }
    }

    private void update() {
        phantom.orbitPosition = (new BlockPosition(phantom.getGoalTarget())).up(20 + phantom.getRandom().nextInt(20));
        if (phantom.orbitPosition.getY() < phantom.world.getSeaLevel()) {
            phantom.orbitPosition = new BlockPosition(phantom.orbitPosition.getX(), phantom.world.getSeaLevel() + 1, phantom.orbitPosition.getZ());
        }
    }
}
