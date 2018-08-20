package net.pl3x.bukkit.ridables.data;

import net.pl3x.bukkit.ridables.util.ItemUtil;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an ItemStack in a player's hand
 */
public class HandItem {
    private final ItemStack itemStack;
    private final EquipmentSlot hand;

    public HandItem(ItemStack itemStack, EquipmentSlot hand) {
        this.itemStack = itemStack;
        this.hand = hand;
    }

    /**
     * Get the itemstack in hand
     *
     * @return Itemstack in hand
     */
    public ItemStack getItem() {
        return itemStack;
    }

    /**
     * Get the hand containing the itemstack
     *
     * @return Hand containing the itemstack
     */
    public EquipmentSlot getHand() {
        return hand;
    }

    /**
     * Subtract 1 from the itemstack's amount
     *
     * @return The itemstack in hand
     */
    public ItemStack subtract() {
        return ItemUtil.subtract(itemStack);
    }
}
