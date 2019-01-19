package net.pl3x.bukkit.ridables.entity.ai.goal.squid;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.Particles;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.TagsFluid;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.pl3x.bukkit.ridables.entity.animal.RidableSquid;

public class AISquidFlee extends PathfinderGoal {
    private final RidableSquid squid;
    private int fleeTime;

    public AISquidFlee(RidableSquid squid) {
        this.squid = squid;
    }

    // shouldExecute
    @Override
    public boolean a() {
        if (squid.getRider() != null) {
            return false;
        }
        if (!squid.isInWater()) {
            return false;
        }
        EntityLiving target = squid.getLastDamager();
        if (target == null) {
            return false;
        }
        return squid.h(target) < 100; // getDistanceSq
    }

    // shouldContinueExecuting
    @Override
    public boolean b() {
        return a();
    }

    // startExecuting
    @Override
    public void c() {
        fleeTime = 0;
    }

    // tick
    @Override
    public void e() {
        ++fleeTime;
        EntityLiving target = squid.getLastDamager();
        if (target == null) {
            return;
        }
        Vec3D dir = new Vec3D(squid.locX - target.locX, squid.locY - target.locY, squid.locZ - target.locZ);
        BlockPosition pos = new BlockPosition(squid.locX + dir.x, squid.locY + dir.y, squid.locZ + dir.z);
        IBlockData state = squid.world.getType(pos);
        if (squid.world.getFluid(pos).a(TagsFluid.WATER) || state.isAir()) { // getFluidState isTagged
            double length = dir.b(); // length
            if (length > 0) {
                dir.a(); // normalize
                double scale = 3;
                if (length > 5) {
                    scale = scale - (length - 5) / 5;
                }
                if (scale > 0) {
                    dir = dir.a(scale); // scale
                }
            }
            if (state.isAir()) {
                dir = dir.a(0, dir.y, 0); // subtract
            }
            squid.c(dir.x / 20, dir.y / 20, dir.z / 20); // setMovementVector
        }
        if (fleeTime % 10 == 5) {
            squid.world.addParticle(Particles.e, squid.locX, squid.locY, squid.locZ, 0, 0, 0);
        }
    }
}
