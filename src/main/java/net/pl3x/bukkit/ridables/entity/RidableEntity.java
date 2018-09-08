package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EntityTameableAnimal;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.data.HandItem;
import net.pl3x.bukkit.ridables.entity.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.listener.RideListener;
import net.pl3x.bukkit.ridables.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
     * Set the rotation of the entity
     * <p>
     * This is used internally for keeping the entity at the same rotation as the player. It is advised to not use this method
     *
     * @param yaw   Yaw to set
     * @param pitch Pitch to set
     */
    void setRotation(float yaw, float pitch);

    /**
     * Get the configured jump power of this entity
     *
     * @return Jump power
     */
    default float getJumpPower() {
        return 0;
    }

    /**
     * Get the configured speed for this entity
     *
     * @return Speed
     */
    default float getSpeed() {
        return 0;
    }

    /**
     * Get the rider of this entity
     * <p>
     * Only the first passenger (index 0) is considered the rider in this context
     *
     * @return Current rider, otherwise null
     */
    EntityPlayer getRider();

    /**
     * Update and get the rider of this entity
     * <p>
     * This method should never be called directly. It is updated once per tick already.
     *
     * @return Current rider, otherwise null
     */
    EntityPlayer updateRider();

    default boolean tryRide(EntityHuman entityhuman) {
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
        if (!player.hasPermission("allow.ride." + ((EntityInsentient)this).getBukkitEntity().getType().name().toLowerCase())) {
            Lang.send(player, Lang.RIDE_NO_PERMISSION);
            return true;
        }
        if (Config.REQUIRE_SADDLE) {
            HandItem saddle = ItemUtil.getItem(player, Material.SADDLE);
            if (saddle == null) {
                return false; // saddle is required
            }
            if (Config.CONSUME_SADDLE) {
                ItemUtil.setItem(player, saddle.subtract(), saddle.getHand());
            }
        }
        RideListener.override.add(player.getUniqueId());
        boolean mounted = entityhuman.a((EntityInsentient) this, true);
        RideListener.override.remove(player.getUniqueId());
        ControllerWASD.setJumping(entityhuman);
        return mounted;
    }

    /**
     * Change to the vanilla AI controller
     */
    void useAIController();

    /**
     * Change to the WASD custom controller
     */
    void useWASDController();

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
