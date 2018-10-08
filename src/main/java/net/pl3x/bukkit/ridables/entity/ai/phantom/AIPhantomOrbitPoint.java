package net.pl3x.bukkit.ridables.entity.ai.phantom;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.pl3x.bukkit.ridables.entity.RidablePhantom;

public class AIPhantomOrbitPoint extends AIPhantomMove {
    private float c;
    private float d;
    private float e;
    private float f;

    public AIPhantomOrbitPoint(RidablePhantom phantom) {
        super(phantom);
    }

    // shouldExecute
    public boolean a() {
        return phantom.getGoalTarget() == null || phantom.phase == RidablePhantom.AttackPhase.CIRCLE;
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // startExecuting
    public void c() {
        d = 5.0F + phantom.getRandom().nextFloat() * 10.0F;
        e = -4.0F + phantom.getRandom().nextFloat() * 9.0F;
        f = phantom.getRandom().nextBoolean() ? 1.0F : -1.0F;
        update();
    }

    // tick
    public void e() {
        if (phantom.getRandom().nextInt(350) == 0) {
            e = -4.0F + phantom.getRandom().nextFloat() * 9.0F;
        }

        if (phantom.getRandom().nextInt(250) == 0) {
            ++d;
            if (d > 15.0F) {
                d = 5.0F;
                f = -f;
            }
        }

        if (phantom.getRandom().nextInt(450) == 0) {
            c = phantom.getRandom().nextFloat() * 2.0F * 3.1415927F;
            update();
        }

        if (g()) {
            update();
        }

        if (phantom.orbitOffset.y < phantom.locY && !phantom.world.isEmpty((new BlockPosition(phantom)).down(1))) {
            e = Math.max(1.0F, e);
            update();
        }

        if (phantom.orbitOffset.y > phantom.locY && !phantom.world.isEmpty((new BlockPosition(phantom)).up(1))) {
            e = Math.min(-1.0F, e);
            update();
        }

    }

    private void update() {
        if (BlockPosition.ZERO.equals(phantom.orbitPosition)) {
            phantom.orbitPosition = new BlockPosition(phantom);
        }

        c += f * 15.0F * 0.017453292F;
        phantom.orbitOffset = (new Vec3D(phantom.orbitPosition)).add((double) (d * MathHelper.cos(c)), (double) (-4.0F + e), (double) (d * MathHelper.sin(c)));
    }
}
