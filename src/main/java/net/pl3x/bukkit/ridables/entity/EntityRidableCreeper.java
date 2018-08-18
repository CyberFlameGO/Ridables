package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerLook;
import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.DataWatcherObject;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityCreeper;
import net.minecraft.server.v1_13_R1.EntityHuman;
import net.minecraft.server.v1_13_R1.EntityLiving;
import net.minecraft.server.v1_13_R1.EntityOcelot;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EnumHand;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.PathfinderGoalAvoidTarget;
import net.minecraft.server.v1_13_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_13_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_13_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_13_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_13_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_13_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_13_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_13_R1.PathfinderGoalSwell;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.controller.BlankLookController;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.lang.reflect.Field;

public class EntityRidableCreeper extends EntityCreeper implements RidableEntity {
    private static Field ignited_field;

    static {
        try {
            ignited_field = EntityCreeper.class.getDeclaredField("c");
            ignited_field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private ControllerMove aiController;
    private ControllerWASD wasdController;
    private ControllerLook defaultLookController;
    private BlankLookController blankLookController;

    private PathfinderGoalNearestAttackableTarget goalTargetPlayer;
    private PathfinderGoalHurtByTarget goalTargetHurtBy;

    public EntityRidableCreeper(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASD(this);
        defaultLookController = lookController;
        blankLookController = new BlankLookController(this);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return false;
    }

    public boolean aY() {
        return true; // dont eject passengers when in water
    }

    protected void mobTick() {
        EntityPlayer rider = getRider();
        if (rider != null) {
            super.setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
            if (rider.bj != 0 || rider.bh != 0) {
                disarm();
            }
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
        return isIgnited() ? 0 : Config.CREEPER_JUMP_POWER;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.CREEPER_SPEED;
    }

    public EntityPlayer getRider() {
        if (passengers != null && !passengers.isEmpty()) {
            Entity entity = passengers.get(0);
            if (entity instanceof EntityPlayer) {
                return (EntityPlayer) entity;
            }
        }
        return null;
    }

    public void useAIController() {
        if (moveController != aiController) {
            moveController = aiController;
            lookController = defaultLookController;
            targetSelector.a(1, goalTargetPlayer);
            targetSelector.a(2, goalTargetHurtBy);
        }
    }

    public void useWASDController() {
        if (moveController != wasdController) {
            moveController = wasdController;
            lookController = blankLookController;
            targetSelector.a(goalTargetPlayer);
            targetSelector.a(goalTargetHurtBy);
            disarm();
        }
    }

    public boolean onSpacebar() {
        if (!isIgnited()) {
            EntityPlayer rider = getRider();
            if (rider != null && rider.bj == 0 && rider.bh == 0 &&
                    rider.getBukkitEntity().hasPermission("allow.special.creeper")) {
                setIgnited(true);
                return true;
            }
        }
        return false;
    }

    public void setGoalTarget(@Nullable EntityLiving entityliving) {
        setGoalTarget(entityliving, EntityTargetEvent.TargetReason.UNKNOWN, true);
    }

    public boolean setGoalTarget(EntityLiving entityliving, EntityTargetEvent.TargetReason reason, boolean fireEvent) {
        return getRider() != null && super.setGoalTarget(entityliving, reason, fireEvent);
    }

    // processInteract
    protected boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        return getRider() != null && super.a(entityhuman, enumhand);
    }

    // initEntityAI
    protected void n() {
        goalTargetPlayer = new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true);
        goalTargetHurtBy = new PathfinderGoalHurtByTarget(this, false, new Class[0]);

        goalSelector.a(1, new PathfinderGoalFloat(this));
        goalSelector.a(2, new PathfinderGoalSwell(this));
        goalSelector.a(3, new PathfinderGoalAvoidTarget(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
        goalSelector.a(4, new PathfinderGoalMeleeAttack(this, 1.0D, false));
        goalSelector.a(5, new PathfinderGoalRandomStrollLand(this, 0.8D));
        goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        goalSelector.a(6, new PathfinderGoalRandomLookaround(this));

        targetSelector.a(1, goalTargetPlayer);
        targetSelector.a(2, goalTargetHurtBy);
    }

    public void disarm() {
        setIgnited(false);
        a(-1); // setSwellState
    }

    /**
     * Set ignited state of a ridable creeper
     *
     * @param ignited Ignited state to set
     */
    public void setIgnited(boolean ignited) {
        if (ignited) {
            dC();
        } else {
            try {
                getDataWatcher().set((DataWatcherObject<Boolean>) ignited_field.get(this), false);
            } catch (IllegalAccessException ignore) {
            }
        }
    }
}
