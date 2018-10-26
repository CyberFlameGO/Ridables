package net.pl3x.bukkit.ridables.entity.monster;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntitySilverfish;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.SilverfishConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.silverfish.AISilverfishHideInBlock;
import net.pl3x.bukkit.ridables.entity.ai.goal.silverfish.AISilverfishWakeOthers;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;

import java.lang.reflect.Field;

public class RidableSilverfish extends EntitySilverfish implements RidableEntity {
    public static final SilverfishConfig CONFIG = new SilverfishConfig();

    private static Field summonSilverfish;

    static {
        try {
            summonSilverfish = EntitySilverfish.class.getDeclaredField("a");
            summonSilverfish.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    public RidableSilverfish(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.SILVERFISH;
    }

    // initAI - override vanilla AI
    protected void n() {
        AISilverfishWakeOthers wakeOthers = new AISilverfishWakeOthers(this);

        try {
            summonSilverfish.set(this, wakeOthers);
        } catch (IllegalAccessException ignore) {
        }

        goalSelector.a(1, new AISwim(this));
        goalSelector.a(3, wakeOthers);
        goalSelector.a(4, new AIAttackMelee(this, 1.0D, false));
        goalSelector.a(5, new AISilverfishHideInBlock(this));
        targetSelector.a(1, new AIHurtByTarget(this, true));
        targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return getRider() == null ? super.cG() : CONFIG.JUMP_POWER;
    }

    protected void mobTick() {
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
}
