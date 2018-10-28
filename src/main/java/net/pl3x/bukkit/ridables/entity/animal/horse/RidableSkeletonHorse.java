package net.pl3x.bukkit.ridables.entity.animal.horse;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHorseAbstract;
import net.minecraft.server.v1_13_R2.EntityHorseSkeleton;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.PathfinderGoalHorseTrap;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.SkeletonHorseConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIBreed;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIFollowParent;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIPanic;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.horse.AIHorseBucking;

public class RidableSkeletonHorse extends EntityHorseSkeleton implements RidableEntity {
    public static final SkeletonHorseConfig CONFIG = new SkeletonHorseConfig();

    private final PathfinderGoalHorseTrap aiSkeletonTrap = new PathfinderGoalHorseTrap(this);
    private boolean isTrap;

    public RidableSkeletonHorse(World world) {
        super(world);
    }

    public RidableType getType() {
        return RidableType.SKELETON_HORSE;
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED);
        reloadAttributes();
    }

    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDE_SPEED);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        if (CONFIG.JUMP_POWER > 0.0D) {
            getAttributeInstance(attributeJumpStrength).setValue(CONFIG.JUMP_POWER);
        }
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
    }

    // initAI - override vanilla AI
    protected void n() {
        // from EntityHorseAbstract
        goalSelector.a(1, new AIPanic(this, 1.2D));
        goalSelector.a(1, new AIHorseBucking(this, 1.2D));
        goalSelector.a(2, new AIBreed(this, 1.0D, EntityHorseAbstract.class));
        goalSelector.a(4, new AIFollowParent(this, 1.0D));
        goalSelector.a(6, new AIWanderAvoidWater(this, 0.7D));
        goalSelector.a(7, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(8, new AILookIdle(this));
        dI(); // initExtraAI
    }

    // initExtraAI
    protected void dI() {
        goalSelector.a(0, new AISwim(this) {
            // shouldExecute
            public boolean a() {
                return CONFIG.FLOATS_IN_WATER && super.a();
            }
        });
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    public boolean isTamed() {
        return true;
    }

    // isTrap
    public boolean dy() {
        return isTrap;
    }

    // setTrap
    public void s(boolean flag) {
        if (flag != isTrap) {
            if (isTrap = flag) {
                goalSelector.a(1, aiSkeletonTrap); // addTask
            } else {
                goalSelector.a(aiSkeletonTrap); // removeTask
            }
        }
    }

    public void mobTick() {
        Q = CONFIG.STEP_HEIGHT;
        super.mobTick();
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    public RidableSkeletonHorse createChild(EntityAgeable entity) {
        return new RidableSkeletonHorse(world);
    }
}
