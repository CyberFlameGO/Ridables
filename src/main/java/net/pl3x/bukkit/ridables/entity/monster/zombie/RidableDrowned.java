package net.pl3x.bukkit.ridables.entity.monster.zombie;

import net.minecraft.server.v1_13_R2.Blocks;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityDrowned;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityIronGolem;
import net.minecraft.server.v1_13_R2.EntityLiving;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTurtle;
import net.minecraft.server.v1_13_R2.EntityVillager;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.EnumItemSlot;
import net.minecraft.server.v1_13_R2.GenericAttributes;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.MathHelper;
import net.minecraft.server.v1_13_R2.Navigation;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.configuration.mob.DrownedConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.ai.controller.LookController;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWander;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.AIZombieAttackTurtleEgg;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.AIZombieBreakDoor;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.drowned.AIDrownedAttack;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.drowned.AIDrownedGoToBeach;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.drowned.AIDrownedGoToWater;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.drowned.AIDrownedSwimUp;
import net.pl3x.bukkit.ridables.entity.ai.goal.zombie.drowned.AIDrownedTridentAttack;
import net.pl3x.bukkit.ridables.entity.projectile.CustomThrownTrident;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import net.pl3x.bukkit.ridables.util.Const;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class RidableDrowned extends EntityDrowned implements RidableEntity {
    public static final DrownedConfig CONFIG = new DrownedConfig();

    private final AIZombieBreakDoor breakDoorAI;
    private boolean swimUp;
    private int shootCooldown = 0;

    public RidableDrowned(World world) {
        super(world);
        moveController = new DrownedWASDController(this);
        lookController = new LookController(this);
        breakDoorAI = new AIZombieBreakDoor(this);
    }

    @Override
    public RidableType getType() {
        return RidableType.DROWNED;
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
        getAttributeInstance(GenericAttributes.h).setValue(CONFIG.AI_ARMOR); // ARMOR
        getAttributeInstance(c).setValue(random.nextDouble() * CONFIG.AI_SPAWN_REINFORCEMENTS_CHANCE); // SPAWN_REINFORCEMENTS_CHANCE
    }

    // initAI - override vanilla AI
    @Override
    protected void n() {
        // from EntityZombie
        goalSelector.a(4, new AIZombieAttackTurtleEgg(Blocks.TURTLE_EGG, this, 1.0D, 3));
        goalSelector.a(5, new AIMoveTowardsRestriction(this, 1.0D));
        goalSelector.a(8, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(8, new AILookIdle(this));

        // from EntityDrowned
        goalSelector.a(1, new AIDrownedGoToWater(this, 1.0D));
        goalSelector.a(2, new AIDrownedTridentAttack(this, 1.0D, 40, 10.0F));
        goalSelector.a(2, new AIDrownedAttack(this, 1.0D, false));
        goalSelector.a(5, new AIDrownedGoToBeach(this, 1.0D));
        goalSelector.a(6, new AIDrownedSwimUp(this, 1.0D, this.world.getSeaLevel()));
        goalSelector.a(7, new AIWander(this, 1.0D));
        targetSelector.a(1, new AIHurtByTarget(this, true, EntityDrowned.class));
        targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, 10, true, false, e -> e != null && (!e.world.L() || e.isInWater())));
        if (world.spigotConfig.zombieAggressiveTowardsVillager) {
            targetSelector.a(3, new AIAttackNearest<>(this, EntityVillager.class, false));
        }
        targetSelector.a(3, new AIAttackNearest<>(this, EntityIronGolem.class, true));
        targetSelector.a(5, new AIAttackNearest<>(this, EntityTurtle.class, 10, true, false, EntityTurtle.bC));
    }

    public void setGroundNavigation() {
        navigation = b;
    }

    public void setWaterNavigation() {
        navigation = a;
    }

    public boolean isCloseToPathTarget() {
        return dD();
    }

    public void setSwimmingUp(boolean swimUp) {
        super.a(swimUp);
        this.swimUp = swimUp;
    }

    public boolean isSwimmingUp(boolean checkTarget) {
        if (checkTarget) {
            if (swimUp) {
                return true;
            }
            EntityLiving target = getGoalTarget();
            return target != null && target.isInWater();
        }
        return swimUp;
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

    // setBreakDoorsAITask
    @Override
    public void t(boolean enabled) {
        if (dz()) { // canBreakDoors
            if (dH() != enabled) {
                RidableZombie.setBreakDoorsTask(this, enabled);
                ((Navigation) this.getNavigation()).a(enabled); // setBreakDoors
                if (enabled) {
                    goalSelector.a(1, breakDoorAI); // addTask
                } else {
                    goalSelector.a(breakDoorAI); // removeTask
                }
            }
        } else if (dH()) {
            goalSelector.a(breakDoorAI); // removeTask
            RidableZombie.setBreakDoorsTask(this, false);
        }
    }

    @Override
    protected void mobTick() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
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
            if (!CONFIG.RIDING_BABIES && isBaby()) {
                return false; // do not ride babies
            }
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
                if (!CONFIG.RIDING_SHOOT_REQUIRE_TRIDENT || hasTrident()) {
                    return throwTrident(rider);
                }
            }
        }
        return false;
    }

    public boolean throwTrident(EntityPlayer rider) {
        shootCooldown = CONFIG.RIDING_SHOOT_COOLDOWN;

        if (rider == null) {
            return false;
        }

        CraftPlayer player = (CraftPlayer) ((Entity) rider).getBukkitEntity();
        if (!player.hasPermission("allow.shoot.drowned")) {
            Lang.send(player, Lang.SHOOT_NO_PERMISSION);
            return false;
        }

        Vector direction = player.getEyeLocation().getDirection().normalize().multiply(25).add(new Vector(0, 3, 0));

        CustomThrownTrident trident = new CustomThrownTrident(this.world, this, rider, new ItemStack(Items.TRIDENT));
        trident.shoot(direction.getX(), direction.getY(), direction.getZ(), (float) (1.6D * CONFIG.RIDING_SHOOT_SPEED), 0);
        world.addEntity(trident);

        a(SoundEffects.ENTITY_DROWNED_SHOOT, 1.0F, 1.0F);

        return true;
    }

    /**
     * Check if this drowned has a trident in it's hands
     *
     * @return True if trident in either hand
     */
    public boolean hasTrident() {
        return getEquipment(EnumItemSlot.MAINHAND).getItem() == Items.TRIDENT ||
                getEquipment(EnumItemSlot.OFFHAND).getItem() == Items.TRIDENT;
    }

    static class DrownedWASDController extends ControllerWASD {
        private final RidableDrowned drowned;

        public DrownedWASDController(RidableDrowned drowned) {
            super(drowned);
            this.drowned = drowned;
        }

        @Override
        public void tick() {
            EntityLiving target = drowned.getGoalTarget();
            if (drowned.isSwimmingUp(true) && drowned.isInWater()) {
                if (target != null && target.locY > drowned.locY || drowned.isSwimmingUp(false)) {
                    drowned.motY += 0.002D;
                }
                if (h != Operation.MOVE_TO || drowned.getNavigation().p()) {
                    drowned.o(0.0F);
                    return;
                }
                double x = b - drowned.locX;
                double y = c - drowned.locY;
                double z = d - drowned.locZ;
                y /= (double) MathHelper.sqrt(x * x + y * y + z * z);
                drowned.yaw = a(drowned.yaw, (float) (MathHelper.c(z, x) * Const.RAD2DEG) - 90.0F, 90.0F);
                drowned.aQ = drowned.yaw;
                float speed = (float) (e * drowned.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue());
                drowned.o(drowned.cK() + (speed - drowned.cK()) * 0.125F);
                drowned.motY += (double) drowned.cK() * y * 0.1D;
                drowned.motX += (double) drowned.cK() * x * 0.005D;
                drowned.motZ += (double) drowned.cK() * z * 0.005D;
            } else {
                if (!drowned.onGround) {
                    drowned.motY -= 0.008D;
                }
                super_tick();
            }
        }
    }
}
