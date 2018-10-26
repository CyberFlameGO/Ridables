package net.pl3x.bukkit.ridables.entity.monster.spider;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityCaveSpider;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.CaveSpiderConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILeapAtTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.spider.AISpiderAttack;
import net.pl3x.bukkit.ridables.entity.ai.goal.spider.AISpiderTarget;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;

public class RidableCaveSpider extends EntityCaveSpider implements RidableEntity {
    public static final CaveSpiderConfig CONFIG = new CaveSpiderConfig();

    public RidableCaveSpider(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.CAVE_SPIDER;
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDE_SPEED); // registerAttribute
        reloadAttributes();
    }

    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDE_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_ATTACK_DAMAGE);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(1, new AISwim(this));
        goalSelector.a(3, new AILeapAtTarget(this, 0.4F));
        goalSelector.a(4, new AISpiderAttack(this));
        goalSelector.a(5, new AIWanderAvoidWater(this, 0.8D));
        goalSelector.a(6, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(6, new AILookIdle(this));
        targetSelector.a(1, new AIHurtByTarget(this, false));
        targetSelector.a(2, new AISpiderTarget<>(this, EntityHuman.class));
        targetSelector.a(3, new AISpiderTarget<>(this, EntityIronGolem.class));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return getRider() == null ? super.cG() : CONFIG.RIDING_JUMP_POWER;
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    // isOnLadder
    public boolean z_() {
        return getRider() == null ? l() : CONFIG.RIDING_CLIMB_WALLS && l(); // isBesideClimbableBlock
    }

    // travel
    public void a(float strafe, float vertical, float forward) {
        super.a(strafe, vertical, forward);
        if (positionChanged && z_() && getRider() != null) { // isOnLadder
            motY = 0.2D * CONFIG.RIDING_CLIMB_SPEED;
        }
    }

    public boolean onClick() {
        // TODO shoot webs
        return false;
    }
}
