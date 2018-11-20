package net.pl3x.bukkit.ridables.data;

import com.google.common.collect.Sets;
import net.pl3x.bukkit.ridables.configuration.Lang;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Represents a collection bucket of a water entity. This bucket contains a specific entity that can be placed into the world.
 */
public class Bucket {
    /**
     * All the loaded buckets
     */
    public final static Collection<Bucket> BUCKETS = Sets.newHashSet();

    /**
     * The dolphin bucket
     */
    public static final Bucket DOLPHIN = createBucket(EntityType.DOLPHIN);

    /**
     * The elder guardian bucket
     */
    public static final Bucket ELDER_GUARDIAN = createBucket(EntityType.ELDER_GUARDIAN);

    /**
     * The guardian bucket
     */
    public static final Bucket GUARDIAN = createBucket(EntityType.GUARDIAN);

    /**
     * The squid bucket
     */
    public static final Bucket SQUID = createBucket(EntityType.SQUID);

    /**
     * The turtle bucket
     */
    public static final Bucket TURTLE = createBucket(EntityType.TURTLE);

    /**
     * Get a Bucket object of a specific itemstack if it is one of the loaded buckets
     *
     * @param itemStack Itemstack to check
     * @return Bucket of the itemstack, otherwise null
     */
    public static Bucket getBucket(ItemStack itemStack) {
        for (Bucket bucket : BUCKETS) {
            if (bucket.getItemStack().isSimilar(itemStack)) {
                return bucket;
            }
        }
        return null;
    }

    public static boolean isFromBucket(Entity entity) {
        if (entity.getType() != EntityType.COD) {
            return false; // not a cod
        }

        String name = entity.getCustomName();
        if (name == null) {
            return false; // no custom name
        }

        for (Bucket bucket : BUCKETS) {
            ItemStack stack = bucket.getItemStack();
            if (!stack.hasItemMeta()) {
                continue; // no meta
            }
            ItemMeta meta = stack.getItemMeta();
            if (!meta.hasDisplayName()) {
                continue; // no custom name
            }
            if (name.equals(meta.getDisplayName())) {
                return true; // found a matching name
            }
        }

        return false; // not from a bucket
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
        BUCKETS.add(bucket);
        return bucket;
    }

    private final ItemStack itemStack;
    private final EntityType entityType;

    private Bucket(ItemStack itemStack, EntityType entityType) {
        this.itemStack = itemStack;
        this.entityType = entityType;
    }

    /**
     * Get the itemstack for this bucket
     *
     * @return Itemstack
     */
    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    /**
     * Get the type of entity for this bucket
     *
     * @return Entity type
     */
    public EntityType getEntityType() {
        return entityType;
    }
}
