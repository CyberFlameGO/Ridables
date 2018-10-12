package net.pl3x.bukkit.ridables.entity;

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
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIMeleeAttack;
import net.pl3x.bukkit.ridables.entity.ai.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableGiant extends EntityGiantZombie implements RidableEntity {
    public RidableGiant(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
        initAI();
    }

    public RidableType getType() {
        return RidableType.GIANT;
    }

    // initAI - override vanilla AI
    protected void n() {
    }

    private void initAI() {
        if (Config.GIANT_AI_ENABLED) {
            goalSelector.a(0, new AISwim(this));
            goalSelector.a(2, new AIMeleeAttack(this, 1.0D, false));
            goalSelector.a(7, new AIWanderAvoidWater(this, 1.0D));
            goalSelector.a(8, new AIWatchClosest(this, EntityHuman.class, 16.0F));
            goalSelector.a(8, new AILookIdle(this));
            targetSelector.a(1, new AIHurtByTarget(this, true, EntityHuman.class));
            if (Config.GIANT_HOSTILE) {
                goalSelector.a(5, new AIMoveTowardsRestriction(this, 1.0D));
                targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
                targetSelector.a(3, new AIAttackNearest<>(this, EntityVillager.class, false));
                targetSelector.a(3, new AIAttackNearest<>(this, EntityIronGolem.class, true));
            }
        }
    }

    protected void initAttributes() {
        super.initAttributes();
        if (Config.GIANT_AI_ENABLED) {
            getAttributeInstance(GenericAttributes.maxHealth).setValue(Config.GIANT_MAX_HEALTH);
            setHealth(Config.GIANT_MAX_HEALTH);

            getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(Config.GIANT_AI_SPEED);
            getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(Config.GIANT_FOLLOW_RANGE);
            getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(Config.GIANT_ATTACK_DAMAGE);
        }
    }

    // canBeRiddenInWater
    public boolean aY() {
        return Config.GIANT_RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return Config.GIANT_JUMP_POWER;
    }

    // isValidLightLevel
    protected boolean K_() {
        BlockPosition pos = new BlockPosition(locX, getBoundingBox().b, locZ);
        return (world.Y() ? world.getLightLevel(pos, 10) : world.getLightLevel(pos)) <= Config.GIANT_SPAWN_LIGHT_LEVEL;
    }

    // func_205022_a
    public float a(BlockPosition pos, IWorldReader world) {
        return 1.0F;
    }

    // canSpawn
    public boolean a(GeneratorAccess world) {
        return super.a(world) && a(new BlockPosition(locX, getBoundingBox().b, locZ), world) >= 0.0F;
    }

    protected void mobTick() {
        Q = Config.GIANT_STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return Config.GIANT_SPEED;
    }

    // processInteract
    public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        if (passengers.isEmpty() && !entityhuman.isPassenger() && !entityhuman.isSneaking()) {
            return enumhand == EnumHand.MAIN_HAND && tryRide(entityhuman, entityhuman.b(enumhand));
        }
        return passengers.isEmpty() && super.a(entityhuman, enumhand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }
}
