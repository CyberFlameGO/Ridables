package net.pl3x.bukkit.ridables.entity.animal.horse;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHorseAbstract;
import net.minecraft.server.v1_13_R2.EntityHorseMule;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.MuleConfig;
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

public class RidableMule extends EntityHorseMule implements RidableEntity {
    public static final MuleConfig CONFIG = new MuleConfig();

    public RidableMule(World world) {
        super(world);
    }

    public RidableType getType() {
        return RidableType.MULE;
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED);
        reloadAttributes();
    }

    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDE_SPEED);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(attributeJumpStrength).setValue(CONFIG.JUMP_POWER);
        if (CONFIG.MAX_HEALTH > 0.0D) {
            getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        }
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
        goalSelector.a(0, new AISwim(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    public boolean isTamed() {
        return true;
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

    public RidableMule createChild(EntityAgeable entity) {
        return new RidableMule(world);
    }
}
