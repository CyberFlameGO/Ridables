package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityPotion;
import net.minecraft.server.v1_13_R2.EntityWitch;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.PotionUtil;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.configuration.mob.WitchConfig;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackNearest;
import net.pl3x.bukkit.ridables.entity.ai.AIAttackRanged;
import net.pl3x.bukkit.ridables.entity.ai.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.AISwim;
import net.pl3x.bukkit.ridables.entity.ai.AIWanderAvoidWater;
import net.pl3x.bukkit.ridables.entity.ai.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.controller.LookController;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.util.Vector;

public class RidableWitch extends EntityWitch implements RidableEntity {
    public static final WitchConfig CONFIG = new WitchConfig();

    private int shootCooldown = 0;

    public RidableWitch(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new LookController(this);
    }

    public RidableType getType() {
        return RidableType.WITCH;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(1, new AISwim(this));
        goalSelector.a(2, new AIAttackRanged(this, 1.0D, 60, 10.0F));
        goalSelector.a(2, new AIWanderAvoidWater(this, 1.0D));
        goalSelector.a(3, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(3, new AILookIdle(this));
        targetSelector.a(1, new AIHurtByTarget(this, false));
        targetSelector.a(2, new AIAttackNearest<>(this, EntityHuman.class, true));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return CONFIG.RIDABLE_IN_WATER;
    }

    // getJumpUpwardsMotion
    protected float cG() {
        return CONFIG.JUMP_POWER;
    }

    protected void mobTick() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        Q = CONFIG.STEP_HEIGHT;
        super.mobTick();
    }

    public float getSpeed() {
        return CONFIG.SPEED;
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
                return throwPotion(rider);
            }
        }
        return false;
    }

    public boolean throwPotion(EntityPlayer rider) {
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

        EntityPotion entitypotion = new EntityPotion(world, this, PotionUtil.a(new ItemStack(Items.SPLASH_POTION), CONFIG.SHOOT_POTION_TYPE));
        entitypotion.pitch -= -20.0F;
        entitypotion.shoot(direction.getX(), direction.getY() + 10, direction.getZ(), 0.75F * CONFIG.SHOOT_SPEED, 0);
        world.addEntity(entitypotion);

        world.a(null, locX, locY, locZ, SoundEffects.ENTITY_WITCH_THROW, bV(), 1.0F, 0.8F + random.nextFloat() * 0.4F);

        return true;
    }

    // processInteract
    public boolean a(EntityHuman player, EnumHand hand) {
        return super.a(player, hand) || processInteract(player, hand);
    }

    // removePassenger
    public boolean removePassenger(Entity passenger) {
        return dismountPassenger(passenger.getBukkitEntity()) && super.removePassenger(passenger);
    }
}
