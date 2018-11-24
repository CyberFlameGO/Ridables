package net.pl3x.bukkit.ridables.entity.monster.spider;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntitySpider;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.configuration.mob.SpiderConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILeapAtTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.spider.AISpiderAttack;
import net.pl3x.bukkit.ridables.entity.ai.goal.spider.AISpiderTarget;
import net.pl3x.bukkit.ridables.entity.projectile.SpiderWeb;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RidableSpider extends EntitySpider implements RidableEntity {
    public static final SpiderConfig CONFIG = new SpiderConfig();

    private int shootCooldown = 0;

    public RidableSpider(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.SPIDER;
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
        reloadAttributes();
    }

    @Override
    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDING_SPEED).setValue(CONFIG.RIDING_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_MELEE_DAMAGE);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
    @Override
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
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        super.mobTick();
    }

    // travel
    @Override
    public void a(float strafe, float vertical, float forward) {
        super.a(strafe, vertical, forward);
        if (positionChanged && z_() && getRider() != null) {
            motY = 0.2D * CONFIG.RIDING_CLIMB_SPEED;
        }
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

    // isOnLadder
    @Override
    public boolean z_() {
        if (getRider() == null) {
            return l(); // isBesideClimbableBlock
        }
        return CONFIG.RIDING_CLIMB_WALLS && l();
    }

    @Override
    public boolean onClick() {
        if (shootCooldown == 0) {
            return shoot(getRider());
        }
        return false;
    }

    public boolean shoot(EntityPlayer rider) {
        shootCooldown = CONFIG.RIDING_SHOOT_COOLDOWN;

        if (rider == null) {
            return false;
        }

        CraftPlayer player = (CraftPlayer) ((Entity) rider).getBukkitEntity();
        if (!player.hasPermission("allow.shoot.spider")) {
            Lang.send(player, Lang.SHOOT_NO_PERMISSION);
            return false;
        }

        Vector direction = player.getEyeLocation().getDirection()
                .normalize().multiply(25).add(new Vector(0, 10, 0)).normalize().multiply(25);

        a(SoundEffects.ENTITY_SPIDER_STEP, 1.0F, 1.0F);

        SpiderWeb web = new SpiderWeb(world, this, rider, locX, locY, locZ);
        web.shoot(direction.getX(), direction.getY(), direction.getZ(), CONFIG.RIDING_SHOOT_SPEED);
        world.addEntity(web);

        return true;
    }
}
