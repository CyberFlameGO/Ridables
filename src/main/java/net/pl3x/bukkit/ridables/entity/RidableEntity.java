package net.pl3x.bukkit.ridables.entity;

import net.minecraft.server.v1_13_R1.EntityPlayer;
import org.bukkit.inventory.ItemStack;

public interface RidableEntity {
    /**
     * Check if this item is an actionable item for this entity
     * <p>
     * An actionable item is something that makes the entity do something (eat, breed, tempt, etc)
     *
     * @param itemstack Itemstack to check
     * @return True if itemstack is actionable
     */
    boolean isActionableItem(ItemStack itemstack);

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
    float getJumpPower();

    /**
     * Get the configured speed for this entity
     *
     * @return Speed
     */
    float getSpeed();

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
     */
    void onSpacebar();
}
