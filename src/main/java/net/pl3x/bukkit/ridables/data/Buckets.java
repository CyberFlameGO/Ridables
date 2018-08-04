package net.pl3x.bukkit.ridables.data;

import net.pl3x.bukkit.ridables.configuration.Lang;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.stream.Collectors;

public class Buckets extends HashMap<EntityType, ItemStack> {
    public ItemStack getBucket(EntityType entityType) {
        return get(entityType);
    }

    public EntityType getEntityType(ItemStack bucket) {
        if (bucket != null && bucket.getType() == Material.COD_BUCKET) {
            for (Entry<EntityType, ItemStack> entry : entrySet()) {
                if (entry.getValue().isSimilar(bucket)) {
                    return entry.getKey();
                }
            }
        }
        return null;
    }

    public void createBucket(EntityType entityType) {
        String entityName = WordUtils.capitalizeFully(entityType.name().replace("_", " "));
        ItemStack bucket = new ItemStack(Material.COD_BUCKET, 1);
        ItemMeta meta = bucket.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Lang.WATER_BUCKET_NAME
                .replace("{creature}", entityName)));
        meta.setLore(Lang.WATER_BUCKET_LORE.stream()
                .map(lore -> lore.replace("{creature}", entityName))
                .collect(Collectors.toList()));
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.DURABILITY, 10, true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        bucket.setItemMeta(meta);
        put(entityType, bucket);
    }
}
