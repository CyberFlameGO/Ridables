package net.pl3x.bukkit.ridables.data;

import com.google.common.collect.Maps;
import net.pl3x.bukkit.ridables.configuration.Lang;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.stream.Collectors;

public class Bucket {
    private final static Map<EntityType, Bucket> BY_ENTITY_TYPE = Maps.newHashMap();

    public static final Bucket DOLPHIN_BUCKET = createBucket(EntityType.DOLPHIN);
    public static final Bucket TURTLE_BUCKET = createBucket(EntityType.TURTLE);

    public static Bucket getBucket(ItemStack itemStack) {
        for (Bucket bucket : BY_ENTITY_TYPE.values()) {
            if (bucket.getItemStack().isSimilar(itemStack)) {
                return bucket;
            }
        }
        return null;
    }

    public static Bucket getBucket(EntityType entityType) {
        return BY_ENTITY_TYPE.get(entityType);
    }

    private static Bucket createBucket(EntityType entityType) {
        String entityName = WordUtils.capitalizeFully(entityType.name().replace("_", " "));
        ItemStack itemStack = new ItemStack(Material.COD_BUCKET, 1);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', Lang.WATER_BUCKET_NAME
                .replace("{creature}", entityName)));
        meta.setLore(Lang.WATER_BUCKET_LORE.stream()
                .map(lore -> lore.replace("{creature}", entityName))
                .collect(Collectors.toList()));
        meta.setUnbreakable(true);
        meta.addEnchant(Enchantment.DURABILITY, 10, true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        Bucket bucket = new Bucket(itemStack, entityType);
        BY_ENTITY_TYPE.put(entityType, bucket);
        return bucket;
    }

    private final ItemStack itemStack;
    private final EntityType entityType;

    private Bucket(ItemStack itemStack, EntityType entityType) {
        this.itemStack = itemStack;
        this.entityType = entityType;
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
