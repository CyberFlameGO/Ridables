package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.DifficultyDamageScaler;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPhantom;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.GroupDataEntity;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.Vec3D;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.mob.PhantomConfig;
import net.pl3x.bukkit.ridables.entity.ai.phantom.AIPhantomAttack;
import net.pl3x.bukkit.ridables.entity.ai.phantom.AIPhantomOrbitPoint;
import net.pl3x.bukkit.ridables.entity.ai.phantom.AIPhantomPickAttack;
import net.pl3x.bukkit.ridables.entity.ai.phantom.AIPhantomSweepAttack;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlying;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import net.pl3x.bukkit.ridables.entity.projectile.PhantomFlames;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;

public class RidablePhantom extends EntityPhantom implements RidableEntity {
    public static final PhantomConfig CONFIG = new PhantomConfig();

    public Vec3D orbitOffset;
    public BlockPosition orbitPosition;
    public AttackPhase phase;

    public RidablePhantom(World world) {
        super(world);
        moveController = new PhantomWASDController(this);
        lookController = new PhantomLookController(this);
        orbitOffset = Vec3D.a;
        orbitPosition = BlockPosition.ZERO;
        phase = AttackPhase.CIRCLE;
    }

    public RidableType getType() {
        return RidableType.PHANTOM;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(1, new AIPhantomPickAttack(this));
        goalSelector.a(2, new AIPhantomSweepAttack(this));
        goalSelector.a(3, new AIPhantomOrbitPoint(this));
        targetSelector.a(1, new AIPhantomAttack(this));
    }

    // readNBT
    public void a(NBTTagCompound nbt) {
        super.a(nbt);
        if (nbt.hasKey("AX")) {
            orbitPosition = new BlockPosition(nbt.getInt("AX"), nbt.getInt("AY"), nbt.getInt("AZ"));
        }
    }

    // writeNBT
    public void b(NBTTagCompound nbt) {
        super.b(nbt);
        nbt.setInt("AX", orbitPosition.getX());
        nbt.setInt("AY", orbitPosition.getY());
        nbt.setInt("AZ", orbitPosition.getZ());
    }

    public GroupDataEntity prepare(DifficultyDamageScaler scaler, @Nullable GroupDataEntity group, @Nullable NBTTagCompound nbt) {
        orbitPosition = (new BlockPosition(this)).up(5);
        return super.prepare(scaler, group, nbt);
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    protected void mobTick() {
        if (getRider() != null && getRider().bj == 0) {
            motY -= CONFIG.GRAVITY;
        }
        super.mobTick();
    }

    // onLivingUpdate
    public void k() {
        super.k();
        if (!CONFIG.BURN_IN_SUNLIGHT) {
            extinguish(); // dont burn in sunlight
        }
        if (dq()) {
            setGoalTarget(null, null, false);
        }
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }

    public boolean onSpacebar() {
        EntityPlayer rider = getRider();
        if (rider != null && hasShootPerm(rider.getBukkitEntity())) {
            shoot(getRider());
        }
        return false;
    }

    public boolean shoot(EntityPlayer rider) {
        Location loc = ((LivingEntity) getBukkitEntity()).getEyeLocation();
        loc.setPitch(-loc.getPitch());
        Vector target = loc.getDirection().normalize().multiply(100).add(loc.toVector());

        PhantomFlames flames = new PhantomFlames(world, this, rider);
        flames.shoot(target.getX() - locX, target.getY() - locY, target.getZ() - locZ, 1.0F, 5.0F);
        world.addEntity(flames);
        return true;
    }

    public boolean canAttack() {
        return !CONFIG.ATTACK_IN_DAYLIGHT && dq();
    }

    class PhantomLookController extends LookController {
        PhantomLookController(RidableEntity ridable) {
            super(ridable);
        }

        @Override
        public void tick(EntityPlayer rider) {
            setYawPitch(rider.yaw, -rider.pitch * 0.75F);
        }
    }

    class PhantomWASDController extends ControllerWASDFlying {
        private final RidablePhantom phantom;
        private float speed = 0.1F;

        PhantomWASDController(RidablePhantom phantom) {
            super(phantom);
            this.phantom = phantom;
        }

        public void tick() {
            if (phantom.positionChanged) {
                phantom.yaw += 180.0F;
                speed = 0.1F;
            }

            float f = (float) (phantom.orbitOffset.x - phantom.locX);
            float f1 = (float) (phantom.orbitOffset.y - phantom.locY);
            float f2 = (float) (phantom.orbitOffset.z - phantom.locZ);
            double d0 = (double) MathHelper.c(f * f + f2 * f2);
            double d1 = 1.0D - (double) MathHelper.e(f1 * 0.7F) / d0;

            f = (float) ((double) f * d1);
            f2 = (float) ((double) f2 * d1);
            d0 = (double) MathHelper.c(f * f + f2 * f2);
            double d2 = (double) MathHelper.c(f * f + f2 * f2 + f1 * f1);
            float f3 = phantom.yaw;
            float f4 = (float) MathHelper.c((double) f2, (double) f);
            float f5 = MathHelper.g(phantom.yaw + 90.0F);
            float f6 = MathHelper.g(f4 * 57.295776F);

            phantom.yaw = MathHelper.c(f5, f6, 4.0F) - 90.0F;
            phantom.aQ = phantom.yaw;
            if (MathHelper.d(f3, phantom.yaw) < 3.0F) {
                speed = MathHelper.b(speed, 1.8F, 0.005F * (1.8F / speed));
            } else {
                speed = MathHelper.b(speed, 0.2F, 0.025F);
            }

            float f7 = (float) (-(MathHelper.c((double) (-f1), d0) * 57.2957763671875D));

            phantom.pitch = f7;
            float f8 = phantom.yaw + 90.0F;
            double d3 = (double) (speed * MathHelper.cos(f8 * 0.017453292F)) * Math.abs((double) f / d2);
            double d4 = (double) (speed * MathHelper.sin(f8 * 0.017453292F)) * Math.abs((double) f2 / d2);
            double d5 = (double) (speed * MathHelper.sin(f7 * 0.017453292F)) * Math.abs((double) f1 / d2);

            phantom.motX += (d3 - phantom.motX) * 0.2D;
            phantom.motY += (d5 - phantom.motY) * 0.2D;
            phantom.motZ += (d4 - phantom.motZ) * 0.2D;
        }
    }

    public enum AttackPhase {
        CIRCLE, SWOOP
    }
}
