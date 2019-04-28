package net.pl3x.bukkit.ridables.entity.monster.slime;

import net.minecraft.server.v1_14_R1.ControllerMove;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityIronGolem;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.EntitySlime;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.GenericAttributes;
import net.minecraft.server.v1_14_R1.MobEffects;
import net.minecraft.server.v1_14_R1.PathfinderGoal;
import net.minecraft.server.v1_14_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_14_R1.Vec3D;
import net.minecraft.server.v1_14_R1.World;
import net.pl3x.bukkit.ridables.configuration.mob.SlimeConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import net.pl3x.bukkit.ridables.util.Const;

import java.util.EnumSet;

public class RidableSlime extends EntitySlime implements RidableEntity {
    private static SlimeConfig config;

    private final SlimeControllerWASD controllerWASD;

    private int spacebarCharge = 0;
    private int prevSpacebarCharge = 0;
    private float fallDistanceCharge = 0;

    public RidableSlime(EntityTypes<? extends EntitySlime> entitytypes, World world) {
        super(entitytypes, world);
        moveController = controllerWASD = new SlimeControllerWASD(this);
        lookController = new LookController(this);

        if (config == null) {
            config = getConfig();
        }
    }

    @Override
    public RidableType getType() {
        return RidableType.SLIME;
    }

    @Override
    public SlimeControllerWASD getController() {
        return controllerWASD;
    }

    @Override
    public SlimeConfig getConfig() {
        return (SlimeConfig) getType().getConfig();
    }

    @Override
    public double getRidingSpeed() {
        return config.RIDING_SPEED;
    }

    @Override
    protected void initPathfinder() {
        PathfinderGoal aiSlimeSwim = new PathfinderGoal() {
            public boolean a() {
                return (isInWater() || aC()) && getControllerMove() instanceof SlimeControllerWASD; // isInLava
            }

            public void e() { // tick
                if (random.nextFloat() < 0.8F) {
                    getControllerJump().jump();
                }
                getController().setSpeed(1.2D);
            }
        };
        aiSlimeSwim.a(EnumSet.of(PathfinderGoal.Type.JUMP, PathfinderGoal.Type.MOVE));
        getNavigation().d(true); // setCanSwim
        goalSelector.a(1, aiSlimeSwim);
        PathfinderGoal aiSlimeFindNearestPlayer = new PathfinderGoal() {
            private EntityLiving target;
            private int targetTimer;

            public boolean a() { // shouldExecute
                target = getGoalTarget();
                if (getRider() != null) {
                    return false;
                }
                if (target == null || !target.isAlive()) {
                    return false;
                }
                if (target instanceof EntityHuman && ((EntityHuman) target).abilities.isInvulnerable) {
                    return false;
                }
                return getControllerMove() instanceof SlimeControllerWASD;
            }

            public boolean b() { // shouldContinueExecuting
                if (target == null || !target.isAlive()) {
                    return false;
                }
                if (getRider() != null) {
                    return false;
                }
                if (target instanceof EntityHuman && ((EntityHuman) target).abilities.isInvulnerable) {
                    return false;
                }
                return --targetTimer > 0;
            }

            public void c() { // startExecuting
                this.targetTimer = 300;
                super.c();
            }

            public void e() { // tick
                RidableSlime.this.a(target, 10.0F, 10.0F);
                getController().setDirection(yaw, dV()); // canDamagePlayer
            }
        };
        aiSlimeFindNearestPlayer.a(EnumSet.of(PathfinderGoal.Type.LOOK));
        goalSelector.a(2, aiSlimeFindNearestPlayer);
        PathfinderGoal aiSlimeFaceRandom = new PathfinderGoal() {
            private float chosenYaw;
            private int faceTimer;

            public boolean a() { // shouldExecute
                return getRider() == null && getGoalTarget() == null && (onGround || isInWater() || aC() || hasEffect(MobEffects.LEVITATION)) && getControllerMove() instanceof SlimeControllerWASD; // isInLava
            }

            public void e() { // tick
                if (--faceTimer <= 0) {
                    faceTimer = 40 + random.nextInt(60);
                    chosenYaw = (float) random.nextInt(360);
                }
                getController().setDirection(chosenYaw, false);
            }
        };
        aiSlimeFaceRandom.a(EnumSet.of(PathfinderGoal.Type.LOOK));
        goalSelector.a(3, aiSlimeFaceRandom);
        PathfinderGoal aiSlimeHop = new PathfinderGoal() {
            public boolean a() { // shouldExecute
                return getRider() == null && !isPassenger();
            }

            @Override
            public void e() { // tick
                getController().setSpeed(1.0D);
            }
        };
        aiSlimeHop.a(EnumSet.of(PathfinderGoal.Type.JUMP, PathfinderGoal.Type.MOVE));
        goalSelector.a(5, aiSlimeHop);
        targetSelector.a(1, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, 10, true, false,
                (entityliving) -> Math.abs(entityliving.locY - this.locY) <= 4.0D) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<EntityIronGolem>(this, EntityIronGolem.class, true) {
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
        return getRider() == null ? super.cW() : (config.RIDING_JUMP_POWER * getJumpCharge());
    }

    @Override
    protected void mobTick() {
        if (spacebarCharge == prevSpacebarCharge) {
            spacebarCharge = 0;
        }
        prevSpacebarCharge = spacebarCharge;
        super.mobTick();
    }

    // travel
    @Override
    public void e(Vec3D motion) {
        super.e(motion);
        checkMove();
    }

    public float getJumpCharge() {
        float charge = 1F;
        if (getRider() != null && spacebarCharge > 0) {
            charge += 1F * (fallDistanceCharge = (spacebarCharge / 72F));
        } else {
            fallDistanceCharge = 0;
        }
        return charge;
    }

    // fall
    @Override
    public void b(float distance, float damageMultiplier) {
        if (getRider() != null && fallDistanceCharge > 0) {
            distance = distance - fallDistanceCharge;
        }
        super.b(distance, damageMultiplier);
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            return tryRide(entityhuman, config.RIDING_SADDLE_REQUIRE, config.RIDING_SADDLE_CONSUME);
        }
        return false;
    }

