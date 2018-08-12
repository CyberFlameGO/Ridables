package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.ControllerMove;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityGhast;
import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.GenericAttributes;
import net.minecraft.server.v1_13_R1.SoundEffects;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASDFlying;
import net.pl3x.bukkit.ridables.entity.projectile.EntityGhastFireball;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class EntityRidableGhast extends EntityGhast implements RidableEntity {
    private ControllerMove aiController;
    private ControllerWASDFlying wasdController;
    private int spacebarCooldown = 0;

    public EntityRidableGhast(World world) {
        super(world);
        aiController = moveController;
        wasdController = new ControllerWASDFlying(this);
    }

    public boolean isActionableItem(ItemStack itemstack) {
        return false;
    }

    protected void mobTick() {
        if (spacebarCooldown > 0) {
            spacebarCooldown--;
        }

        EntityPlayer rider = getRider();
        if (rider != null) {
            setGoalTarget(null, null, false);
            setRotation(rider.yaw, rider.pitch);
            useWASDController();
        }
        super.mobTick();
    }

    public void setRotation(float newYaw, float newPitch) {
        setYawPitch(lastYaw = yaw = newYaw, pitch = newPitch * 0.5F);
        aS = aQ = yaw;
    }

    public float getJumpPower() {
        return 0;
    }

    public float getSpeed() {
        return (float) getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * Config.GHAST_SPEED;
    }

    public EntityPlayer getRider() {
        if (passengers != null && !passengers.isEmpty()) {
            Entity entity = passengers.get(0);
            if (entity instanceof EntityPlayer) {
                return (EntityPlayer) entity;
            }
        }
        return null;
    }

    public void useAIController() {
        if (moveController != aiController) {
            moveController = aiController;
        }
    }

    public void useWASDController() {
        if (moveController != wasdController) {
            moveController = wasdController;
        }
    }

    public boolean onSpacebar() {
        if (spacebarCooldown == 0) {
            EntityPlayer rider = getRider();
            if (rider == null) {
                return false;
            }
            return shoot(rider);
        }
        return false;
    }

    public boolean shoot(EntityPlayer rider) {
        spacebarCooldown = Config.GHAST_SHOOT_COOLDOWN;

        if (rider == null) {
            return false;
        }

        CraftPlayer player = rider.getBukkitEntity();
        if (!player.hasPermission("allow.shoot.ghast")) {
            Lang.send(player, Lang.SHOOT_NO_PERMISSION);
            return false;
        }

        Vector direction = rider.getBukkitEntity().getEyeLocation().getDirection()
                .normalize().multiply(25).add(new Vector(0, 2.5, 0)).normalize().multiply(25);

        a(SoundEffects.ENTITY_GHAST_WARN, 1.0F, 1.0F);

        new BukkitRunnable() {
            @Override
            public void run() {
                EntityGhastFireball fireball = new EntityGhastFireball(world, EntityRidableGhast.this,
                        rider, direction.getX(), direction.getY(), direction.getZ());
                world.addEntity(fireball);

                a(SoundEffects.ENTITY_GHAST_SHOOT, 1.0F, 1.0F);
            }
        }.runTaskLater(Ridables.getInstance(), 10);

        return true;
    }
}
