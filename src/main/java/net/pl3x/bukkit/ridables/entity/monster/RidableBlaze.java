package net.pl3x.bukkit.ridables.entity.monster;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityBlaze;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.configuration.mob.BlazeConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASDFlyingWithSpacebar;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.blaze.AIBlazeFireballAttack;
import net.pl3x.bukkit.ridables.entity.projectile.CustomFireball;
import net.pl3x.bukkit.ridables.event.BlazeShootFireballEvent;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RidableBlaze extends EntityBlaze implements RidableEntity {
    public static final BlazeConfig CONFIG = new BlazeConfig();

    private int shootCooldown = 0;

    public RidableBlaze(World world) {
        super(world);
        moveController = new ControllerWASDFlyingWithSpacebar(this, 0.5D);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.BLAZE;
    }

    // isNoDespawnRequired
    @Override
    public boolean isPersistent() {
        return persistent;
    }

    // canDespawn
    @Override
    public boolean isTypeNotPersistent() {
        return !hasCustomName() && !isLeashed();
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDING_SPEED); // registerAttribute
        getAttributeMap().b(RidableType.RIDING_MAX_Y); // registerAttribute
        reloadAttributes();
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(RidableType.RIDING_MAX_Y).setValue(CONFIG.RIDING_FLYING_MAX_Y);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_MELEE_DAMAGE);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        goalSelector.a(4, new AIBlazeFireballAttack(this));
        goalSelector.a(5, new AIMoveTowardsRestriction(this, 1.0D));
        goalSelector.a(7, new AIWanderAvoidWater(this, 1.0D, 0.0F));
        goalSelector.a(8, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(8, new AILookIdle(this));
        targetSelector.a(1, new AIHurtByTarget(this, true));
        targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
    }


    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return CONFIG.RIDING_RIDE_IN_WATER;
    }

    @Override
    protected void mobTick() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        if (getRider() != null) {
            motY += bi > 0 ? 0.07D * CONFIG.RIDING_VERTICAL : 0.04704D - CONFIG.RIDING_GRAVITY; // moveVertical
        }
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

    @Override
    public boolean onClick(org.bukkit.entity.Entity entity, EnumHand hand) {
        return handleClick();
    }

    @Override
    public boolean onClick(Block block, BlockFace blockFace, EnumHand hand) {
        return handleClick();
    }

    @Override
    public boolean onClick(EnumHand hand) {
        return handleClick();
    }

    private boolean handleClick() {
        if (shootCooldown == 0) {
            EntityPlayer rider = getRider();
            if (rider != null) {
                return shoot(rider);
            }
        }
        return false;
    }

    public boolean shoot(EntityPlayer rider) {
        shootCooldown = CONFIG.RIDING_SHOOT_COOLDOWN;

        if (rider == null) {
            return false;
        }

        CraftPlayer player = rider.getBukkitEntity();
        if (!player.hasPermission("allow.shoot.blaze")) {
            Lang.send(player, Lang.SHOOT_NO_PERMISSION);
            return false;
        }

        Vector direction = player.getEyeLocation().getDirection().normalize().multiply(25).add(new Vector(0, 1, 0));

        CustomFireball fireball = new CustomFireball(world, this, rider, direction.getX(), direction.getY(), direction.getZ(),
                CONFIG.RIDING_SHOOT_SPEED, CONFIG.RIDING_SHOOT_IMPACT_DAMAGE, CONFIG.RIDING_SHOOT_GRIEF);
        fireball.isIncendiary = CONFIG.RIDING_SHOOT_EXPLOSION_FIRE;

        if (!new BlazeShootFireballEvent(this, fireball).callEvent()) {
            return false; // cancelled
        }

        world.addEntity(fireball);

        world.triggerEffect(1018, new BlockPosition(locX, locY, locZ), 0);
        a(SoundEffects.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F); // playSound

        return true;
    }
}
