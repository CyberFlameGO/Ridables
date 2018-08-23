package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerLook;
import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityGiantZombie;
import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityIronGolem;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EntityVillager;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_13_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_13_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_13_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_13_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_13_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_13_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.BlankLookController;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;

public class EntityRidableGiant extends EntityGiantZombie implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASD wasdController;
    private ControllerLook defaultLookController;
    private BlankLookController blankLookController;
    private EntityPlayer rider;

    public EntityRidableGiant(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASD(this);
        defaultLookController = lookController;
        blankLookController = new BlankLookController(this);
        Q = 3;
    }

    public RidableType getType() {
        return RidableType.GIANT;
    }

    public boolean aY() {
        return true;
    }

    protected void mobTick() {
        Q = 3;
        EntityPlayer rider = updateRider();
        if (rider != null) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
        }
        super.mobTick();
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return super.cG() * getJumpPower() * 2.2F;
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getJumpPower() {
        return Config.GIANT_JUMP_POWER;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.GIANT_SPEED;
    }

    public EntityPlayer getRider() {
        return rider;
    }

    public EntityPlayer updateRider() {
        if (passengers == null || passengers.isEmpty()) {
            rider = null;
        } else {
            Entity entity = passengers.get(0);
            rider = entity instanceof EntityPlayer ? (EntityPlayer) entity : null;
        }
        return rider;
    }

    public void useAIController() {
        if (moveController != aiController) {
            moveController = aiController;
            lookController = defaultLookController;
        }
    }

    public void useWASDController() {
        if (moveController != wasdController) {
            moveController = wasdController;
            lookController = blankLookController;
        }
    }

    protected void n() {
        super.n();
        if (Config.GIANT_AI_ENABLED) {
            goalSelector.a(0, new PathfinderGoalFloat(this));
            goalSelector.a(2, new PathfinderGoalMeleeAttack(this, 1.0D, false));
            goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0D));
            goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 16.0F));
            goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
            targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, EntityHuman.class));
            if (Config.GIANT_HOSTILE) {
                goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
                targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<>(this, EntityHuman.class, true));
                targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityVillager.class, false));
                targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityIronGolem.class, true));
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
}
