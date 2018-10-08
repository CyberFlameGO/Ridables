package net.pl3x.bukkit.ridables.entity.ai.enderman;

import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.IBlockData;
import net.minecraft.server.v1_13_R2.IWorldReader;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.PathfinderGoal;
import net.pl3x.bukkit.ridables.entity.RidableEnderman;
import org.bukkit.craftbukkit.v1_13_R2.event.CraftEventFactory;

public class AIEndermanPlaceBlock extends PathfinderGoal {
    private final RidableEnderman enderman;

    public AIEndermanPlaceBlock(RidableEnderman enderman) {
        this.enderman = enderman;
    }

    // shouldExecute
    public boolean a() {
        if (enderman.getRider() != null) {
            return false;
        }
        if (enderman.getCarried() == null) {
            return false;
        }
        if (!enderman.world.getGameRules().getBoolean("mobGriefing")) {
            return false;
        }
        return enderman.getRandom().nextInt(2000) == 0;
    }

    // shouldContinueExecuting
    public boolean b() {
        return a();
    }

    // tick
    public void e() {
        int x = MathHelper.floor(enderman.locX - 1.0D + enderman.getRandom().nextDouble() * 2.0D);
        int y = MathHelper.floor(enderman.locY + enderman.getRandom().nextDouble() * 2.0D);
        int z = MathHelper.floor(enderman.locZ - 1.0D + enderman.getRandom().nextDouble() * 2.0D);
        BlockPosition pos = new BlockPosition(x, y, z);
        IBlockData state = enderman.world.getType(pos);
        IBlockData stateDown = enderman.world.getType(pos.down());
        IBlockData stateValid = Block.getValidBlockForPosition(enderman.getCarried(), enderman.world, pos);
        if (stateValid != null && isValidPlacement(enderman.world, pos, stateValid, state, stateDown)) {
            if (!CraftEventFactory.callEntityChangeBlockEvent(enderman, pos, stateValid).isCancelled()) {
                enderman.world.setTypeAndData(pos, stateValid, 3);
                enderman.setCarried(null);
            }
        }
    }

    private boolean isValidPlacement(IWorldReader world, BlockPosition pos, IBlockData newState, IBlockData oldState, IBlockData stateDown) {
        return oldState.isAir() && !stateDown.isAir() && stateDown.g() && newState.canPlace(world, pos);
    }
}
