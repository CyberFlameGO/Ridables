package net.pl3x.bukkit.ridables.entity.monster;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityGiantZombie;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntityVillager;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GeneratorAccess;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IWorldReader;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.GiantConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;

public class RidableGiant extends EntityGiantZombie implements RidableEntity {
    public static GiantConfig CONFIG = new GiantConfig();

    public RidableGiant(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.GIANT;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        if (CONFIG.AI_ENABLED) {
            goalSelector.a(0, new AISwim(this));
            goalSelector.a(2, new AIAttackMelee(this, 1.0D, false));
            goalSelector.a(7, new AIWanderAvoidWater(this, 1.0D));
            goalSelector.a(8, new AIWatchClosest(this, EntityHuman.class, 16.0F));
            goalSelector.a(8, new AILookIdle(this));
            targetSelector.a(1, new AIHurtByTarget(this, true, EntityHuman.class));
            if (CONFIG.AI_HOSTILE) {
                goalSelector.a(5, new AIMoveTowardsRestriction(this, 1.0D));
                targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
                targetSelector.a(3, new AIAttackNearest<>(this, EntityVillager.class, false));
                targetSelector.a(3, new AIAttackNearest<>(this, EntityIronGolem.class, true));
            }
        }
    }

    @Override
    public void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        reloadAttributes();
        setHealth(CONFIG.AI_HEALTH);
    }

    @Override
    public void reloadAttributes() {
        super.initAttributes();
        if (CONFIG.AI_ENABLED) {
            getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.AI_HEALTH);
            getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.AI_SPEED);
            getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
            getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_MELEE_DAMAGE);
        }
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return CONFIG.JUMP_POWER;
    }

    // isValidLightLevel
    @Override
    protected boolean K_() {
        BlockPosition pos = new BlockPosition(locX, getBoundingBox().minY, locZ);
        return (world.Y() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos)) <= CONFIG.SPAWN_LIGHT_LEVEL;
    }

    // func_205022_a
    @Override
    public float a(BlockPosition pos, IWorldReader world) {
        return 1.0F;
    }

    // canSpawn
    @Override
    public boolean a(GeneratorAccess world, boolean var2) {
        return super.a(world) && a(new BlockPosition(locX, getBoundingBox().minY, locZ), world) >= 0.0F;
    }

    @Override
    protected void mobTick() {
        Q = CONFIG.STEP_HEIGHT;
        super.mobTick();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    @Override
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }
}
