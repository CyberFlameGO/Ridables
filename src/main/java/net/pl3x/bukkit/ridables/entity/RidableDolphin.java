package net.pl3x.bukkit.ridables.entity;

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
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.entity.ai.AIAvoidTarget;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackMelee;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderSwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.dolphin.AIDolphinBreath;
import net.pl3x.bukkit.ridables.entity.ai.dolphin.AIDolphinFindWater;
import net.pl3x.bukkit.ridables.entity.ai.dolphin.AIDolphinFollowBoat;
import net.pl3x.bukkit.ridables.entity.ai.dolphin.AIDolphinPlayWithItems;
import net.pl3x.bukkit.ridables.entity.ai.dolphin.AIDolphinSwimToTreasure;
import net.pl3x.bukkit.ridables.entity.ai.dolphin.AIDolphinSwimWithPlayer;
import net.pl3x.bukkit.ridables.entity.ai.dolphin.AIDolphinWaterJump;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDWater;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import net.pl3x.bukkit.ridables.entity.projectile.DolphinSpit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RidableDolphin extends EntityDolphin implements RidableEntity {
    private final ControllerWASD wasdController;
    private int bounceCounter = 0;
    private boolean bounceUp = false;
    private int spacebarCooldown = 0;
    private boolean dashing = false;
    private int dashCounter = 0;

    public RidableDolphin(World world) {
        super(world);
        moveController = wasdController = new DolphinWASDController(this);
        lookController = new DolphinLookController(this, 10);
    }

    public RidableType getType() {
        return RidableType.DOLPHIN;
    }

    // initAI - override vanilla AI
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
    public boolean aY() {
        return true;
    }

    // canBeRidden
    protected boolean n(Entity entity) {
        return k <= 0; // rideCooldown
    }

    public boolean nearTargetPos() {
        return dA();
    }

    protected void mobTick() {
        if (++bounceCounter > 10) {
            bounceCounter = 0;
            bounceUp = !bounceUp;
        }
        if (spacebarCooldown > 0) {
            spacebarCooldown--;
        }
        if (dashing) {
            if (++dashCounter > Config.DOLPHIN_DASH_DURATION) {
                dashCounter = 0;
                dashing = false;
            }
        }

        if (getRider() != null && getAirTicks() > 150 && isInWater()) {
            if (Config.DOLPHIN_BUBBLES) {
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
            if (Config.DOLPHIN_BOUNCE && getRider().bj == 0) {
                motY += bounceUp ? 0.005D : -0.005D;
            }
        }
        super.mobTick();
    }

    public float getSpeed() {
        return Config.DOLPHIN_SPEED * (dashing ? Config.DOLPHIN_DASH_BOOST : 1);
    }

    // processInteract
    public boolean a(EntityHuman entityhuman, EnumHand enumhand) {
        ItemStack itemstack = entityhuman.b(enumhand);
        if (itemstack.getItem() == Items.WATER_BUCKET && isAlive() && hasCollectPerm((Player) entityhuman.getBukkitEntity())) {
            a(SoundEffects.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
            itemstack.subtract(1);
            ItemStack bucket = CraftItemStack.asNMSCopy(Bucket.DOLPHIN.getItemStack());
            // TODO set custom name
            CriterionTriggers.j.a((EntityPlayer) entityhuman, bucket);
            if (itemstack.isEmpty()) {
                entityhuman.a(enumhand, bucket);
            } else if (!entityhuman.inventory.pickup(bucket)) {
                entityhuman.drop(bucket, false);
            }
            die();
            return true;
        }
        if (passengers.isEmpty() && !entityhuman.isPassenger() && !entityhuman.isSneaking()) {
            return enumhand == EnumHand.MAIN_HAND && tryRide(entityhuman, itemstack);
        }
        return passengers.isEmpty() && super.a(entityhuman, enumhand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    public boolean onSpacebar() {
        if (spacebarCooldown == 0 && Config.DOLPHIN_SPACEBAR_MODE != null) {
            EntityPlayer rider = getRider();
            if (rider != null) {
                if (Config.DOLPHIN_SPACEBAR_MODE.equalsIgnoreCase("shoot")) {
                    return shoot(rider);
                } else if (Config.DOLPHIN_SPACEBAR_MODE.equalsIgnoreCase("dash")) {
                    return dash(rider);
                }
            }
        }
        return false;
    }

    public boolean shoot(EntityPlayer rider) {
        spacebarCooldown = Config.DOLPHIN_SHOOT_COOLDOWN;
        if (rider == null) {
            return false;
        }

        CraftPlayer player = rider.getBukkitEntity();
        if (!hasShootPerm(player)) {
            Lang.send(player, Lang.SHOOT_NO_PERMISSION);
            return false;
        }

        Location loc = player.getEyeLocation();
        loc.setPitch(loc.getPitch() - 10);
        Vector target = loc.getDirection().normalize().multiply(10).add(loc.toVector());

        DolphinSpit spit = new DolphinSpit(world, this, rider);
        spit.shoot(target.getX() - locX, target.getY() - locY, target.getZ() - locZ, Config.DOLPHIN_SHOOT_SPEED, 5.0F);
        world.addEntity(spit);

        a(SoundEffects.ENTITY_DOLPHIN_ATTACK, 1.0F, 1.0F);
        return true;
    }

    public boolean dash(EntityPlayer rider) {
        spacebarCooldown = Config.DOLPHIN_DASH_COOLDOWN;
        if (!dashing) {
            if (rider != null && !hasSpecialPerm(rider.getBukkitEntity())) {
                return false;
            }

            dashing = true;
            dashCounter = 0;
            a(SoundEffects.ENTITY_DOLPHIN_JUMP, 1.0F, 1.0F);
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

        public void tick() {
            if (d) { // isLooking
                d = false; // isLooking
                double x = e - a.locX;
                double y = f - (a.locY + (double) a.getHeadHeight());
                double z = g - a.locZ;
                a.pitch = a(a.pitch, (float) (-(MathHelper.c(y, (double) MathHelper.sqrt(x * x + z * z)) * (double) (180F / (float) Math.PI))) + 10.0F, c);
                a.aS = a(a.aS, (float) (MathHelper.c(z, x) * (double) (180F / (float) Math.PI)) - 90.0F + 20.0F, b); // rotationYawHead
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

        public void tick() {
            if (dolphin.isInWater()) {
                dolphin.motY += 0.005D;
            }

            if (h == ControllerMove.Operation.MOVE_TO && !dolphin.getNavigation().p()) {
                double x = b - dolphin.locX;
                double y = c - dolphin.locY;
                double z = d - dolphin.locZ;
                double distance = x * x + y * y + z * z;

                if (distance < 2.5D) {
                    a.r(0.0F);
                } else {
                    float f = (float) (MathHelper.c(z, x) * (double) (180F / (float) Math.PI)) - 90.0F;

                    dolphin.aS = dolphin.aQ = dolphin.yaw = a(dolphin.yaw, f, 10.0F);
                    float speed = (float) (e * dolphin.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());

                    if (dolphin.isInWater()) {
                        dolphin.o(speed * 0.02F);
                        float f2 = -((float) (MathHelper.c(y, (double) MathHelper.sqrt(x * x + z * z)) * (double) (180F / (float) Math.PI)));
                        f2 = MathHelper.a(MathHelper.g(f2), -85.0F, 85.0F);
                        dolphin.pitch = a(dolphin.pitch, f2, 5.0F);
                        dolphin.bj = MathHelper.cos(dolphin.pitch * ((float) Math.PI / 180F)) * speed;
                        dolphin.bi = -MathHelper.sin(dolphin.pitch * ((float) Math.PI / 180F)) * speed;
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
