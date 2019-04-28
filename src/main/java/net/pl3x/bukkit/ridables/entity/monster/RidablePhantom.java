package net.pl3x.bukkit.ridables.entity.monster;

import net.minecraft.server.v1_14_R1.BlockPosition;
import net.minecraft.server.v1_14_R1.DamageSource;
import net.minecraft.server.v1_14_R1.DifficultyDamageScaler;
import net.minecraft.server.v1_14_R1.EntityHuman;
import net.minecraft.server.v1_14_R1.EntityLiving;
import net.minecraft.server.v1_14_R1.EntityPhantom;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import net.minecraft.server.v1_14_R1.EntityTypes;
import net.minecraft.server.v1_14_R1.EnumHand;
import net.minecraft.server.v1_14_R1.EnumMobSpawn;
import net.minecraft.server.v1_14_R1.GeneratorAccess;
import net.minecraft.server.v1_14_R1.GroupDataEntity;
import net.minecraft.server.v1_14_R1.HeightMap;
import net.minecraft.server.v1_14_R1.ItemStack;
import net.minecraft.server.v1_14_R1.Items;
import net.minecraft.server.v1_14_R1.LootTableInfo;
import net.minecraft.server.v1_14_R1.MathHelper;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.PathfinderGoal;
import net.minecraft.server.v1_14_R1.PathfinderTargetCondition;
import net.minecraft.server.v1_14_R1.SoundEffects;
import net.minecraft.server.v1_14_R1.Vec3D;
import net.minecraft.server.v1_14_R1.World;
import net.pl3x.bukkit.ridables.configuration.mob.PhantomConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableFlyingEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.goal.phantom.AIPhantomAttack;
import net.pl3x.bukkit.ridables.entity.ai.goal.phantom.AIPhantomFindTotem;
import net.pl3x.bukkit.ridables.entity.ai.goal.phantom.AIPhantomOrbitTotem;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlying;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import net.pl3x.bukkit.ridables.entity.item.CustomEnderCrystal;
import net.pl3x.bukkit.ridables.entity.projectile.PhantomFlames;
import net.pl3x.bukkit.ridables.util.Const;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import javax.annotation.Nullable;

public class RidablePhantom extends EntityPhantom implements RidableEntity, RidableFlyingEntity {
    private static PhantomConfig config;

    private final PhantomControllerWASD controllerWASD;

    public Vec3D orbitOffset;
    public BlockPosition orbitPosition;
    public AttackPhase phase;
    public BlockPosition totemPosition;

    public RidablePhantom(EntityTypes<? extends EntityPhantom> entitytypes, World world) {
        super(entitytypes, world);
        moveController = controllerWASD = new PhantomControllerWASD(this);
        lookController = new PhantomLookController(this);
        orbitOffset = Vec3D.a;
        orbitPosition = BlockPosition.ZERO;
        phase = AttackPhase.CIRCLE;

        if (config == null) {
            config = getConfig();
        }
    }

    @Override
    public RidableType getType() {
        return RidableType.PHANTOM;
    }

    @Override
    public PhantomControllerWASD getController() {
        return controllerWASD;
    }

    @Override
    public PhantomConfig getConfig() {
        return (PhantomConfig) getType().getConfig();
    }

    @Override
    public double getRidingSpeed() {
        return config.RIDING_SPEED;
    }

    @Override
    public int getMaxY() {
        return config.RIDING_FLYING_MAX_Y;
    }

