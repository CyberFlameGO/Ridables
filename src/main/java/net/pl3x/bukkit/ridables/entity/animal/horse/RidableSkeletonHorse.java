package net.pl3x.bukkit.ridables.entity.animal.horse;

import net.minecraft.server.v1_14_R1.EntityHorseAbstract;
import net.minecraft.server.v1_14_R1.EntityHorseSkeleton;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.PathfinderGoal;
import net.minecraft.server.v1_14_R1.PathfinderGoalBreed;
import net.minecraft.server.v1_14_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_14_R1.PathfinderGoalFollowParent;
import net.minecraft.server.v1_14_R1.PathfinderGoalHorseTrap;
import net.minecraft.server.v1_14_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_14_R1.PathfinderGoalPanic;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_14_R1.PathfinderGoalTame;
import net.minecraft.server.v1_14_R1.Vec3D;
import net.minecraft.server.v1_14_R1.World;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.configuration.mob.SkeletonHorseConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class RidableSkeletonHorse extends EntityHorseSkeleton implements RidableEntity {
    private static SkeletonHorseConfig config;

    public RidableSkeletonHorse(EntityTypes<? extends EntityHorseSkeleton> entitytypes, World world) {
        super(entitytypes, world);

        if (config == null) {
            config = getConfig();
        }

        setTrapGoal(this, new PathfinderGoalHorseTrap(this) {
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
        return RidableType.SKELETON_HORSE;
    }

    @Override
    public ControllerWASD getController() {
        return null; // use vanilla's controller
    }

    @Override
    public SkeletonHorseConfig getConfig() {
        return (SkeletonHorseConfig) getType().getConfig();
    }

    @Override
    public double getRidingSpeed() {
        return config.RIDING_SPEED;
    }

    @Override
    protected void initPathfinder() {
        // from EntityHorseAbstract
        goalSelector.a(1, new PathfinderGoalPanic(this, 1.2D) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        goalSelector.a(1, new PathfinderGoalTame(this, 1.2D) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        goalSelector.a(2, new PathfinderGoalBreed(this, 1.0D, EntityHorseAbstract.class) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        goalSelector.a(4, new PathfinderGoalFollowParent(this, 1.0D) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        goalSelector.a(6, new PathfinderGoalRandomStrollLand(this, 0.7D) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F) {
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
        ee(); // initExtraAI
    }

    // initExtraAI
    @Override
    protected void ee() {
        // add ability to swim if configured
        goalSelector.a(0, new PathfinderGoalFloat(this) {
            public boolean a() { // shouldExecute
                return config.FLOATS_IN_WATER && super.a();
            }
        });
    }

    // canBeRiddenInWater
    @Override
    public boolean be() {
        return config.RIDING_RIDE_IN_WATER;
    }

    @Override
    public boolean isTamed() {
        return true; // always tame
    }

    // getJumpUpwardsMotion
    @Override
    protected float cW() {
        float jump = getRider() == null ? super.cW() : config.RIDING_JUMP_POWER;
        return jump > 0.0F ? jump : super.cW();
    }

    @Override
    public void mobTick() {
        K = getRider() == null ? 1.0F : config.RIDING_STEP_HEIGHT;
        super.mobTick();
    }

    // travel
    @Override
    public void e(Vec3D motion) {
        super.e(motion);
        //checkMove(); // not needed
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (isBaby() && config.RIDING_BABIES && hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            g(entityhuman); // mountTo
            return true;
        }
        return false;
    }

    // mountTo
    @Override
    public void g(EntityHuman entityhuman) {
        Player player = (Player) entityhuman.getBukkitEntity();
        if (!player.hasPermission("ridables.ride.skeleton_horse")) {
            Lang.send(player, Lang.RIDE_NO_PERMISSION);
            return;
        }
        super.g(entityhuman);
        entityhuman.o(false); // setJumping - fixes jump on mount
    }

    private static Field trapGoal;

    static {
        try {
            trapGoal = EntityHorseSkeleton.class.getDeclaredField("bJ");
            trapGoal.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    public static void setTrapGoal(EntityHorseSkeleton horse, PathfinderGoal pathfinderGoal) {
        try {
            trapGoal.set(horse, pathfinderGoal);
        } catch (IllegalAccessException ignore) {
        }
    }
}
