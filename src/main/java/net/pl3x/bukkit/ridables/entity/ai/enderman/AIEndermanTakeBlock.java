package net.pl3x.bukkit.ridables.entity.ai.enderman;

import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.FluidCollisionOption;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.MovingObjectPosition;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.minecraft.server.v1_13_R2.TagsBlock;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.pl3x.bukkit.ridables.entity.RidableEnderman;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;

public class AIEndermanTakeBlock extends PathfinderGoal {
    private final RidableEnderman enderman;

    public AIEndermanTakeBlock(RidableEnderman enderman) {
        this.enderman = enderman;
    }

    // shouldExecute
    public boolean a() {
        if (enderman.getRider() != null) {
            return false;
        }
        if (enderman.getCarried() != null) {
            return false;
        }
        if (!enderman.world.getGameRules().getBoolean("mobGriefing")) {
            return false;
        }
        return enderman.getRandom().nextInt(20) == 0;
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // tick
    public void e() {
        int x = MathHelper.floor(enderman.locX - 2.0D + enderman.getRandom().nextDouble() * 4.0D);
        int y = MathHelper.floor(enderman.locY + enderman.getRandom().nextDouble() * 3.0D);
        int z = MathHelper.floor(enderman.locZ - 2.0D + enderman.getRandom().nextDouble() * 4.0D);
        BlockPosition pos = new BlockPosition(x, y, z);
        IBlockData state = enderman.world.getType(pos);
        if (!state.getBlock().a(TagsBlock.ENDERMAN_HOLDABLE)) {
            return; // not a holdable block
        }
        MovingObjectPosition rayTrace = enderman.world.rayTrace(
                new Vec3D((MathHelper.floor(enderman.locX) + 0.5D), (y + 0.5D), (MathHelper.floor(enderman.locZ) + 0.5D)),
                new Vec3D((x + 0.5D), (y + 0.5D), (z + 0.5D)),
                FluidCollisionOption.NEVER, true, false);
        if (rayTrace == null) {
            return; // no target block in range (shouldn't happen?)
        }
        if (!rayTrace.a().equals(pos)) {
            return; // block in the way
        }
        if (CraftEventFactory.callEntityChangeBlockEvent(enderman, pos, Blocks.AIR.getBlockData()).isCancelled()) {
            return; // plugin cancelled
        }
        enderman.world.setAir(pos);
        enderman.setCarried(Block.getValidBlockForPosition(state, enderman.world, pos));
    }
}