    @Override
    protected void initPathfinder() {
        goalSelector.a(1, new PathfinderGoal() { // pickAttack
            private EntityLiving target;
            private int b;

            public boolean a() { // shouldExecute
                target = getGoalTarget();
                return target != null && canAttack() && EntityPhantom.this.a(target, PathfinderTargetCondition.a);
            }

            public void c() { // startExecuting
                this.b = 10;
                EntityPhantom.this.bz = EntityPhantom.AttackPhase.CIRCLE;
                this.g();
            }

            public void d() { // resetTask
                EntityPhantom.this.d = EntityPhantom.this.world.getHighestBlockYAt(HeightMap.Type.MOTION_BLOCKING, EntityPhantom.this.d).up(10 + EntityPhantom.this.random.nextInt(20));
            }

            public void e() { // tick
                if (EntityPhantom.this.bz == EntityPhantom.AttackPhase.CIRCLE) {
                    --this.b;
                    if (this.b <= 0) {
                        EntityPhantom.this.bz = EntityPhantom.AttackPhase.SWOOP;
                        this.g();
                        this.b = (8 + EntityPhantom.this.random.nextInt(4)) * 20;
                        EntityPhantom.this.a(SoundEffects.ENTITY_PHANTOM_SWOOP, 10.0F, 0.95F + EntityPhantom.this.random.nextFloat() * 0.1F);
                    }
                }

            }

            private void g() {
                EntityPhantom.this.d = (new BlockPosition(EntityPhantom.this.getGoalTarget())).up(20 + EntityPhantom.this.random.nextInt(20));
                if (EntityPhantom.this.d.getY() < EntityPhantom.this.world.getSeaLevel()) {
                    EntityPhantom.this.d = new BlockPosition(EntityPhantom.this.d.getX(), EntityPhantom.this.world.getSeaLevel() + 1, EntityPhantom.this.d.getZ());
                }

            }
        });
        goalSelector.a(2, new EntityPhantom.i()); // sweepAttack
        goalSelector.a(3, new EntityPhantom.e()); // orbitPoint
        targetSelector.a(1, new EntityPhantom.b()); // attack

        goalSelector.a(0, new AIPhantomFindTotem(this));
        goalSelector.a(3, new AIPhantomOrbitTotem(this));
        targetSelector.a(1, new AIPhantomAttack(this));
    }

    // canBeRiddenInWater
    @Override
    public boolean be() {
        return config.RIDING_RIDE_IN_WATER;
    }

    // onLivingUpdate
    @Override
    public void movementTick() {
        super.movementTick();
        boolean hasRider = getRider() != null;
        if ((hasRider && !config.RIDING_BURN_IN_SUNLIGHT) || (!hasRider && !config.AI_BURN_IN_SUNLIGHT)) {
            extinguish(); // dont burn in sunlight
        }
        if (!config.AI_ATTACK_IN_SUNLIGHT && dS()) { // isInDaylight
            setGoalTarget(null, null, false);
        }
    }

    @Override
    protected void mobTick() {
        if (getRider() != null && ControllerWASD.getForward(getRider()) == 0) {
            setMot(getMot().add(0.0F, -config.RIDING_GRAVITY, 0.0F));
        }
        super.mobTick();
    }

    // travel
    @Override
    public void e(Vec3D motion) {
        super.e(motion);
        checkMove();
    }

    // processInteract
    @Override
    public boolean a(EntityHuman entityhuman, EnumHand hand) {
        if (super.a(entityhuman, hand)) {
            return true; // handled by vanilla action
        }
        if (hand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && passengers.isEmpty() && !entityhuman.isPassenger()) {
            return tryRide(entityhuman, config.RIDING_SADDLE_REQUIRE, config.RIDING_SADDLE_CONSUME);
        }
        return false;
    }

