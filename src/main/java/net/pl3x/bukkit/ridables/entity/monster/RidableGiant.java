package net.pl3x.bukkit.ridables.entity.monster;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.Blocks;
import net.minecraft.server.v1_14_R1.EntityGiantZombie;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityIronGolem;
import net.minecraft.server.v1_14_R1.EntityPigZombie;
import net.minecraft.server.v1_14_R1.EntityTurtle;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EntityVillagerAbstract;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.GeneratorAccess;
import net.minecraft.server.v1_14_R1.GenericAttributes;
import net.minecraft.server.v1_14_R1.IWorldReader;
import net.minecraft.server.v1_14_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_14_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_14_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_14_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_14_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_14_R1.PathfinderGoalRemoveBlock;
import net.minecraft.server.v1_14_R1.SoundCategory;
import net.minecraft.server.v1_14_R1.SoundEffects;
import net.minecraft.server.v1_14_R1.Vec3D;
import net.minecraft.server.v1_14_R1.World;
import net.pl3x.bukkit.ridables.configuration.mob.GiantConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableGiant extends EntityGiantZombie implements RidableEntity {
    private static GiantConfig config = new GiantConfig();
    private final ControllerWASD controllerWASD;

    public RidableGiant(EntityTypes<? extends EntityGiantZombie> entitytypes, World world) {
        super(entitytypes, world);
        moveController = controllerWASD = new ControllerWASD(this);
        lookController = new LookController(this);

        if (config == null) {
            config = getConfig();
        }
    }

    @Override
    public RidableType getType() {
        return RidableType.GIANT;
    }

    @Override
    public ControllerWASD getController() {
        return controllerWASD;
    }

    @Override
    public GiantConfig getConfig() {
        return (GiantConfig) getType().getConfig();
    }

    @Override
    public double getRidingSpeed() {
        return config.RIDING_SPEED;
    }

    @Override
    public void initAttributes() {
        super.initAttributes();
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(config.AI_SPEED);
        if (config.AI_ENABLED) {
            getAttributeInstance(GenericAttributes.MAX_HEALTH).setValue(config.MAX_HEALTH);
            getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(config.AI_MELEE_DAMAGE);
            getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(config.AI_FOLLOW_RANGE);
        }
        setHealth(getMaxHealth());
    }

    @Override
    protected void initPathfinder() {
        goalSelector.a(0, new PathfinderGoalFloat(this));
        initAIPathfinder();
        initHostileAIPathfinder();
    }

    private void initAIPathfinder() {
        if (!config.AI_ENABLED) {
            return; // AI disabled
        }
        goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D) {
            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }
        });
        goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 16.0F) {
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
        if (config.AI_HOSTILE) {
            initHostileAIPathfinder();
        }
    }

    private void initHostileAIPathfinder() {
        if (!config.AI_ENABLED) {
            return; // AI disabled
        }
        if (!config.AI_HOSTILE) {
            return; // hostile AI disabled
        }
        goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false) { // PathfinderGoalZombieAttack
            private int raiseArmTicks;

            public boolean a() { // shouldExecute
                return getRider() == null && super.a();
            }

            public boolean b() { // shouldContinueExecuting
                return getRider() == null && super.b();
            }

            public void c() { // startExecuting
                super.c();
                raiseArmTicks = 0;
            }

            public void d() { // resetTask
                super.d();
                q(false);
            }

            public void e() { // tick
                super.e();
                ++raiseArmTicks;
                if (raiseArmTicks >= 5 && b < 10) {
                    q(true);
                } else {
                    q(false);
                }
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
        if (config.AI_HOSTILE_TO_PLAYERS) {
            targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(this, EntityHuman.class, true) {
                public boolean a() { // shouldExecute
                    return getRider() == null && super.a();
                }

                public boolean b() { // shouldContinueExecuting
                    return getRider() == null && super.b();
                }
            });
        }
        if (config.AI_HOSTILE_TO_VILLAGERS) {
            targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<EntityVillagerAbstract>(this, EntityVillagerAbstract.class, false) {
                public boolean a() { // shouldExecute
                    return getRider() == null && super.a();
                }

                public boolean b() { // shouldContinueExecuting
                    return getRider() == null && super.b();
                }
            });
        }
        if (config.AI_HOSTILE_TO_IRON_GOLEMS) {
            targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<EntityIronGolem>(this, EntityIronGolem.class, true) {
                public boolean a() { // shouldExecute
                    return getRider() == null && super.a();
                }

                public boolean b() { // shouldContinueExecuting
                    return getRider() == null && super.b();
                }
            });
        }
        if (config.AI_HOSTILE_TO_TURTLES) {
            targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<EntityTurtle>(this, EntityTurtle.class, true) {
                public boolean a() { // shouldExecute
                    return getRider() == null && super.a();
                }

                public boolean b() { // shouldContinueExecuting
                    return getRider() == null && super.b();
                }
            });
        }
        if (config.AI_HOSTILE_TO_TURTLE_EGGS) {
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
        }
    }

    // canBeRiddenInWater
    @Override
    public boolean be() {
        return config.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cW() {
        return getRider() == null ? config.AI_JUMP_POWER : config.RIDING_JUMP_POWER;
    }

    // getBlockPathWeight
    @Override
    public float a(BlockPosition pos, IWorldReader world) {
        return 0.5F - world.w(pos); // getBrightness
    }

    @Override
    protected void mobTick() {
        K = getRider() == null ? config.AI_STEP_HEIGHT : config.RIDING_STEP_HEIGHT;
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
            return tryRide(entityhuman, config.RIDING_SADDLE_REQUIRE, config.RIDING_SADDLE_CONSUME);
        }
        return false;
    }
}
