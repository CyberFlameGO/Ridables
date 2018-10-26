package net.pl3x.bukkit.ridables.entity.ai.goal.zombie;

import net.minecraft.server.v1_13_R2.Block;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EntityCreature;
import net.minecraft.server.v1_13_R2.GeneratorAccess;
import net.minecraft.server.v1_13_R2.PathfinderGoalRemoveBlock;
import net.minecraft.server.v1_13_R2.SoundCategory;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.entity.RidableEntity;

public class AIZombieAttackTurtleEgg extends PathfinderGoalRemoveBlock {
    private final RidableEntity ridable;

    public AIZombieAttackTurtleEgg(Block block, RidableEntity ridable, double d0, int i) {
        super(block, (EntityCreature) ridable, d0, i);
        this.ridable = ridable;
    }

    // shouldExecute
    public boolean a() {
        return ridable.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return ridable.getRider() == null && super.b();
    }

    // playBreakingSound
    public void a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        generatoraccess.a(null, blockposition, SoundEffects.ENTITY_ZOMBIE_DESTROY_EGG, SoundCategory.HOSTILE, 0.5F, 0.9F + getEntity().getRandom().nextFloat() * 0.2F);
    }

    // playBrokenSound
    public void a(World world, BlockPosition blockposition) {
        world.a(null, blockposition, SoundEffects.ENTITY_TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 0.7F, 0.9F + world.random.nextFloat() * 0.2F);
    }

    // getTargetDistanceSq
    public double g() {
        return 1.3D;
    }
}

