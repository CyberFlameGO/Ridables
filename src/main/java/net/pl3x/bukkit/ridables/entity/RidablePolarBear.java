package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityPolarBear;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.mob.PolarBearConfig;
import net.pl3x.bukkit.ridables.entity.ai.AIFollowParent;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWander;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.polar_bear.AIPolarBearAttack;
import net.pl3x.bukkit.ridables.entity.ai.polar_bear.AIPolarBearAttackPlayer;
import net.pl3x.bukkit.ridables.entity.ai.polar_bear.AIPolarBearHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.polar_bear.AIPolarBearPanic;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import org.bukkit.scheduler.BukkitRunnable;

public class RidablePolarBear extends EntityPolarBear implements RidableEntity {
    public static final PolarBearConfig CONFIG = new PolarBearConfig();

    public RidablePolarBear(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.POLAR_BEAR;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(0, new AISwim(this));
        goalSelector.a(1, new AIPolarBearAttack(this));
        goalSelector.a(1, new AIPolarBearPanic(this));
        goalSelector.a(4, new AIFollowParent(this, 1.25D));
        goalSelector.a(5, new AIWander(this, 1.0D));
        goalSelector.a(6, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(7, new AILookIdle(this));
        targetSelector.a(1, new AIPolarBearHurtByTarget(this));
        targetSelector.a(2, new AIPolarBearAttackPlayer(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return isStanding() ? 0 : CONFIG.JUMP_POWER;
    }

    public void playWarningSound() {
        dy();
    }

    protected void mobTick() {
        Q = CONFIG.STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return isStanding() ? 0 : CONFIG.SPEED;
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    public RidablePolarBear createChild(EntityAgeable entity) {
        return new RidablePolarBear(world);
    }

    public boolean onSpacebar() {
        if (CONFIG.STAND_ON_SPACEBAR && !isStanding()) {
            EntityPlayer rider = getRider();
            if (rider != null && rider.bj == 0 && rider.bh == 0) {
                setStanding(true);
                a(SoundEffects.ENTITY_POLAR_BEAR_WARNING, 1.0F, 1.0F);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        setStanding(false);
                    }
                }.runTaskLater(Ridables.getInstance(), 20);
                return true;
            }
        }
        return false;
    }

    private boolean isStanding() {
        return dz();
    }

    private void setStanding(boolean standing) {
        s(standing);
    }
}
