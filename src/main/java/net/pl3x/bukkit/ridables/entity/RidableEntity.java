package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.ItemStack;
import net.minecraft.server.v1_13_R2.Items;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.entity.ai.controller.ControllerWASD;
import net.pl3x.bukkit.ridables.event.RidableMountEvent;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

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
     * Get the rider of this entity
     * <p>
     * Only the first passenger (index 0) is considered the rider in this context
     *
     * @return Current rider, otherwise null
     */
    default EntityPlayer getRider() {
        return ((ControllerWASD) ((EntityInsentient) this).getControllerMove()).rider;
    }

    default boolean tryRide(EntityHuman entityhuman, boolean requireSaddle, boolean consumeSaddle) {
        Player player = (Player) entityhuman.getBukkitEntity();
        ItemStack itemstack = entityhuman.b(EnumHand.MAIN_HAND);
        if (requireSaddle && (itemstack == null || itemstack.getItem() != Items.SADDLE)) {
            Lang.send(player, Lang.RIDE_REQUIRES_SADDLE);
            return false; // not handled - saddle is required
        }
        if (!player.hasPermission("allow.ride." + getType().getName())) {
            Lang.send(player, Lang.RIDE_NO_PERMISSION);
            return true; // handled
        }
        if (!new RidableMountEvent(this, player).callEvent()) {
            return true; // handled
        }
        if (requireSaddle && consumeSaddle) {
            itemstack.subtract(1);
        }
        EntityInsentient entity = (EntityInsentient) this;
        entityhuman.yaw = entity.yaw;
        entityhuman.pitch = entity.pitch;
        entityhuman.startRiding(entity);
        entityhuman.o(false); // setJumping - fixes jump on mount
        return true; // handled
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

    default boolean onClick() {
        return false;
    }
}
