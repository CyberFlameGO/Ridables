package net.pl3x.bukkit.ridables.entity;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityInsentient;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.Logger;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.util.RegistryHax;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;

import java.util.Map;
import java.util.function.Function;

public class RidableType {
    private final static Map<EntityType, RidableType> BY_BUKKIT_TYPE = Maps.newHashMap();

    public static final RidableType BAT = inject(Config.BAT_ENABLED, "bat", EntityTypes.BAT, Material.BAT_SPAWN_EGG, EntityRidableBat.class, EntityRidableBat::new);
    public static final RidableType BLAZE = inject(Config.BLAZE_ENABLED, "blaze", EntityTypes.BLAZE, Material.BLAZE_SPAWN_EGG, EntityRidableBlaze.class, EntityRidableBlaze::new);
    public static final RidableType CAVE_SPIDER = inject(Config.CAVE_SPIDER_ENABLED, "cave_spider", EntityTypes.CAVE_SPIDER, Material.CAVE_SPIDER_SPAWN_EGG, EntityRidableCaveSpider.class, EntityRidableCaveSpider::new);
    public static final RidableType CHICKEN = inject(Config.CHICKEN_ENABLED, "chicken", EntityTypes.CHICKEN, Material.CHICKEN_SPAWN_EGG, EntityRidableChicken.class, EntityRidableChicken::new);
    public static final RidableType COD = inject(Config.COD_ENABLED, "cod", EntityTypes.COD, Material.COD_SPAWN_EGG, EntityRidableCod.class, EntityRidableCod::new);
    public static final RidableType COW = inject(Config.COW_ENABLED, "cow", EntityTypes.COW, Material.COW_SPAWN_EGG, EntityRidableCow.class, EntityRidableCow::new);
    public static final RidableType CREEPER = inject(Config.CREEPER_ENABLED, "creeper", EntityTypes.CREEPER, Material.CREEPER_SPAWN_EGG, EntityRidableCreeper.class, EntityRidableCreeper::new);
    public static final RidableType DOLPHIN = inject(Config.DOLPHIN_ENABLED, "dolphin", EntityTypes.DOLPHIN, Material.DOLPHIN_SPAWN_EGG, EntityRidableDolphin.class, EntityRidableDolphin::new, Bucket.DOLPHIN);
    public static final RidableType DROWNED = inject(Config.DROWNED_ENABLED, "drowned", EntityTypes.DROWNED, Material.DROWNED_SPAWN_EGG, EntityRidableDrowned.class, EntityRidableDrowned::new);
    public static final RidableType ENDER_DRAGON = inject(Config.DRAGON_ENABLED, "ender_dragon", EntityTypes.ENDER_DRAGON, null, EntityRidableEnderDragon.class, EntityRidableEnderDragon::new);
    public static final RidableType ENDERMAN = inject(Config.ENDERMAN_ENABLED, "enderman", EntityTypes.ENDERMAN, null, EntityRidableEnderman.class, EntityRidableEnderman::new);
    public static final RidableType ENDERMITE = inject(Config.ENDERMITE_ENABLED, "endermite", EntityTypes.ENDERMITE, null, EntityRidableEndermite.class, EntityRidableEndermite::new);
    public static final RidableType EVOKER = inject(Config.EVOKER_ENABLED, "evoker", EntityTypes.EVOKER, Material.EVOKER_SPAWN_EGG, EntityRidableEvoker.class, EntityRidableEvoker::new);
    public static final RidableType GHAST = inject(Config.GHAST_ENABLED, "ghast", EntityTypes.GHAST, Material.GHAST_SPAWN_EGG, EntityRidableGhast.class, EntityRidableGhast::new);
    public static final RidableType GIANT = inject(Config.GIANT_ENABLED, "giant", EntityTypes.GIANT, null, EntityRidableGiant.class, EntityRidableGiant::new);
    public static final RidableType HUSK = inject(Config.HUSK_ENABLED, "husk", EntityTypes.HUSK, Material.HUSK_SPAWN_EGG, EntityRidableHusk.class, EntityRidableHusk::new);
    public static final RidableType ILLUSIONER = inject(Config.ILLUSIONER_ENABLED, "illusioner", EntityTypes.ILLUSIONER, null, EntityRidableIllusioner.class, EntityRidableIllusioner::new);
    public static final RidableType IRON_GOLEM = inject(Config.IRON_GOLEM_ENABLED, "iron_golem", EntityTypes.IRON_GOLEM, null, EntityRidableIronGolem.class, EntityRidableIronGolem::new);
    public static final RidableType LLAMA = inject(Config.LLAMA_ENABLED, "llama", EntityTypes.LLAMA, Material.LLAMA_SPAWN_EGG, EntityRidableLlama.class, EntityRidableLlama::new);
    public static final RidableType MOOSHROOM = inject(Config.MOOSHROOM_ENABLED, "mooshroom", EntityTypes.MOOSHROOM, Material.MOOSHROOM_SPAWN_EGG, EntityRidableMushroomCow.class, EntityRidableMushroomCow::new);
    public static final RidableType OCELOT = inject(Config.OCELOT_ENABLED, "ocelot", EntityTypes.OCELOT, Material.OCELOT_SPAWN_EGG, EntityRidableOcelot.class, EntityRidableOcelot::new);
    public static final RidableType PARROT = inject(Config.PARROT_ENABLED, "parrot", EntityTypes.PARROT, Material.PARROT_SPAWN_EGG, EntityRidableParrot.class, EntityRidableParrot::new);
    public static final RidableType PIG = inject(Config.PIG_ENABLED, "pig", EntityTypes.PIG, Material.PIG_SPAWN_EGG, EntityRidablePig.class, EntityRidablePig::new);
    public static final RidableType PHANTOM = inject(Config.PHANTOM_ENABLED, "phantom", EntityTypes.PHANTOM, Material.PHANTOM_SPAWN_EGG, EntityRidablePhantom.class, EntityRidablePhantom::new);
    public static final RidableType POLAR_BEAR = inject(Config.POLAR_BEAR_ENABLED, "polar_bear", EntityTypes.POLAR_BEAR, Material.POLAR_BEAR_SPAWN_EGG, EntityRidablePolarBear.class, EntityRidablePolarBear::new);
    public static final RidableType RABBIT = inject(Config.RABBIT_ENABLED, "rabbit", EntityTypes.RABBIT, Material.RABBIT_SPAWN_EGG, EntityRidableRabbit.class, EntityRidableRabbit::new);
    public static final RidableType SHEEP = inject(Config.SHEEP_ENABLED, "sheep", EntityTypes.SHEEP, Material.SHEEP_SPAWN_EGG, EntityRidableSheep.class, EntityRidableSheep::new);
    public static final RidableType SILVERFISH = inject(Config.SILVERFISH_ENABLED, "silverfish", EntityTypes.SILVERFISH, Material.SILVERFISH_SPAWN_EGG, EntityRidableSilverfish.class, EntityRidableSilverfish::new);
    public static final RidableType SKELETON = inject(Config.SKELETON_ENABLED, "skeleton", EntityTypes.SKELETON, Material.SKELETON_SPAWN_EGG, EntityRidableSkeleton.class, EntityRidableSkeleton::new);
    public static final RidableType SKELETON_HORSE = inject(Config.SKELETON_HORSE_ENABLED, "skeleton_horse", EntityTypes.SKELETON_HORSE, Material.SKELETON_HORSE_SPAWN_EGG, EntityRidableSkeletonHorse.class, EntityRidableSkeletonHorse::new);
    public static final RidableType SNOWMAN = inject(Config.SNOWMAN_ENABLED, "snow_golem", EntityTypes.SNOW_GOLEM, null, EntityRidableSnowman.class, EntityRidableSnowman::new);
    public static final RidableType SPIDER = inject(Config.SPIDER_ENABLED, "spider", EntityTypes.SPIDER, Material.SPIDER_SPAWN_EGG, EntityRidableSpider.class, EntityRidableSpider::new);
    public static final RidableType STRAY = inject(Config.STRAY_ENABLED, "stray", EntityTypes.STRAY, Material.STRAY_SPAWN_EGG, EntityRidableStray.class, EntityRidableStray::new);
    public static final RidableType TURTLE = inject(Config.TURTLE_ENABLED, "turtle", EntityTypes.TURTLE, Material.TURTLE_SPAWN_EGG, EntityRidableTurtle.class, EntityRidableTurtle::new, Bucket.TURTLE);
    public static final RidableType VILLAGER = inject(Config.VILLAGER_ENABLED, "villager", EntityTypes.VILLAGER, Material.VILLAGER_SPAWN_EGG, EntityRidableVillager.class, EntityRidableVillager::new);
    public static final RidableType VINDICATOR = inject(Config.VINDICATOR_ENABLED, "vindicator", EntityTypes.VINDICATOR, Material.VINDICATOR_SPAWN_EGG, EntityRidableVindicator.class, EntityRidableVindicator::new);
    public static final RidableType WITCH = inject(Config.WITCH_ENABLED, "witch", EntityTypes.WITCH, Material.WITCH_SPAWN_EGG, EntityRidableWitch.class, EntityRidableWitch::new);
    public static final RidableType WITHER_SKELETON = inject(Config.WITHER_SKELETON_ENABLED, "wither_skeleton", EntityTypes.WITHER_SKELETON, Material.WITHER_SKELETON_SPAWN_EGG, EntityRidableWitherSkeleton.class, EntityRidableWitherSkeleton::new);
    public static final RidableType WOLF = inject(Config.WOLF_ENABLED, "wolf", EntityTypes.WOLF, Material.WOLF_SPAWN_EGG, EntityRidableWolf.class, EntityRidableWolf::new);
    public static final RidableType ZOMBIE = inject(Config.ZOMBIE_ENABLED, "zombie", EntityTypes.ZOMBIE, Material.ZOMBIE_SPAWN_EGG, EntityRidableZombie.class, EntityRidableZombie::new);
    public static final RidableType ZOMBIE_HORSE = inject(Config.ZOMBIE_HORSE_ENABLED, "zombie_horse", EntityTypes.ZOMBIE_HORSE, Material.ZOMBIE_HORSE_SPAWN_EGG, EntityRidableZombieHorse.class, EntityRidableZombieHorse::new);
    public static final RidableType ZOMBIE_PIGMAN = inject(Config.ZOMBIE_PIGMAN_ENABLED, "zombie_pigman", EntityTypes.ZOMBIE_PIGMAN, Material.ZOMBIE_PIGMAN_SPAWN_EGG, EntityRidableZombiePigman.class, EntityRidableZombiePigman::new);
    public static final RidableType ZOMBIE_VILLAGER = inject(Config.ZOMBIE_VILLAGER_ENABLED, "zombie_villager", EntityTypes.ZOMBIE_VILLAGER, Material.ZOMBIE_VILLAGER_SPAWN_EGG, EntityRidableZombieVillager.class, EntityRidableZombieVillager::new);

