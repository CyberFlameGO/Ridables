package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.DamageSource;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityCreeper;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.IMonster;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.IronGolemConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveThroughVillage;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.iron_golem.AIIronGolemDefendVillage;
import net.pl3x.bukkit.ridables.entity.ai.goal.iron_golem.AIIronGolemOfferFlower;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class RidableIronGolem extends EntityIronGolem implements RidableEntity {
    public static final IronGolemConfig CONFIG = new IronGolemConfig();

    public RidableIronGolem(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.IRON_GOLEM;
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        reloadAttributes();
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
        getAttributeInstance(GenericAttributes.c).setValue(CONFIG.AI_KNOCKBACK_RESISTANCE);
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        goalSelector.a(1, new AIAttackMelee(this, 1.0D, true));
        goalSelector.a(2, new AIMoveTowardsTarget(this, 0.9D, 32.0F));
        goalSelector.a(3, new AIMoveThroughVillage(this, 0.6D, true));
        goalSelector.a(4, new AIMoveTowardsRestriction(this, 1.0D));
        goalSelector.a(5, new AIIronGolemOfferFlower(this));
        goalSelector.a(6, new AIWanderAvoidWater(this, 0.6D));
        goalSelector.a(7, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(8, new AILookIdle(this));
        targetSelector.a(1, new AIIronGolemDefendVillage(this));
        targetSelector.a(2, new AIHurtByTarget(this, false));
        targetSelector.a(3, new AIAttackNearest<>(this, EntityInsentient.class, 10, false, true,
                target -> target != null && IMonster.e.test(target) && !(target instanceof EntityCreeper)));
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    // getJumpUpwardsMotion
    @Override
    protected float cG() {
        return getRider() == null ? CONFIG.AI_JUMP_POWER : CONFIG.RIDING_JUMP_POWER;
    }

    @Override
    protected void mobTick() {
        Q = getRider() == null ? CONFIG.AI_STEP_HEIGHT : CONFIG.RIDING_STEP_HEIGHT;
        super.mobTick();
    }

    // travel
    @Override
    public void a(float strafe, float vertical, float forward) {
        super.a(strafe, vertical, forward);
        checkMove();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            return tryRide(entityhuman, CONFIG.RIDING_SADDLE_REQUIRE, CONFIG.RIDING_SADDLE_CONSUME);
        }
        return false;
    }

    @Override
    public boolean removePassenger(Entity passenger, boolean notCancellable) {
        if (passenger instanceof EntityPlayer && !passengers.isEmpty() && passenger == passengers.get(0)) {
            if (!new RidableDismountEvent(this, (Player) passenger.getBukkitEntity(), notCancellable).callEvent() && !notCancellable) {
                return false; // cancelled
            }
        }
        return super.removePassenger(passenger, notCancellable);
    }

    // attackEntityAsMob
    @Override
    public boolean B(Entity entity) {
        //bC = 10; // attackTimer (not used by server?)
        world.broadcastEntityEffect(this, (byte) 4);
        boolean damaged = entity.damageEntity(DamageSource.mobAttack(this), (float) (CONFIG.AI_MELEE_DAMAGE < 0 ? 7 + random.nextInt(15) : CONFIG.AI_MELEE_DAMAGE));
        if (damaged) {
            entity.motY += 0.4D;
            a(this, entity); // applyEnchantments
        }
        a(SoundEffects.ENTITY_IRON_GOLEM_ATTACK, 1.0F, 1.0F); // playSound
        return damaged;
    }

    @Override
    public boolean onClick(org.bukkit.entity.Entity entity, EnumHand hand) {
        handleClick(hand);
        return false;
    }

    @Override
    public boolean onClick(Block block, BlockFace blockFace, EnumHand hand) {
        handleClick(hand);
        return false;
    }

    @Override
    public boolean onClick(EnumHand hand) {
        handleClick(hand);
        return false;
    }

    private void handleClick(EnumHand hand) {
        // handle right click (toggle rose)
        if (hand == EnumHand.OFF_HAND) {
            a(dz() == 0); // setHoldingRose getHoldRoseTick
            return;
        }

        // handle left click (swing arms)
        if (dz() > 0) { // getHoldRoseTick
            // remove rose if one is in hand
            a(false); // setHoldingRose
        }
        world.broadcastEntityEffect(this, (byte) 4); // swing arms
    }
}
