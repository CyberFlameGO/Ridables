package net.pl3x.bukkit.ridables.entity.monster;

import net.minecraft.server.v1_13_R2.ControllerLook;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityShulker;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.configuration.mob.ShulkerConfig;
import net.pl3x.bukkit.ridables.entity.RidableEntity;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIHurtByTarget;
import net.pl3x.bukkit.ridables.entity.ai.goal.AILookIdle;
import net.pl3x.bukkit.ridables.entity.ai.goal.AIWatchClosest;
import net.pl3x.bukkit.ridables.entity.ai.goal.shulker.AIShulkerAttack;
import net.pl3x.bukkit.ridables.entity.ai.goal.shulker.AIShulkerAttackPlayer;
import net.pl3x.bukkit.ridables.entity.ai.goal.shulker.AIShulkerDefenseAttack;
import net.pl3x.bukkit.ridables.entity.ai.goal.shulker.AIShulkerPeek;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.entity.projectile.CustomShulkerBullet;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.util.Vector;

public class RidableShulker extends EntityShulker implements RidableEntity {
    public static final ShulkerConfig CONFIG = new ShulkerConfig();

    private boolean isOpen = true;
    private int shootCooldown = 0;
    private int spacebarCooldown = 0;

    public RidableShulker(World world) {
        super(world);
        moveController = new ControllerWASD(this);
        lookController = new ControllerLook(this);
    }

    public RidableType getType() {
        return RidableType.SHULKER;
    }

    // initAI - override vanilla AI
    protected void n() {
        goalSelector.a(1, new AIWatchClosest(this, EntityHuman.class, 8.0F));
        goalSelector.a(4, new AIShulkerAttack(this));
        goalSelector.a(7, new AIShulkerPeek(this));
        goalSelector.a(8, new AILookIdle(this));
        targetSelector.a(1, new AIHurtByTarget(this, true));
        targetSelector.a(2, new AIShulkerAttackPlayer(this));
        targetSelector.a(3, new AIShulkerDefenseAttack(this));
    }

    // canBeRiddenInWater
    public boolean aY() {
        return false;
    }

    // tryTeleportToNewPosition
    protected boolean l() {
        return getRider() != null || super.l();
    }

    protected void mobTick() {
        if (spacebarCooldown > 0) {
            spacebarCooldown--;
        }
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        if (getRider() != null) {
            updatePeek();
        }
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

    public boolean onSpacebar() {
        if (spacebarCooldown == 0) {
            spacebarCooldown = 20;
            setOpen(!isOpen());
            return true;
        }
        return false;
    }

    public boolean onClick(org.bukkit.entity.Entity entity, EnumHand hand) {
        handleClick();
        return true;
    }

    public boolean onClick(Block block, BlockFace blockFace, EnumHand hand) {
        handleClick();
        return true;
    }

    public boolean onClick(EnumHand hand) {
        handleClick();
        return true;
    }

    private void handleClick() {
        if (shootCooldown == 0 && isOpen()) {
            EntityPlayer rider = getRider();
            if (rider != null) {
                shoot(rider);
            }
        }
    }

    private void updatePeek() {
        byte peekTick = (byte) (isOpen ? CONFIG.PEEK_HEIGHT : 0);
        if (dA() != peekTick) {
            a(peekTick);
        }
    }

    public void setOpen(boolean open) {
        this.isOpen = open;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean shoot(EntityPlayer rider) {
        shootCooldown = CONFIG.SHOOT_COOLDOWN;

        if (rider == null) {
            return false;
        }

        CraftPlayer player = (CraftPlayer) ((Entity) rider).getBukkitEntity();
        if (!hasShootPerm(player)) {
            Lang.send(player, Lang.SHOOT_NO_PERMISSION);
            return false;
        }

        Vector target = player.getEyeLocation().getDirection().normalize().multiply(25);

        CustomShulkerBullet bullet = new CustomShulkerBullet(world, this, rider, null, dy().k());
        bullet.shoot(target.getX(), target.getY(), target.getZ(), CONFIG.SHOOT_SPEED, 5.0F);
        world.addEntity(bullet);

        a(SoundEffects.ENTITY_SHULKER_SHOOT, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F);
        return true;
    }
}