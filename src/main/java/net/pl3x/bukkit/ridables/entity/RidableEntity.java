package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.CriterionTriggers;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTameableAnimal;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.minecraft.server.v1_13_R2.SoundEffects;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.event.RidableDismountEvent;
import net.pl3x.bukkit.ridables.event.RidableMountEvent;
import net.pl3x.bukkit.ridables.util.Logger;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface RidableEntity {
    /**
     * Get the RidableType of this entity
     *
     * @return RidableType
     */
    RidableType getType();

    /**
     * Reload the mob's attributes
     */
    void reloadAttributes();

    /**
     * Get the Bukkit entity
     *
     * @return Bukkit entity
     */
    default Entity getBukkitEntity() {
        return ((EntityInsentient) this).getBukkitEntity();
    }

    /**
     * Get the rider of this entity
     * <p>
     * Only the first passenger (index 0) is considered the rider in this context
     *
     * @return Current rider, otherwise null
     */
    default EntityPlayer getRider() {
        return ((ControllerWASD) ((EntityInsentient) this).getControllerMove()).rider;
    }

    default boolean processInteract(EntityHuman entityhuman, EnumHand enumhand) {
        EntityInsentient entity = (EntityInsentient) this;
        ItemStack itemstack = entityhuman.b(enumhand);
        if (itemstack.getItem() == Items.WATER_BUCKET) {
            if (getType().getWaterBucket() != null && entity.isAlive()) {
                if (!hasCollectPerm((Player) entityhuman.getBukkitEntity())) {
                    if (enumhand == EnumHand.MAIN_HAND) {
                        entityhuman.getBukkitEntity().sendMessage(Lang.COLLECT_NO_PERMISSION);
                    }
                    return true; // handled
                }
                ItemStack bucket = CraftItemStack.asNMSCopy(getType().getWaterBucket().getItemStack());
                entity.a(SoundEffects.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
                itemstack.subtract(1);
                // TODO set custom name
                CriterionTriggers.j.a((EntityPlayer) entityhuman, bucket); // filled_bucket achievement
                if (itemstack.isEmpty()) {
                    entityhuman.a(enumhand, bucket);
                } else if (!entityhuman.inventory.pickup(bucket)) {
                    entityhuman.drop(bucket, false);
                }
                entity.die();
                return true; // handled
            }
        } else if (enumhand == EnumHand.MAIN_HAND && !entityhuman.isSneaking() && entity.passengers.isEmpty() && !entityhuman.isPassenger()) {
            return tryRide(entityhuman, itemstack);
        }
        return false;
    }

    /**
     * Tries to let player mount this creature
     *
     * @param entityhuman Player trying to mount
     * @param itemStack   Item in player's hand
     * @return True if mount was successful
     */
    default boolean tryRide(EntityHuman entityhuman, ItemStack itemStack) {
        Player player = (Player) entityhuman.getBukkitEntity();
        if (this instanceof EntityAgeable) {
            if (!Config.ALLOW_RIDE_BABIES && ((EntityAgeable) this).isBaby()) {
                return false; // do not ride babies
            }
        }
        if (this instanceof EntityTameableAnimal) {
            UUID owner = ((EntityTameableAnimal) this).getOwnerUUID();
            if (owner == null || !player.getUniqueId().equals(owner)) {
                return false; // player doesnt own this creature
            }
        }
        if (!hasRidePerm(player)) {
            Lang.send(player, Lang.RIDE_NO_PERMISSION);
            return true;
        }
        if (Config.REQUIRE_SADDLE) {
            if (itemStack == null || itemStack.isEmpty()) {
                return false; // saddle is required
            }
            if (Config.CONSUME_SADDLE) {
                itemStack.subtract(1);
            }
        }
        RidableMountEvent mountEvent = new RidableMountEvent(this, player);
        Bukkit.getPluginManager().callEvent(mountEvent);
        if (mountEvent.isCancelled()) {
            return false;
        }
        boolean mounted = entityhuman.startRiding((EntityInsentient) this);
        entityhuman.o(false); // setJumping
        return mounted;
    }

    /**
     * Calls the dismount event
     *
     * @param passenger Passenger dismounting
     * @return True if dismount event was not cancelled
     */
    default boolean dismountPassenger(Entity passenger) {
        if (passenger instanceof Player) {
            RidableDismountEvent dismountEvent = new RidableDismountEvent(this, (Player) passenger);
            Bukkit.getPluginManager().callEvent(dismountEvent);
            return !dismountEvent.isCancelled();
        }
        return true;
    }

    /**
     * Check if player has permission to collect this creature in a water bucket
     *
     * @param player Player to check
     * @return True if player had permission
     */
    default boolean hasCollectPerm(Player player) {
        boolean hasPerm = player.hasPermission("allow.collect." + getType().getName());
        if (!hasPerm) {
            Logger.debug("Perm Check: " + player.getName() + " does NOT have permission to collect: " + getType().getName());
        }
        return hasPerm;
    }

    /**
     * Check if player has permission to mount this creature
     *
     * @param player Player to check
     * @return True if player had permission
     */
    default boolean hasRidePerm(Player player) {
        boolean hasPerm = player.hasPermission("allow.ride." + getType().getName());
        if (!hasPerm) {
            Logger.debug("Perm Check: " + player.getName() + " does NOT have permission to ride: " + getType().getName());
        }
        return hasPerm;
    }

    /**
     * Check if player has permission to make this creature shoot projectiles
     *
     * @param player Player to check
     * @return True if player had permission
     */
    default boolean hasShootPerm(Player player) {
        boolean hasPerm = player.hasPermission("allow.shoot." + getType().getName());
        if (!hasPerm) {
            Logger.debug("Perm Check: " + player.getName() + " does NOT have permission to shoot: " + getType().getName());
        }
        return hasPerm;
    }

    /**
     * Check if player has permission to make this creature perform special actions
     *
     * @param player Player to check
     * @return True if player had permission
     */
    default boolean hasSpecialPerm(Player player) {
        boolean hasPerm = player.hasPermission("allow.special." + getType().getName());
        if (!hasPerm) {
            Logger.debug("Perm Check: " + player.getName() + " does NOT have permission to use special: " + getType().getName());
        }
        return hasPerm;
    }

    /**
     * This method is called when the spacebar is pressed by the current rider
     * <p>
     * This is used internally for triggering spacebar events other than jumping. It is advised to not use this method
     *
     * @return True if spacebar was handled
     */
    default boolean onSpacebar() {
        return false;
    }

    /**
     * This method is called when the current rider clicks on an entity
     * <p>
     * This is used internally for triggering click events on the creature. It is advised to not use this method
     *
     * @param entity The Entity clicked on
     * @param hand   Hand used to click
     * @return True if click was handled
     */
    default boolean onClick(Entity entity, EnumHand hand) {
        return false;
    }

    /**
     * This method is called when the current rider clicks on a block
     * <p>
     * This is used internally for triggering click events on the creature. It is advised to not use this method
     *
     * @param block     The Block clicked on
     * @param blockFace Face of black clicked
     * @param hand      Hand used to click
     * @return True if click was handled
     */
    default boolean onClick(Block block, BlockFace blockFace, EnumHand hand) {
        return false;
    }

    /**
     * This method is called when the current rider clicks in the air
     * <p>
     * This is used internally for triggering click events on the creature. It is advised to not use this method
     *
     * @param hand Hand used to click
     * @return True if click was handled
     */
    default boolean onClick(EnumHand hand) {
        return false;
    }
}