    @Override
    public boolean onSpacebar() {
        EntityPlayer rider = getRider();
        if (rider != null && rider.getBukkitEntity().hasPermission("ridables.shoot.phantom")) {
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

    public void setTotemPosition(BlockPosition pos) {
        if (config.AI_ENDER_CRYSTALS_ORBIT) {
            totemPosition = pos;
        }
    }

    public boolean isCirclingTotem() {
        return totemPosition != null;
    }

    // readNBT
    @Override
    public void a(NBTTagCompound nbt) {
        super.a(nbt);
        if (nbt.hasKey("AX")) {
            orbitPosition = new BlockPosition(nbt.getInt("AX"), nbt.getInt("AY"), nbt.getInt("AZ"));
        }
    }

    // writeNBT
    @Override
    public void b(NBTTagCompound nbt) {
        super.b(nbt);
        nbt.setInt("AX", orbitPosition.getX());
        nbt.setInt("AY", orbitPosition.getY());
        nbt.setInt("AZ", orbitPosition.getZ());
    }

    // dropLoot
    @Override
    protected LootTableInfo.Builder a(boolean wasRecentlyHit, DamageSource damagesource) {
        if (killer == null && damagesource.getEntity() instanceof CustomEnderCrystal) {
            if (random.nextInt(5) < 1) { // 1 out of 5 chance (20%)
                a(new ItemStack(Items.PHANTOM_MEMBRANE)); // dropItem
            }
        }
        return super.a(wasRecentlyHit, damagesource); // dropLoot
    }

    @Override
    public GroupDataEntity prepare(GeneratorAccess world, DifficultyDamageScaler scaler, EnumMobSpawn mobSpawn, @Nullable GroupDataEntity group, @Nullable NBTTagCompound nbt) {
        orbitPosition = (new BlockPosition(this)).up(5);
        setSize(0);
        return super.prepare(world, scaler, mobSpawn, group, nbt);
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

    class PhantomControllerWASD extends ControllerWASDFlying {
        private final RidablePhantom phantom;
        private float speed = 0.1F;

        PhantomControllerWASD(RidablePhantom phantom) {
            super(phantom);
            this.phantom = phantom;
        }

        @Override
        public void tick() {
            if (phantom.positionChanged) {
                phantom.yaw += 180.0F;
                speed = 0.1F;
            }

            float f = (float) (phantom.orbitOffset.x - phantom.locX);
            float f1 = (float) (phantom.orbitOffset.y - phantom.locY);
            float f2 = (float) (phantom.orbitOffset.z - phantom.locZ);
            double d0 = (double) MathHelper.c(f * f + f2 * f2); // sqrt
            double d1 = 1.0D - (double) MathHelper.e(f1 * 0.7F) / d0; // abs

            f = (float) ((double) f * d1);
            f2 = (float) ((double) f2 * d1);
            d0 = (double) MathHelper.c(f * f + f2 * f2); // sqrt
            double d2 = (double) MathHelper.c(f * f + f2 * f2 + f1 * f1); // sqrt
            float f3 = phantom.yaw;
            float f4 = (float) MathHelper.d((double) f2, (double) f); // atan2
            float f5 = MathHelper.g(phantom.yaw + 90.0F); // wrapDegrees
            float f6 = MathHelper.g(f4 * Const.RAD2DEG_FLOAT); // wrapDegrees

            phantom.yaw = MathHelper.c(f5, f6, 4.0F) - 90.0F; // approachDegrees
            phantom.aK = phantom.yaw;
            if (MathHelper.d(f3, phantom.yaw) < 3.0F) { // degreesDifferenceAbs
                speed = MathHelper.b(speed, 1.8F, 0.005F * (1.8F / speed)); // approach
            } else {
                speed = MathHelper.b(speed, 0.2F, 0.025F); // approach
            }

            float f7 = (float) (-(MathHelper.d((double) (-f1), d0) * Const.RAD2DEG)); // atan2
            phantom.pitch = f7;
            float f8 = phantom.yaw + 90.0F;

            double d3 = (double) (speed * MathHelper.cos(f8 * Const.DEG2RAD_FLOAT)) * Math.abs((double) f / d2);
            double d4 = (double) (speed * MathHelper.sin(f8 * Const.DEG2RAD_FLOAT)) * Math.abs((double) f2 / d2);
            double d5 = (double) (speed * MathHelper.sin(f7 * Const.DEG2RAD_FLOAT)) * Math.abs((double) f1 / d2);

            Vec3D motion = getMot();
            setMot(motion.e((new Vec3D(d3, d4, d5)).d(motion).a(0.2D)));
        }
    }

    public enum AttackPhase {
        CIRCLE, SWOOP
    }
}
