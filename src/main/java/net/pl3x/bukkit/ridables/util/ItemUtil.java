package net.pl3x.bukkit.ridables.util;

import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EnumHand;
import net.minecraft.server.v1_13_R2.Items;
import net.pl3x.bukkit.ridables.data.HandItem;
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

    /**
     * Checks if a player is not holding any item at all, or holding a saddle in either hand
     *
     * @param player Player ot check
     * @return True if holding a saddle or nothing at all
     */
    public static boolean isEmptyOrSaddle(EntityHuman player) {
        net.minecraft.server.v1_13_R2.ItemStack stack = player.b(EnumHand.MAIN_HAND);
        if (stack.isEmpty()) {
            stack = player.b(EnumHand.OFF_HAND);
            if (stack.isEmpty()) {
                return true;
            }
            return stack.getItem() == Items.SADDLE;
        }
        return stack.getItem() == Items.SADDLE;
    }

    /**
     * Checks if a player is not holding any item at all
     *
     * @param player Player ot check
     * @return True if not holding any items
     */
    public static boolean isEmpty(EntityHuman player) {
        return player.b(EnumHand.MAIN_HAND).isEmpty() && player.b(EnumHand.OFF_HAND).isEmpty();
    }
}
