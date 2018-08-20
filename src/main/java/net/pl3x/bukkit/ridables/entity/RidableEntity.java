package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.EntityPlayer;
import net.minecraft.server.v1_13_R1.EnumHand;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

public interface RidableEntity {
    /**
     * Get the RidableType of this entity
     *
     * @return RidableType
     */
    RidableType getType();

    /**
     * Check if this item is an actionable item for this entity
     * <p>
     * An actionable item is something that makes the entity do something (eat, breed, tempt, etc)
     *
     * @param itemstack Itemstack to check
     * @return True if itemstack is actionable
     */
    default boolean isActionableItem(ItemStack itemstack) {
        return false;
    }

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
