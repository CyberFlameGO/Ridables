package net.pl3x.bukkit.ridables.util;

import net.pl3x.bukkit.ridables.HandItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {
    /**
     * Get the item in a player's hand
     *
     * @param player The player to check
     * @param hand   The hand to check
     * @return Itemstack that is in player's hand
     */
    public static ItemStack getItem(Player player, EquipmentSlot hand) {
        return hand == EquipmentSlot.OFF_HAND ?
                player.getInventory().getItemInOffHand() :
                player.getInventory().getItemInMainHand();
    }

    /**
     * Set an item to a player's hand
     *
     * @param player    Player to set item to
     * @param itemStack Itemstack to set
     * @param hand      Hand to set itemstack in
     */
    public static void setItem(Player player, ItemStack itemStack, EquipmentSlot hand) {
        if (hand == EquipmentSlot.HAND) {
            player.getInventory().setItemInMainHand(itemStack);
        } else if (hand == EquipmentSlot.OFF_HAND) {
            player.getInventory().setItemInOffHand(itemStack);
        } else {
            throw new IllegalArgumentException("Slot is not a hand slot: " + hand);
        }
    }

    /**
     * Subtract 1 from the amount of an itemstack
     *
     * @param stack The itemstack to subtract from
     * @return The itemstack
     */
    public static ItemStack subtract(ItemStack stack) {
        return subtract(stack, 1);
    }

    /**
     * Subtract from the amount of an itemstack
     *
     * @param stack  The itemstack to subtract from
     * @param amount The amount to subtract
     * @return The itemstack
     */
    public static ItemStack subtract(ItemStack stack, int amount) {
        stack.setAmount(Math.max(0, stack.getAmount() - amount));
        return stack;
    }

    /**
     * Get a specific item in a player's hands
     * <p>
     * This will first check the main hand and then the off hand
     *
     * @param player   Player to check
     * @param material Material to check for
     * @return HandItem for the itemstack and which hand it is in
     */
    public static HandItem getItem(Player player, Material material) {
        ItemStack item = getItem(player, EquipmentSlot.HAND);
        if (item.getType() == material) {
            return new HandItem(item, EquipmentSlot.HAND);
        }
        item = getItem(player, EquipmentSlot.OFF_HAND);
        if (item.getType() == material) {
            return new HandItem(item, EquipmentSlot.OFF_HAND);
        }
        return null;
    }
}