    @Override
    public boolean onSpacebar() {
        if (getRider().getBukkitEntity().hasPermission("ridables.special.slime")) {
            spacebarCharge++;
            if (spacebarCharge > 50) {
                spacebarCharge -= 2;
            }
        }
        return false;
    }

    static class SlimeControllerWASD extends ControllerWASD {
        private final RidableSlime slime;
        private float yRot;
        private int jumpDelay;
        private boolean isAggressive;

        SlimeControllerWASD(RidableSlime slime) {
            super(slime);
            this.slime = slime;
            yRot = slime.yaw * Const.RAD2DEG_FLOAT;
        }

        public void setDirection(float yRot, boolean isAggressive) {
            this.yRot = yRot;
            this.isAggressive = isAggressive;
        }

        public void setSpeed(double speed) {
            e = speed;
            h = ControllerMove.Operation.MOVE_TO;
        }

        @Override
        public void tick() {
            slime.aK = slime.aM = slime.yaw = a(slime.yaw, yRot, 90.0F);
            if (h != ControllerMove.Operation.MOVE_TO) {
                slime.r(0.0F); // forward
                return;
            }
            h = ControllerMove.Operation.WAIT;
            if (slime.onGround) {
                slime.o((float) (e * a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
                if (jumpDelay-- <= 0) {
                    jumpDelay = slime.dT(); // getJumpDelay
                    if (isAggressive) {
                        jumpDelay /= 3;
                    }
                    slime.getControllerJump().jump(); // setJumping
                    if (slime.eb()) { // makeSoundOnJump
                        slime.a(slime.getSoundJump(), slime.getSoundVolume(), ((slime.getRandom().nextFloat() - slime.getRandom().nextFloat()) * 0.2F + 1.0F) * 0.8F); // playSound
                    }
                } else {
                    slime.t(0.0F); // moveStrafing
                    slime.r(0.0F); // moveForward
                    slime.o(0.0F); // setSpeed
                }
                return;
            }
            slime.o((float) (e * a.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue()));
        }
    }
}
