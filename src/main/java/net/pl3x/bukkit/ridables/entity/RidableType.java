package net.pl3x.bukkit.ridables.entity;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityAgeable;
import net.minecraft.server.v1_13_R2.EntityInsentient;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.util.Logger;
import net.pl3x.bukkit.ridables.util.RegistryHax;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;

import java.util.Map;
import java.util.function.Function;

public class RidableType {
    public final static Map<EntityType, RidableType> BY_BUKKIT_TYPE = Maps.newHashMap();

    public static final RidableType BAT = inject(Config.BAT_ENABLED, "bat", EntityTypes.BAT, Material.BAT_SPAWN_EGG, RidableBat.class, RidableBat::new);
    public static final RidableType BLAZE = inject(Config.BLAZE_ENABLED, "blaze", EntityTypes.BLAZE, Material.BLAZE_SPAWN_EGG, RidableBlaze.class, RidableBlaze::new);
    public static final RidableType CAVE_SPIDER = inject(Config.CAVE_SPIDER_ENABLED, "cave_spider", EntityTypes.CAVE_SPIDER, Material.CAVE_SPIDER_SPAWN_EGG, RidableCaveSpider.class, RidableCaveSpider::new);
    public static final RidableType CHICKEN = inject(Config.CHICKEN_ENABLED, "chicken", EntityTypes.CHICKEN, Material.CHICKEN_SPAWN_EGG, RidableChicken.class, RidableChicken::new);
    public static final RidableType COD = inject(Config.COD_ENABLED, "cod", EntityTypes.COD, Material.COD_SPAWN_EGG, RidableCod.class, RidableCod::new, Material.COD_BUCKET);
    public static final RidableType COW = inject(Config.COW_ENABLED, "cow", EntityTypes.COW, Material.COW_SPAWN_EGG, RidableCow.class, RidableCow::new);
    public static final RidableType CREEPER = inject(Config.CREEPER_ENABLED, "creeper", EntityTypes.CREEPER, Material.CREEPER_SPAWN_EGG, RidableCreeper.class, RidableCreeper::new);
    public static final RidableType DOLPHIN = inject(Config.DOLPHIN_ENABLED, "dolphin", EntityTypes.DOLPHIN, Material.DOLPHIN_SPAWN_EGG, RidableDolphin.class, RidableDolphin::new, Bucket.DOLPHIN);
    public static final RidableType DROWNED = inject(Config.DROWNED_ENABLED, "drowned", EntityTypes.DROWNED, Material.DROWNED_SPAWN_EGG, RidableDrowned.class, RidableDrowned::new);
    public static final RidableType ELDER_GUARDIAN = inject(Config.ELDER_GUARDIAN_ENABLED, "elder_guardian", EntityTypes.ELDER_GUARDIAN, Material.ELDER_GUARDIAN_SPAWN_EGG, RidableElderGuardian.class, RidableElderGuardian::new);
    public static final RidableType ENDER_DRAGON = inject(Config.DRAGON_ENABLED, "ender_dragon", EntityTypes.ENDER_DRAGON, null, RidableEnderDragon.class, RidableEnderDragon::new);
    public static final RidableType ENDERMAN = inject(Config.ENDERMAN_ENABLED, "enderman", EntityTypes.ENDERMAN, null, RidableEnderman.class, RidableEnderman::new);
    public static final RidableType ENDERMITE = inject(Config.ENDERMITE_ENABLED, "endermite", EntityTypes.ENDERMITE, null, RidableEndermite.class, RidableEndermite::new);
    public static final RidableType EVOKER = inject(Config.EVOKER_ENABLED, "evoker", EntityTypes.EVOKER, Material.EVOKER_SPAWN_EGG, RidableEvoker.class, RidableEvoker::new);
    public static final RidableType GHAST = inject(Config.GHAST_ENABLED, "ghast", EntityTypes.GHAST, Material.GHAST_SPAWN_EGG, RidableGhast.class, RidableGhast::new);
    public static final RidableType GIANT = inject(Config.GIANT_ENABLED, "giant", EntityTypes.GIANT, null, RidableGiant.class, RidableGiant::new);
    public static final RidableType GUARDIAN = inject(Config.GUARDIAN_ENABLED, "guardian", EntityTypes.GUARDIAN, Material.GUARDIAN_SPAWN_EGG, RidableGuardian.class, RidableGuardian::new);
    public static final RidableType HUSK = inject(Config.HUSK_ENABLED, "husk", EntityTypes.HUSK, Material.HUSK_SPAWN_EGG, RidableHusk.class, RidableHusk::new);
    public static final RidableType ILLUSIONER = inject(Config.ILLUSIONER_ENABLED, "illusioner", EntityTypes.ILLUSIONER, null, RidableIllusioner.class, RidableIllusioner::new);
    public static final RidableType IRON_GOLEM = inject(Config.IRON_GOLEM_ENABLED, "iron_golem", EntityTypes.IRON_GOLEM, null, RidableIronGolem.class, RidableIronGolem::new);
    public static final RidableType LLAMA = inject(Config.LLAMA_ENABLED, "llama", EntityTypes.LLAMA, Material.LLAMA_SPAWN_EGG, RidableLlama.class, RidableLlama::new);
    public static final RidableType MAGMA_CUBE = inject(Config.MAGMA_CUBE_ENABLED, "magma_cube", EntityTypes.MAGMA_CUBE, Material.MAGMA_CUBE_SPAWN_EGG, RidableMagmaCube.class, RidableMagmaCube::new);
    public static final RidableType MOOSHROOM = inject(Config.MOOSHROOM_ENABLED, "mooshroom", EntityTypes.MOOSHROOM, Material.MOOSHROOM_SPAWN_EGG, RidableMushroomCow.class, RidableMushroomCow::new);
    public static final RidableType OCELOT = inject(Config.OCELOT_ENABLED, "ocelot", EntityTypes.OCELOT, Material.OCELOT_SPAWN_EGG, RidableOcelot.class, RidableOcelot::new);
    public static final RidableType PARROT = inject(Config.PARROT_ENABLED, "parrot", EntityTypes.PARROT, Material.PARROT_SPAWN_EGG, RidableParrot.class, RidableParrot::new);
    public static final RidableType PHANTOM = inject(Config.PHANTOM_ENABLED, "phantom", EntityTypes.PHANTOM, Material.PHANTOM_SPAWN_EGG, RidablePhantom.class, RidablePhantom::new);
    public static final RidableType PIG = inject(Config.PIG_ENABLED, "pig", EntityTypes.PIG, Material.PIG_SPAWN_EGG, RidablePig.class, RidablePig::new);
    public static final RidableType POLAR_BEAR = inject(Config.POLAR_BEAR_ENABLED, "polar_bear", EntityTypes.POLAR_BEAR, Material.POLAR_BEAR_SPAWN_EGG, RidablePolarBear.class, RidablePolarBear::new);
    public static final RidableType PUFFERFISH = inject(Config.PUFFERFISH_ENABLED, "pufferfish", EntityTypes.PUFFERFISH, Material.PUFFERFISH_SPAWN_EGG, RidablePufferFish.class, RidablePufferFish::new, Material.PUFFERFISH_BUCKET);
    public static final RidableType RABBIT = inject(Config.RABBIT_ENABLED, "rabbit", EntityTypes.RABBIT, Material.RABBIT_SPAWN_EGG, RidableRabbit.class, RidableRabbit::new);
    public static final RidableType SALMON = inject(Config.SALMON_ENABLED, "salmon", EntityTypes.SALMON, Material.SALMON_SPAWN_EGG, RidableSalmon.class, RidableSalmon::new, Material.SALMON_BUCKET);
    public static final RidableType SHEEP = inject(Config.SHEEP_ENABLED, "sheep", EntityTypes.SHEEP, Material.SHEEP_SPAWN_EGG, RidableSheep.class, RidableSheep::new);
    public static final RidableType SHULKER = inject(Config.SHULKER_ENABLED, "shulker", EntityTypes.SHULKER, Material.SHULKER_SPAWN_EGG, RidableShulker.class, RidableShulker::new);
    public static final RidableType SILVERFISH = inject(Config.SILVERFISH_ENABLED, "silverfish", EntityTypes.SILVERFISH, Material.SILVERFISH_SPAWN_EGG, RidableSilverfish.class, RidableSilverfish::new);
    public static final RidableType SKELETON = inject(Config.SKELETON_ENABLED, "skeleton", EntityTypes.SKELETON, Material.SKELETON_SPAWN_EGG, RidableSkeleton.class, RidableSkeleton::new);
    public static final RidableType SKELETON_HORSE = inject(Config.SKELETON_HORSE_ENABLED, "skeleton_horse", EntityTypes.SKELETON_HORSE, Material.SKELETON_HORSE_SPAWN_EGG, RidableSkeletonHorse.class, RidableSkeletonHorse::new);
    public static final RidableType SLIME = inject(Config.SLIME_ENABLED, "slime", EntityTypes.SLIME, Material.SLIME_SPAWN_EGG, RidableSlime.class, RidableSlime::new);
    public static final RidableType SNOWMAN = inject(Config.SNOWMAN_ENABLED, "snow_golem", EntityTypes.SNOW_GOLEM, null, RidableSnowman.class, RidableSnowman::new);
    public static final RidableType SPIDER = inject(Config.SPIDER_ENABLED, "spider", EntityTypes.SPIDER, Material.SPIDER_SPAWN_EGG, RidableSpider.class, RidableSpider::new);
    public static final RidableType SQUID = inject(Config.SQUID_ENABLED, "squid", EntityTypes.SQUID, Material.SQUID_SPAWN_EGG, RidableSquid.class, RidableSquid::new, Bucket.SQUID);
    public static final RidableType STRAY = inject(Config.STRAY_ENABLED, "stray", EntityTypes.STRAY, Material.STRAY_SPAWN_EGG, RidableStray.class, RidableStray::new);
    public static final RidableType TROPICAL_FISH = inject(Config.TROPICAL_FISH_ENABLED, "tropical_fish", EntityTypes.TROPICAL_FISH, Material.TROPICAL_FISH_SPAWN_EGG, RidableTropicalFish.class, RidableTropicalFish::new, Material.TROPICAL_FISH_BUCKET);
    public static final RidableType TURTLE = inject(Config.TURTLE_ENABLED, "turtle", EntityTypes.TURTLE, Material.TURTLE_SPAWN_EGG, RidableTurtle.class, RidableTurtle::new, Bucket.TURTLE);
    public static final RidableType VEX = inject(Config.VEX_ENABLED, "vex", EntityTypes.VEX, Material.VEX_SPAWN_EGG, RidableVex.class, RidableVex::new);
    public static final RidableType VILLAGER = inject(Config.VILLAGER_ENABLED, "villager", EntityTypes.VILLAGER, Material.VILLAGER_SPAWN_EGG, RidableVillager.class, RidableVillager::new);
    public static final RidableType VINDICATOR = inject(Config.VINDICATOR_ENABLED, "vindicator", EntityTypes.VINDICATOR, Material.VINDICATOR_SPAWN_EGG, RidableVindicator.class, RidableVindicator::new);
    public static final RidableType WITCH = inject(Config.WITCH_ENABLED, "witch", EntityTypes.WITCH, Material.WITCH_SPAWN_EGG, RidableWitch.class, RidableWitch::new);
    public static final RidableType WITHER = inject(Config.WITHER_ENABLED, "wither", EntityTypes.WITHER, null, RidableWither.class, RidableWither::new);
    public static final RidableType WITHER_SKELETON = inject(Config.WITHER_SKELETON_ENABLED, "wither_skeleton", EntityTypes.WITHER_SKELETON, Material.WITHER_SKELETON_SPAWN_EGG, RidableWitherSkeleton.class, RidableWitherSkeleton::new);
    public static final RidableType WOLF = inject(Config.WOLF_ENABLED, "wolf", EntityTypes.WOLF, Material.WOLF_SPAWN_EGG, RidableWolf.class, RidableWolf::new);
    public static final RidableType ZOMBIE = inject(Config.ZOMBIE_ENABLED, "zombie", EntityTypes.ZOMBIE, Material.ZOMBIE_SPAWN_EGG, RidableZombie.class, RidableZombie::new);
    public static final RidableType ZOMBIE_HORSE = inject(Config.ZOMBIE_HORSE_ENABLED, "zombie_horse", EntityTypes.ZOMBIE_HORSE, Material.ZOMBIE_HORSE_SPAWN_EGG, RidableZombieHorse.class, RidableZombieHorse::new);
    public static final RidableType ZOMBIE_PIGMAN = inject(Config.ZOMBIE_PIGMAN_ENABLED, "zombie_pigman", EntityTypes.ZOMBIE_PIGMAN, Material.ZOMBIE_PIGMAN_SPAWN_EGG, RidableZombiePigman.class, RidableZombiePigman::new);
    public static final RidableType ZOMBIE_VILLAGER = inject(Config.ZOMBIE_VILLAGER_ENABLED, "zombie_villager", EntityTypes.ZOMBIE_VILLAGER, Material.ZOMBIE_VILLAGER_SPAWN_EGG, RidableZombieVillager.class, RidableZombieVillager::new);

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
        net.minecraft.server.v1_13_R2.Entity craftEntity = ((CraftEntity) entity).getHandle();
        return craftEntity instanceof RidableEntity ? (RidableEntity) craftEntity : null;
    }

    private static RidableType inject(boolean enabled, String name, EntityTypes nmsTypes, Material spawnEgg, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function) {
        return inject(enabled, name, nmsTypes, spawnEgg, clazz, function, null, null);
    }

    private static RidableType inject(boolean enabled, String name, EntityTypes nmsTypes, Material spawnEgg, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function, Material fishBucket) {
        return inject(enabled, name, nmsTypes, spawnEgg, clazz, function, null, fishBucket);
    }

    private static RidableType inject(boolean enabled, String name, EntityTypes nmsTypes, Material spawnEgg, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function, Bucket waterBucket) {
        return inject(enabled, name, nmsTypes, spawnEgg, clazz, function, waterBucket, null);
    }

    private static RidableType inject(boolean enabled, String name, EntityTypes nmsTypes, Material spawnEgg, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function, Bucket waterBucket, Material fishBucket) {
        if (enabled) {
            EntityTypes.a<?> entityTypes_a = EntityTypes.a.a(clazz, function);

            MinecraftKey key = new MinecraftKey(name);
            EntityTypes<?> newType = entityTypes_a.a(name);
            EntityType bukkitType = EntityType.fromName(name);

            if (RegistryHax.injectReplacementEntityTypes(name, nmsTypes, key, newType, spawnEgg, fishBucket)) {
                try {
                    // these fields are only available on Paper
                    EntityTypes.clsToKeyMap.put(clazz, key);
                    EntityTypes.clsToTypeMap.put(clazz, bukkitType);
                    Logger.debug("Updated Paper's extra maps");
                } catch (NoSuchFieldError ignore) {
                }

                Logger.info("Successfully injected replacement entity: &a" + name);

                RidableType ridableTypes = new RidableType(newType, waterBucket, name);
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
    private final String name;

    private RidableType(EntityTypes<?> entityTypes, Bucket waterBucket, String name) {
        this.entityTypes = entityTypes;
        this.waterBucket = waterBucket;
        this.name = name;
    }

    /**
     * Get the mojang name of this entity type
     *
     * @return Entity type name
     */
    public String getName() {
        return name;
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
     * @return The spawned entity
     */
    public Entity spawn(Location loc) {
        return spawn(loc, false);
    }

    /**
     * Spawn this ridable entity in the world at given location
     *
     * @param loc  Location to spawn at
     * @param baby True to make baby
     * @return The spawned entity
     */
    public Entity spawn(Location loc, boolean baby) {
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
            if (baby && entity instanceof EntityAgeable) {
                ((EntityAgeable) entity).setAgeRaw(-24000);
            }
        }
        return entity;
    }
}