    /**
     * Gets a ridable entity of the specified type
     *
     * @param bukkitType Entity type
     * @return RidableType if one is set/loaded, otherwise null
     */
    public static RidableType getRidableType(EntityType bukkitType) {
        return BY_BUKKIT_TYPE.get(bukkitType);
    }

    public static RidableEntity getRidable(org.bukkit.entity.Entity entity) {
        net.minecraft.server.v1_13_R1.Entity craftEntity = ((CraftEntity) entity).getHandle();
        return craftEntity instanceof RidableEntity ? (RidableEntity) craftEntity : null;
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

                Logger.info("Successfully injected replacement entity: &a" + name);

                RidableType ridableTypes = new RidableType(newType, waterBucket);
                BY_BUKKIT_TYPE.put(bukkitType, ridableTypes);
                return ridableTypes;
            }
        } else {
            Logger.info("Skipping disabled entity: &a" + name);
        }
        return null;
    }

    private final EntityTypes<?> entityTypes;
    private final Bucket waterBucket;

    private RidableType(EntityTypes<?> entityTypes, Bucket waterBucket) {
        this.entityTypes = entityTypes;
        this.waterBucket = waterBucket;
    }

    /**
     * Get the bucket for this entity that can be used to place it into the world
     * <p>
     * This bucket works similar to Material.COD_BUCKET
     *
     * @return Bucket if one is set, null otherwise
     */
    public Bucket getWaterBucket() {
        return waterBucket;
    }

    /**
     * Spawn this ridable entity in the world at given location
     *
     * @param loc Location to spawn at
     */
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
