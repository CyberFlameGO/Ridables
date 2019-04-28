package net.pl3x.bukkit.ridables.entity.monster.zombie;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.Blocks;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityIronGolem;
import net.minecraft.server.v1_14_R1.EntityPigZombie;
import net.minecraft.server.v1_14_R1.EntityTurtle;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EntityVillagerAbstract;
import net.minecraft.server.v1_14_R1.EntityZombie;
import net.minecraft.server.v1_14_R1.EnumDifficulty;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.GeneratorAccess;
import net.minecraft.server.v1_14_R1.PathfinderGoal;
import net.minecraft.server.v1_14_R1.PathfinderGoalBreakDoor;
import net.minecraft.server.v1_14_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_14_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_14_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_14_R1.PathfinderGoalRemoveBlock;
import net.minecraft.server.v1_14_R1.PathfinderGoalZombieAttack;
import net.minecraft.server.v1_14_R1.SoundCategory;
import net.minecraft.server.v1_14_R1.SoundEffects;
import net.minecraft.server.v1_14_R1.Vec3D;
import net.minecraft.server.v1_14_R1.World;
import net.pl3x.bukkit.ridables.configuration.mob.ZombieConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

import java.lang.reflect.Field;

public class RidableZombie extends EntityZombie implements RidableEntity {
    private static ZombieConfig config;

    private final ControllerWASD controllerWASD;

    public RidableZombie(EntityTypes<? extends EntityZombie> entitytypes, World world) {
        super(entitytypes, world);
        moveController = controllerWASD = new ControllerWASD(this);
        lookController = new LookController(this);

        if (config == null) {
            config = getConfig();
        }

        RidableZombie.setBreakDoorsGoal(this, new PathfinderGoalBreakDoor(this, difficulty -> difficulty == EnumDifficulty.HARD) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
    }

    @Override
    public RidableType getType() {
        return RidableType.ZOMBIE;
    }

    @Override
    public ControllerWASD getController() {
        return controllerWASD;
    }

    @Override
    public ZombieConfig getConfig() {
        return (ZombieConfig) getType().getConfig();
    }

    @Override
    public double getRidingSpeed() {
        return config.RIDING_SPEED;
    }

    @Override
    protected void initPathfinder() {
        // from EntityZombie
        goalSelector.a(4, new PathfinderGoalRemoveBlock(Blocks.TURTLE_EGG, this, 1.0D, 3) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }

            public void a(GeneratorAccess world, BlockPosition pos) { // playBreakingSound
                world.a(null, pos, SoundEffects.ENTITY_ZOMBIE_DESTROY_EGG, SoundCategory.HOSTILE, 0.5F, 0.9F + getRandom().nextFloat() * 0.2F);
            }

            public void a(World world, BlockPosition pos) { // playBrokenSound
                world.a(null, pos, SoundEffects.ENTITY_TURTLE_EGG_BREAK, SoundCategory.BLOCKS, 0.7F, 0.9F + world.random.nextFloat() * 0.2F);
            }

            public double h() { // getTargetDistanceSq
                return 1.14D;
            }
        });
        goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        goalSelector.a(8, new PathfinderGoalRandomLookaround(this) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        l();
    }

    @Override
    protected void l() {
        // also from EntityZombie
        goalSelector.a(2, new PathfinderGoalZombieAttack(this, 1.0D, false) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, true, 4, this::ef) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        targetSelector.a(1, new PathfinderGoalHurtByTarget(this) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        }.a(EntityPigZombie.class));
        targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        if (world.spigotConfig.zombieAggressiveTowardsVillager) {
            targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<EntityVillagerAbstract>(this, EntityVillagerAbstract.class, false) {
                public boolean a() { // shouldExecute
                    return getRider() == null && super.a();
                }

                public boolean b() { // shouldContinueExecuting
                    return getRider() == null && super.b();
                }
            });
        }
        targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<EntityIronGolem>(this, EntityIronGolem.class, true) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        targetSelector.a(5, new PathfinderGoalNearestAttackableTarget<EntityTurtle>(this, EntityTurtle.class, 10, true, false, EntityTurtle.bz) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
    }

    // canBeRiddenInWater
    @Override
    public boolean be() {
        return config.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cW() {
        return getRider() == null ? super.cW() : config.RIDING_JUMP_POWER;
    }

    @Override
    protected void mobTick() {
        K = getRider() == null ? 0.6F : config.RIDING_STEP_HEIGHT;
        super.mobTick();
    }

    // travel
    @Override
    public void e(Vec3D motion) {
        super.e(motion);
        checkMove();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            if (!config.RIDING_BABIES && isBaby()) {
                return false; // do not ride babies
            }
            return tryRide(entityhuman, config.RIDING_SADDLE_REQUIRE, config.RIDING_SADDLE_CONSUME);
        }
        return false;
    }

    private static Field breakDoorsGoal;

    static {
        try {
            breakDoorsGoal = EntityZombie.class.getDeclaredField("bD");
            breakDoorsGoal.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    public static void setBreakDoorsGoal(EntityZombie zombie, PathfinderGoal pathfinderGoal) {
        try {
            breakDoorsGoal.set(zombie, pathfinderGoal);
        } catch (IllegalAccessException ignore) {
        }
    }
}
