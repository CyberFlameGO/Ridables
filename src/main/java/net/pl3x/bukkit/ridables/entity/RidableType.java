package net.pl3x.bukkit.ridables.entity;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityInsentient;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.util.Logger;
import net.pl3x.bukkit.ridables.util.RegistryHax;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.entity.EntityType;

import java.util.Map;
import java.util.function.Function;

public class RidableType {
    private final static Map<EntityType, RidableType> BY_BUKKIT_TYPE = Maps.newHashMap();

    public static final RidableType BAT = inject(Config.BAT_ENABLED, "bat", EntityTypes.BAT, Material.BAT_SPAWN_EGG, EntityRidableBat.class, EntityRidableBat::new);
    public static final RidableType CHICKEN = inject(Config.CHICKEN_ENABLED, "chicken", EntityTypes.CHICKEN, Material.CHICKEN_SPAWN_EGG, EntityRidableChicken.class, EntityRidableChicken::new);
    public static final RidableType COW = inject(Config.COW_ENABLED, "cow", EntityTypes.COW, Material.COW_SPAWN_EGG, EntityRidableCow.class, EntityRidableCow::new);
    public static final RidableType DOLPHIN = inject(Config.DOLPHIN_ENABLED, "dolphin", EntityTypes.DOLPHIN, Material.DOLPHIN_SPAWN_EGG, EntityRidableDolphin.class, EntityRidableDolphin::new, Bucket.DOLPHIN);
    public static final RidableType ENDER_DRAGON = inject(Config.DRAGON_ENABLED, "ender_dragon", EntityTypes.ENDER_DRAGON, null, EntityRidableEnderDragon.class, EntityRidableEnderDragon::new);
    public static final RidableType LLAMA = inject(Config.LLAMA_ENABLED, "llama", EntityTypes.LLAMA, Material.LLAMA_SPAWN_EGG, EntityRidableLlama.class, EntityRidableLlama::new);
    public static final RidableType MOOSHROOM = inject(Config.MOOSHROOM_ENABLED, "mooshroom", EntityTypes.MOOSHROOM, Material.MOOSHROOM_SPAWN_EGG, EntityRidableMushroomCow.class, EntityRidableMushroomCow::new);
    public static final RidableType OCELOT = inject(Config.OCELOT_ENABLED, "ocelot", EntityTypes.OCELOT, Material.OCELOT_SPAWN_EGG, EntityRidableOcelot.class, EntityRidableOcelot::new);
    public static final RidableType PARROT = inject(Config.PARROT_ENABLED, "parrot", EntityTypes.PARROT, Material.PARROT_SPAWN_EGG, EntityRidableParrot.class, EntityRidableParrot::new);
    public static final RidableType PHANTOM = inject(Config.PHANTOM_ENABLED, "phantom", EntityTypes.PHANTOM, Material.PHANTOM_SPAWN_EGG, EntityRidablePhantom.class, EntityRidablePhantom::new);
    public static final RidableType POLAR_BEAR = inject(Config.POLAR_BEAR_ENABLED, "polar_bear", EntityTypes.POLAR_BEAR, Material.POLAR_BEAR_SPAWN_EGG, EntityRidablePolarBear.class, EntityRidablePolarBear::new);
    public static final RidableType SNOWMAN = inject(Config.SNOWMAN_ENABLED, "snow_golem", EntityTypes.SNOW_GOLEM, null, EntityRidableSnowman.class, EntityRidableSnowman::new);
    public static final RidableType SHEEP = inject(Config.SHEEP_ENABLED, "sheep", EntityTypes.SHEEP, Material.SHEEP_SPAWN_EGG, EntityRidableSheep.class, EntityRidableSheep::new);
    public static final RidableType TURTLE = inject(Config.TURTLE_ENABLED, "turtle", EntityTypes.TURTLE, Material.TURTLE_SPAWN_EGG, EntityRidableTurtle.class, EntityRidableTurtle::new, Bucket.TURTLE);
    public static final RidableType WOLF = inject(Config.WOLF_ENABLED, "wolf", EntityTypes.WOLF, Material.WOLF_SPAWN_EGG, EntityRidableWolf.class, EntityRidableWolf::new);

    public static RidableType getRidable(EntityType bukkitType) {
        return BY_BUKKIT_TYPE.get(bukkitType);
    }

    private static RidableType inject(boolean enabled, String name, EntityTypes nmsTypes, Material spawnEgg, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function) {
        return inject(enabled, name, nmsTypes, spawnEgg, clazz, function, null);
    }

    private static RidableType inject(boolean enabled, String name, EntityTypes nmsTypes, Material spawnEgg, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function, Bucket waterBucket) {
        if (enabled) {
            EntityTypes.a<?> entityTypes_a = EntityTypes.a.a(clazz, function);

            MinecraftKey key = new MinecraftKey(name);
            EntityTypes<?> newType = entityTypes_a.a(name);
            EntityType bukkitType = EntityType.fromName(name);

            if (RegistryHax.injectReplacementEntityTypes(name, nmsTypes, key, newType, spawnEgg)) {
                try {
                    // these fields are only available on Paper
                    EntityTypes.clsToKeyMap.put(clazz, key);
                    EntityTypes.clsToTypeMap.put(clazz, bukkitType);
                    Logger.debug("Updated Paper's extra maps");
                } catch (NoSuchFieldError ignore) {
                }

                Logger.info("Successfully injected replacement entity: " + Logger.ANSI_GREEN + name);

                RidableType ridableTypes = new RidableType(newType, waterBucket);
                BY_BUKKIT_TYPE.put(bukkitType, ridableTypes);
                return ridableTypes;
            }
        } else {
            Logger.info("Skipping disabled entity: " + Logger.ANSI_GREEN + name);
        }
        return null;
    }

    private final EntityTypes<?> entityTypes;
    private final Bucket waterBucket;

    private RidableType(EntityTypes<?> entityTypes, Bucket waterBucket) {
        this.entityTypes = entityTypes;
        this.waterBucket = waterBucket;
    }

    public Bucket getWaterBucket() {
        return waterBucket;
    }

    public void spawn(Location loc) {
        Entity entity = entityTypes.a(((CraftWorld) loc.getWorld()).getHandle());
        if (entity != null) {
            entity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            entity.world.addEntity(entity);
            if (entity instanceof EntityInsentient) {
                EntityInsentient insentient = (EntityInsentient) entity;
                insentient.aS = insentient.yaw;
                insentient.aQ = insentient.yaw;
                insentient.prepare(entity.world.getDamageScaler(new BlockPosition(insentient)), null, null);
                insentient.A();
            }
        }
    }
}
