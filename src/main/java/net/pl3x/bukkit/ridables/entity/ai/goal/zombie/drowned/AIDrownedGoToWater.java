package net.pl3x.bukkit.ridables.entity.ai.goal.zombie.drowned;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

import javax.annotation.Nullable;
import java.util.Random;

public class AIDrownedGoToWater extends PathfinderGoal {
    private final RidableEntity ridable;
    private final EntityCreature entity;
    private double x;
    private double y;
    private double z;
    private final double speed;
    private final World world;

    public AIDrownedGoToWater(RidableEntity ridable, double speed) {
        this.ridable = ridable;
        this.entity = (EntityCreature) ridable;
        this.speed = speed;
        this.world = entity.world;
        a(1); // setMutexBits
    }

    // shouldExecute
    public boolean a() {
        if (ridable.getRider() != null) {
            return false;
        }
        if (!world.L()) { // isDayTime
            return false;
        }
        if (entity.isInWater()) {
            return false;
        }
        BlockPosition pos = findWater();
        if (pos == null) {
            return false;
        } else {
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
            return true;
        }
    }

    // shouldContinueExecuting
    public boolean b() {
        return ridable.getRider() == null && !entity.getNavigation().p();
    }

    // startExecuting
    public void c() {
        entity.getNavigation().a(x, y, z, speed);
    }

    @Nullable
    private BlockPosition findWater() {
        Random rand = entity.getRandom();
        BlockPosition pos = new BlockPosition(entity.locX, entity.getBoundingBox().minY, entity.locZ);
        for (int i = 0; i < 10; ++i) {
            BlockPosition randPos = pos.a(rand.nextInt(20) - 10, 2 - rand.nextInt(8), rand.nextInt(20) - 10);
            if (world.getType(randPos).getBlock() == Blocks.WATER) {
                return randPos;
            }
        }
        return null;
    }
}
