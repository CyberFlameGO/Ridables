package net.pl3x.bukkit.ridables.entity.animal;

import net.minecraft.server.v1_13_R2.ControllerMove;
import net.minecraft.server.v1_13_R2.CriterionTriggers;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityDolphin;
import net.minecraft.server.v1_13_R2.EntityGuardian;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.Particles;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.minecraft.server.v1_13_R2.WorldServer;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.configuration.mob.DolphinConfig;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASDWater;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWanderSwim;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.dolphin.AIDolphinBreath;
import net.pl3x.bukkit.ridables.entity.ai.goal.dolphin.AIDolphinFindWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.dolphin.AIDolphinFollowBoat;
import net.pl3x.bukkit.ridables.entity.ai.goal.dolphin.AIDolphinPlayWithItems;
import net.pl3x.bukkit.ridables.entity.ai.goal.dolphin.AIDolphinSwimToTreasure;
import net.pl3x.bukkit.ridables.entity.ai.goal.dolphin.AIDolphinSwimWithPlayer;
import net.pl3x.bukkit.ridables.entity.ai.goal.dolphin.AIDolphinWaterJump;
import net.pl3x.bukkit.ridables.entity.projectile.DolphinSpit;
import net.pl3x.bukkit.ridables.event.DolphinSpitEvent;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import net.pl3x.bukkit.ridables.util.Const;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RidableDolphin extends EntityDolphin implements RidableEntity {
    public static final DolphinConfig CONFIG = new DolphinConfig();

    private int bounceCounter = 0;
    private boolean bounceUp = false;
    private int spacebarCooldown = 0;
    private boolean dashing = false;
    private int dashCounter = 0;

    public RidableDolphin(World world) {
        super(world);
        moveController = new DolphinWASDController(this);
        lookController = new DolphinLookController(this, 10);
    }

    @Override
    public RidableType getType() {
        return RidableType.DOLPHIN;
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
        goalSelector.a(0, new AIDolphinBreath(this));
        goalSelector.a(0, new AIDolphinFindWater(this));
        goalSelector.a(1, new AIDolphinSwimToTreasure(this));
        goalSelector.a(2, new AIDolphinSwimWithPlayer(this, 4.0D));
        goalSelector.a(4, new AIWanderSwim(this, 1.0D, 10));
        goalSelector.a(4, new AILookIdle(this));
        goalSelector.a(5, new AIWatchClosest(this, EntityHuman.class, 6.0F));
        goalSelector.a(5, new AIDolphinWaterJump(this, 10));
        goalSelector.a(6, new AIAttackMelee(this, (double) 1.2F, true));
        goalSelector.a(8, new AIDolphinPlayWithItems(this));
        goalSelector.a(8, new AIDolphinFollowBoat(this));
        goalSelector.a(9, new AIAvoidTarget<>(this, EntityGuardian.class, 8.0F, 1.0D, 1.0D));
        targetSelector.a(1, new AIHurtByTarget(this, true, EntityGuardian.class));
    }

    // canBeRiddenInWater
    @Override
    public boolean aY() {
        return true;
    }

    // canBeRidden
    @Override
    protected boolean n(Entity entity) {
        return k <= 0; // rideCooldown
    }

    public boolean nearTargetPos() {
        return dA();
    }

    @Override
    protected void mobTick() {
        if (++bounceCounter > 10) {
            bounceCounter = 0;
            bounceUp = !bounceUp;
        }
        if (spacebarCooldown > 0) {
            spacebarCooldown--;
        }
        if (dashing) {
            if (++dashCounter > CONFIG.RIDING_DASH_DURATION) {
                dashCounter = 0;
                dashing = false;
            }
        }

        if (getRider() != null && getAirTicks() > 150 && isInWater()) {
            if (CONFIG.RIDING_BUBBLES) {
                double velocity = motX * motX + motY * motY + motZ * motZ;
                if (velocity > 0.2 || velocity < -0.2) {
                    int i = (int) (velocity * 5);
                    for (int j = 0; j < i; j++) {
                        ((WorldServer) world).sendParticles(null, Particles.e,
                                lastX + random.nextFloat() / 2 - 0.25F,
                                lastY + random.nextFloat() / 2 - 0.25F,
                                lastZ + random.nextFloat() / 2 - 0.25F,
                                1, 0, 0, 0, 0, true);
                    }
                }
            }

            motY += 0.005D;
            if (CONFIG.RIDING_BOUNCE && getRider().bj == 0) {
                motY += bounceUp ? 0.005D : -0.005D;
            }
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
        if (collectInWaterBucket(entityhuman, hand)) {
            return true; // handled
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
    public boolean onSpacebar() {
        if (spacebarCooldown == 0 && CONFIG.RIDING_SPACEBAR_MODE != null) {
            EntityPlayer rider = getRider();
            if (rider != null) {
                if (CONFIG.RIDING_SPACEBAR_MODE.equalsIgnoreCase("shoot")) {
                    return shoot(rider);
                } else if (CONFIG.RIDING_SPACEBAR_MODE.equalsIgnoreCase("dash")) {
                    return dash(rider);
                }
            }
        }
        return false;
    }

    public boolean shoot(EntityPlayer rider) {
        spacebarCooldown = CONFIG.RIDING_SHOOT_COOLDOWN;
        if (rider == null) {
            return false;
        }

        CraftPlayer player = rider.getBukkitEntity();
        if (!player.hasPermission("allow.shoot.dolphin")) {
            Lang.send(player, Lang.SHOOT_NO_PERMISSION);
            return false;
        }

        Location loc = player.getEyeLocation();
        loc.setPitch(loc.getPitch() - 10);
        Vector target = loc.getDirection().normalize().multiply(10).add(loc.toVector());

        DolphinSpit spit = new DolphinSpit(world, this, rider);
        spit.shoot(target.getX() - locX, target.getY() - locY, target.getZ() - locZ, CONFIG.RIDING_SHOOT_SPEED, 5.0F);

        if (!new DolphinSpitEvent(this, spit).callEvent()) {
            return false; // cancelled
        }

        world.addEntity(spit);
        a(SoundEffects.ENTITY_DOLPHIN_ATTACK, 1.0F, 1.0F); // playSound
        return true;
    }

    public boolean dash(EntityPlayer rider) {
        spacebarCooldown = CONFIG.RIDING_DASH_COOLDOWN;
        if (!dashing) {
            if (rider != null && !rider.getBukkitEntity().hasPermission("allow.special.dolphin")) {
                return false;
            }

            dashing = true;
            dashCounter = 0;
            a(SoundEffects.ENTITY_DOLPHIN_JUMP, 1.0F, 1.0F); // playSound
            return true;
        }
        return false;
    }

    class DolphinLookController extends LookController {
        private final int h;

        public DolphinLookController(RidableDolphin dolphin, int var2) {
            super(dolphin);
            this.h = var2;
        }

        @Override
        public void tick() {
            if (d) { // isLooking
                d = false; // isLooking
                double x = e - a.locX;
                double y = f - (a.locY + (double) a.getHeadHeight());
                double z = g - a.locZ;
                a.pitch = a(a.pitch, (float) (-(MathHelper.c(y, (double) MathHelper.sqrt(x * x + z * z)) * Const.RAD2DEG)) + 10.0F, c);
                a.aS = a(a.aS, (float) (MathHelper.c(z, x) * Const.RAD2DEG) - 90.0F + 20.0F, b); // rotationYawHead
            } else {
                if (a.getNavigation().p()) { // noPath
                    a.pitch = a(a.pitch, 0.0F, 5.0F);
                }
                a.aS = a(a.aS, a.aQ, b); // rotationYawHead
            }
            float yawDiff = MathHelper.g(a.aS - a.aQ);
            if (yawDiff < (float) (-h)) {
                a.aQ -= 4.0F; // renderYawOffset
            } else if (yawDiff > (float) h) {
                a.aQ += 4.0F; // renderYawOffset
            }
        }
    }

    static class DolphinWASDController extends ControllerWASDWater {
        private final RidableDolphin dolphin;

        public DolphinWASDController(RidableDolphin dolphin) {
            super(dolphin);
            this.dolphin = dolphin;
        }

        @Override
        public void tick() {
            if (dolphin.isInWater()) {
                dolphin.motY += 0.005D;
            }

            if (h == ControllerMove.Operation.MOVE_TO && !dolphin.getNavigation().p()) {
                double x = b - dolphin.locX;
                double y = c - dolphin.locY;
                double z = d - dolphin.locZ;
                double distance = x * x + y * y + z * z;

                if (distance < 2.5E-7D) {
                    a.r(0.0F);
                } else {
                    float f = (float) (MathHelper.c(z, x) * Const.RAD2DEG) - 90.0F;

                    dolphin.aS = dolphin.aQ = dolphin.yaw = a(dolphin.yaw, f, 10.0F);
                    float speed = (float) (e * dolphin.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());

                    if (dolphin.isInWater()) {
                        dolphin.o(speed * 0.02F);
                        float f2 = -((float) (MathHelper.c(y, (double) MathHelper.sqrt(x * x + z * z)) * Const.RAD2DEG));
                        f2 = MathHelper.a(MathHelper.g(f2), -85.0F, 85.0F);
                        dolphin.pitch = a(dolphin.pitch, f2, 5.0F);
                        dolphin.bj = MathHelper.cos(dolphin.pitch * Const.DEG2RAD_FLOAT) * speed;
                        dolphin.bi = -MathHelper.sin(dolphin.pitch * Const.DEG2RAD_FLOAT) * speed;
                    } else {
                        dolphin.o(speed * 0.1F);
                    }
                }
            } else {
                dolphin.o(0.0F);
                dolphin.t(0.0F);
                dolphin.s(0.0F);
                dolphin.r(0.0F);
            }
        }
    }
}
