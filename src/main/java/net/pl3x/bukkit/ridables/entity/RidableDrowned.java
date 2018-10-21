package net.pl3x.bukkit.ridables.entity;

import io.papermc.lib.PaperLib;
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
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AIMoveTowardsRestriction;
import net.pl3x.bukkit.ridables.entity.ai.AIWander;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.zombie.AIZombieAttackTurtleEgg;
import net.pl3x.bukkit.ridables.entity.ai.zombie.AIZombieBreakDoor;
import net.pl3x.bukkit.ridables.entity.ai.zombie.drowned.AIDrownedAttack;
import net.pl3x.bukkit.ridables.entity.ai.zombie.drowned.AIDrownedGoToBeach;
import net.pl3x.bukkit.ridables.entity.ai.zombie.drowned.AIDrownedGoToWater;
import net.pl3x.bukkit.ridables.entity.ai.zombie.drowned.AIDrownedSwimUp;
import net.pl3x.bukkit.ridables.entity.ai.zombie.drowned.AIDrownedTridentAttack;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import net.pl3x.bukkit.ridables.entity.projectile.CustomThrownTrident;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
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

    public RidableType getType() {
        return RidableType.DROWNED;
    }

    protected void initAttributes() {
        super.initAttributes();
        getAttributeMap().b(RidableType.RIDE_SPEED);
        reloadAttributes();
    }

    public void reloadAttributes() {
        getAttributeInstance(RidableType.RIDE_SPEED).setValue(CONFIG.RIDE_SPEED);
        getAttributeInstance(GenericAttributes.maxHealth).setValue(CONFIG.MAX_HEALTH);
        getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(CONFIG.BASE_SPEED);
        getAttributeInstance(GenericAttributes.ATTACK_DAMAGE).setValue(CONFIG.AI_ATTACK_DAMAGE);
        getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(CONFIG.AI_FOLLOW_RANGE);
    }

    // initAI - override vanilla AI
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
        if (PaperLib.isSpigot() && world.spigotConfig.zombieAggressiveTowardsVillager) {
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
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return getRider() == null ? super.cG() : CONFIG.JUMP_POWER;
    }

    // setBreakDoorsAITask
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

    protected void mobTick() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
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

    public boolean onClick(org.bukkit.entity.Entity entity, EnumHand hand) {
        return handleClick();
    }

    public boolean onClick(Block block, BlockFace blockFace, EnumHand hand) {
        return handleClick();
    }

    public boolean onClick(EnumHand hand) {
        return handleClick();
    }

    private boolean handleClick() {
        if (shootCooldown == 0) {
            EntityPlayer rider = getRider();
            if (rider != null) {
                if (!CONFIG.SHOOT_REQUIRE_TRIDENT || hasTrident()) {
                    return throwTrident(rider);
                }
            }
        }
        return false;
    }

    public boolean throwTrident(EntityPlayer rider) {
        shootCooldown = CONFIG.SHOOT_COOLDOWN;

        if (rider == null) {
            return false;
        }

        CraftPlayer player = (CraftPlayer) ((Entity) rider).getBukkitEntity();
        if (!hasShootPerm(player)) {
            Lang.send(player, Lang.SHOOT_NO_PERMISSION);
            return false;
        }

        Vector direction = player.getEyeLocation().getDirection().normalize().multiply(25).add(new Vector(0, 3, 0));

        CustomThrownTrident trident = new CustomThrownTrident(this.world, this, rider, new ItemStack(Items.TRIDENT));
        trident.shoot(direction.getX(), direction.getY(), direction.getZ(), 1.6F * CONFIG.SHOOT_SPEED, 0);
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
                drowned.yaw = a(drowned.yaw, (float) (MathHelper.c(z, x) * (double) (180F / (float) Math.PI)) - 90.0F, 90.0F);
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
