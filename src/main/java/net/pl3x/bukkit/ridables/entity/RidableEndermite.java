package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityEndermite;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.EndermiteConfig;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;

public class RidableEndermite extends EntityEndermite implements RidableEntity {
    public static final EndermiteConfig CONFIG = new EndermiteConfig();

    public RidableEndermite(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.ENDERMITE;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(1, new AISwim(this));
        goalSelector.a(2, new AIAttackMelee(this, 1.0D, false));
        goalSelector.a(3, new AIWanderAvoidWater(this, 1.0D));
        goalSelector.a(7, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(8, new AILookIdle(this));
        targetSelector.a(1, new AIHurtByTarget(this, true));
        targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return CONFIG.JUMP_POWER;
    }

    protected void mobTick() {
        Q = CONFIG.STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return CONFIG.SPEED;
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }
}
