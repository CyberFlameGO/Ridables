package net.pl3x.bukkit.ridables.entity;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_13_R2.AttributeRanged;
import net.minecraft.server.v1_13_R2.BlockPosition;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.IAttribute;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.MobConfig;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.entity.ambient.RidableBat;
import net.pl3x.bukkit.ridables.entity.animal.RidableChicken;
import net.pl3x.bukkit.ridables.entity.animal.RidableCow;
import net.pl3x.bukkit.ridables.entity.animal.RidableDolphin;
import net.pl3x.bukkit.ridables.entity.animal.RidableIronGolem;
import net.pl3x.bukkit.ridables.entity.animal.RidableMooshroom;
import net.pl3x.bukkit.ridables.entity.animal.RidableOcelot;
import net.pl3x.bukkit.ridables.entity.animal.RidableParrot;
import net.pl3x.bukkit.ridables.entity.animal.RidablePig;
import net.pl3x.bukkit.ridables.entity.animal.RidablePolarBear;
import net.pl3x.bukkit.ridables.entity.animal.RidableRabbit;
import net.pl3x.bukkit.ridables.entity.animal.RidableSheep;
import net.pl3x.bukkit.ridables.entity.animal.RidableSnowGolem;
import net.pl3x.bukkit.ridables.entity.animal.RidableSquid;
import net.pl3x.bukkit.ridables.entity.animal.RidableTurtle;
import net.pl3x.bukkit.ridables.entity.animal.RidableWolf;
import net.pl3x.bukkit.ridables.entity.animal.fish.RidableCod;
import net.pl3x.bukkit.ridables.entity.animal.fish.RidablePufferFish;
import net.pl3x.bukkit.ridables.entity.animal.fish.RidableSalmon;
import net.pl3x.bukkit.ridables.entity.animal.fish.RidableTropicalFish;
import net.pl3x.bukkit.ridables.entity.animal.horse.RidableDonkey;
import net.pl3x.bukkit.ridables.entity.animal.horse.RidableHorse;
import net.pl3x.bukkit.ridables.entity.animal.horse.RidableLlama;
import net.pl3x.bukkit.ridables.entity.animal.horse.RidableMule;
import net.pl3x.bukkit.ridables.entity.animal.horse.RidableSkeletonHorse;
import net.pl3x.bukkit.ridables.entity.animal.horse.RidableZombieHorse;
import net.pl3x.bukkit.ridables.entity.boss.RidableEnderDragon;
import net.pl3x.bukkit.ridables.entity.boss.RidableWither;
import net.pl3x.bukkit.ridables.entity.monster.RidableBlaze;
import net.pl3x.bukkit.ridables.entity.monster.RidableCreeper;
import net.pl3x.bukkit.ridables.entity.monster.RidableEnderman;
import net.pl3x.bukkit.ridables.entity.monster.RidableEndermite;
import net.pl3x.bukkit.ridables.entity.monster.RidableEvoker;
import net.pl3x.bukkit.ridables.entity.monster.RidableGhast;
import net.pl3x.bukkit.ridables.entity.monster.RidableGiant;
import net.pl3x.bukkit.ridables.entity.monster.RidableIllusioner;
import net.pl3x.bukkit.ridables.entity.monster.RidablePhantom;
import net.pl3x.bukkit.ridables.entity.monster.RidableShulker;
import net.pl3x.bukkit.ridables.entity.monster.RidableSilverfish;
import net.pl3x.bukkit.ridables.entity.monster.RidableVex;
import net.pl3x.bukkit.ridables.entity.monster.RidableVindicator;
import net.pl3x.bukkit.ridables.entity.monster.RidableWitch;
import net.pl3x.bukkit.ridables.entity.monster.guardian.RidableElderGuardian;
import net.pl3x.bukkit.ridables.entity.monster.guardian.RidableGuardian;
import net.pl3x.bukkit.ridables.entity.monster.skeleton.RidableSkeleton;
import net.pl3x.bukkit.ridables.entity.monster.skeleton.RidableStray;
import net.pl3x.bukkit.ridables.entity.monster.skeleton.RidableWitherSkeleton;
import net.pl3x.bukkit.ridables.entity.monster.slime.RidableMagmaCube;
import net.pl3x.bukkit.ridables.entity.monster.slime.RidableSlime;
import net.pl3x.bukkit.ridables.entity.monster.spider.RidableCaveSpider;
import net.pl3x.bukkit.ridables.entity.monster.spider.RidableSpider;
import net.pl3x.bukkit.ridables.entity.monster.zombie.RidableDrowned;
import net.pl3x.bukkit.ridables.entity.monster.zombie.RidableHusk;
import net.pl3x.bukkit.ridables.entity.monster.zombie.RidableZombie;
import net.pl3x.bukkit.ridables.entity.monster.zombie.RidableZombiePigman;
import net.pl3x.bukkit.ridables.entity.monster.zombie.RidableZombieVillager;
import net.pl3x.bukkit.ridables.entity.npc.RidableVillager;
import net.pl3x.bukkit.ridables.util.Logger;
import net.pl3x.bukkit.ridables.util.RegistryHax;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Function;

public class RidableType {
    public final static Map<EntityType, RidableType> BY_BUKKIT_TYPE = Maps.newHashMap();
    public final static Map<EntityTypes, RidableType> BY_NMS_TYPE = Maps.newHashMap();

    public static final RidableType BAT = inject(Config.BAT_ENABLED, "bat", EntityTypes.BAT, RidableBat.class, RidableBat::new);
    public static final RidableType BLAZE = inject(Config.BLAZE_ENABLED, "blaze", EntityTypes.BLAZE, RidableBlaze.class, RidableBlaze::new);
    public static final RidableType CAVE_SPIDER = inject(Config.CAVE_SPIDER_ENABLED, "cave_spider", EntityTypes.CAVE_SPIDER, RidableCaveSpider.class, RidableCaveSpider::new);
    public static final RidableType CHICKEN = inject(Config.CHICKEN_ENABLED, "chicken", EntityTypes.CHICKEN, RidableChicken.class, RidableChicken::new);
    public static final RidableType COD = inject(Config.COD_ENABLED, "cod", EntityTypes.COD, RidableCod.class, RidableCod::new);
    public static final RidableType COW = inject(Config.COW_ENABLED, "cow", EntityTypes.COW, RidableCow.class, RidableCow::new);
    public static final RidableType CREEPER = inject(Config.CREEPER_ENABLED, "creeper", EntityTypes.CREEPER, RidableCreeper.class, RidableCreeper::new);
    public static final RidableType DOLPHIN = inject(Config.DOLPHIN_ENABLED, "dolphin", EntityTypes.DOLPHIN, RidableDolphin.class, RidableDolphin::new, Bucket.DOLPHIN);
    public static final RidableType DONKEY = inject(Config.DONKEY_ENABLED, "donkey", EntityTypes.DONKEY, RidableDonkey.class, RidableDonkey::new);
    public static final RidableType DROWNED = inject(Config.DROWNED_ENABLED, "drowned", EntityTypes.DROWNED, RidableDrowned.class, RidableDrowned::new);
    public static final RidableType ELDER_GUARDIAN = inject(Config.ELDER_GUARDIAN_ENABLED, "elder_guardian", EntityTypes.ELDER_GUARDIAN, RidableElderGuardian.class, RidableElderGuardian::new, Bucket.ELDER_GUARDIAN);
    public static final RidableType ENDER_DRAGON = inject(Config.ENDER_DRAGON_ENABLED, "ender_dragon", EntityTypes.ENDER_DRAGON, RidableEnderDragon.class, RidableEnderDragon::new);
    public static final RidableType ENDERMAN = inject(Config.ENDERMAN_ENABLED, "enderman", EntityTypes.ENDERMAN, RidableEnderman.class, RidableEnderman::new);
    public static final RidableType ENDERMITE = inject(Config.ENDERMITE_ENABLED, "endermite", EntityTypes.ENDERMITE, RidableEndermite.class, RidableEndermite::new);
    public static final RidableType EVOKER = inject(Config.EVOKER_ENABLED, "evoker", EntityTypes.EVOKER, RidableEvoker.class, RidableEvoker::new);
    public static final RidableType GHAST = inject(Config.GHAST_ENABLED, "ghast", EntityTypes.GHAST, RidableGhast.class, RidableGhast::new);
    public static final RidableType GIANT = inject(Config.GIANT_ENABLED, "giant", EntityTypes.GIANT, RidableGiant.class, RidableGiant::new);
    public static final RidableType GUARDIAN = inject(Config.GUARDIAN_ENABLED, "guardian", EntityTypes.GUARDIAN, RidableGuardian.class, RidableGuardian::new, Bucket.GUARDIAN);
    public static final RidableType HORSE = inject(Config.HORSE_ENABLED, "horse", EntityTypes.HORSE, RidableHorse.class, RidableHorse::new);
    public static final RidableType HUSK = inject(Config.HUSK_ENABLED, "husk", EntityTypes.HUSK, RidableHusk.class, RidableHusk::new);
    public static final RidableType ILLUSIONER = inject(Config.ILLUSIONER_ENABLED, "illusioner", EntityTypes.ILLUSIONER, RidableIllusioner.class, RidableIllusioner::new);
    public static final RidableType IRON_GOLEM = inject(Config.IRON_GOLEM_ENABLED, "iron_golem", EntityTypes.IRON_GOLEM, RidableIronGolem.class, RidableIronGolem::new);
    public static final RidableType LLAMA = inject(Config.LLAMA_ENABLED, "llama", EntityTypes.LLAMA, RidableLlama.class, RidableLlama::new);
    public static final RidableType MAGMA_CUBE = inject(Config.MAGMA_CUBE_ENABLED, "magma_cube", EntityTypes.MAGMA_CUBE, RidableMagmaCube.class, RidableMagmaCube::new);
    public static final RidableType MOOSHROOM = inject(Config.MOOSHROOM_ENABLED, "mooshroom", EntityTypes.MOOSHROOM, RidableMooshroom.class, RidableMooshroom::new);
    public static final RidableType MULE = inject(Config.MULE_ENABLED, "mule", EntityTypes.MULE, RidableMule.class, RidableMule::new);
    public static final RidableType OCELOT = inject(Config.OCELOT_ENABLED, "ocelot", EntityTypes.OCELOT, RidableOcelot.class, RidableOcelot::new);
    public static final RidableType PARROT = inject(Config.PARROT_ENABLED, "parrot", EntityTypes.PARROT, RidableParrot.class, RidableParrot::new);
    public static final RidableType PHANTOM = inject(Config.PHANTOM_ENABLED, "phantom", EntityTypes.PHANTOM, RidablePhantom.class, RidablePhantom::new);
    public static final RidableType PIG = inject(Config.PIG_ENABLED, "pig", EntityTypes.PIG, RidablePig.class, RidablePig::new);
    public static final RidableType POLAR_BEAR = inject(Config.POLAR_BEAR_ENABLED, "polar_bear", EntityTypes.POLAR_BEAR, RidablePolarBear.class, RidablePolarBear::new);
    public static final RidableType PUFFERFISH = inject(Config.PUFFERFISH_ENABLED, "pufferfish", EntityTypes.PUFFERFISH, RidablePufferFish.class, RidablePufferFish::new);
    public static final RidableType RABBIT = inject(Config.RABBIT_ENABLED, "rabbit", EntityTypes.RABBIT, RidableRabbit.class, RidableRabbit::new);
    public static final RidableType SALMON = inject(Config.SALMON_ENABLED, "salmon", EntityTypes.SALMON, RidableSalmon.class, RidableSalmon::new);
    public static final RidableType SHEEP = inject(Config.SHEEP_ENABLED, "sheep", EntityTypes.SHEEP, RidableSheep.class, RidableSheep::new);
    public static final RidableType SHULKER = inject(Config.SHULKER_ENABLED, "shulker", EntityTypes.SHULKER, RidableShulker.class, RidableShulker::new);
    public static final RidableType SILVERFISH = inject(Config.SILVERFISH_ENABLED, "silverfish", EntityTypes.SILVERFISH, RidableSilverfish.class, RidableSilverfish::new);
    public static final RidableType SKELETON = inject(Config.SKELETON_ENABLED, "skeleton", EntityTypes.SKELETON, RidableSkeleton.class, RidableSkeleton::new);
    public static final RidableType SKELETON_HORSE = inject(Config.SKELETON_HORSE_ENABLED, "skeleton_horse", EntityTypes.SKELETON_HORSE, RidableSkeletonHorse.class, RidableSkeletonHorse::new);
    public static final RidableType SLIME = inject(Config.SLIME_ENABLED, "slime", EntityTypes.SLIME, RidableSlime.class, RidableSlime::new);
    public static final RidableType SNOWMAN = inject(Config.SNOW_GOLEM_ENABLED, "snow_golem", EntityTypes.SNOW_GOLEM, RidableSnowGolem.class, RidableSnowGolem::new);
    public static final RidableType SPIDER = inject(Config.SPIDER_ENABLED, "spider", EntityTypes.SPIDER, RidableSpider.class, RidableSpider::new);
    public static final RidableType SQUID = inject(Config.SQUID_ENABLED, "squid", EntityTypes.SQUID, RidableSquid.class, RidableSquid::new, Bucket.SQUID);
    public static final RidableType STRAY = inject(Config.STRAY_ENABLED, "stray", EntityTypes.STRAY, RidableStray.class, RidableStray::new);
    public static final RidableType TROPICAL_FISH = inject(Config.TROPICAL_FISH_ENABLED, "tropical_fish", EntityTypes.TROPICAL_FISH, RidableTropicalFish.class, RidableTropicalFish::new);
    public static final RidableType TURTLE = inject(Config.TURTLE_ENABLED, "turtle", EntityTypes.TURTLE, RidableTurtle.class, RidableTurtle::new, Bucket.TURTLE);
    public static final RidableType VEX = inject(Config.VEX_ENABLED, "vex", EntityTypes.VEX, RidableVex.class, RidableVex::new);
    public static final RidableType VILLAGER = inject(Config.VILLAGER_ENABLED, "villager", EntityTypes.VILLAGER, RidableVillager.class, RidableVillager::new);
    public static final RidableType VINDICATOR = inject(Config.VINDICATOR_ENABLED, "vindicator", EntityTypes.VINDICATOR, RidableVindicator.class, RidableVindicator::new);
    public static final RidableType WITCH = inject(Config.WITCH_ENABLED, "witch", EntityTypes.WITCH, RidableWitch.class, RidableWitch::new);
    public static final RidableType WITHER = inject(Config.WITHER_ENABLED, "wither", EntityTypes.WITHER, RidableWither.class, RidableWither::new);
    public static final RidableType WITHER_SKELETON = inject(Config.WITHER_SKELETON_ENABLED, "wither_skeleton", EntityTypes.WITHER_SKELETON, RidableWitherSkeleton.class, RidableWitherSkeleton::new);
    public static final RidableType WOLF = inject(Config.WOLF_ENABLED, "wolf", EntityTypes.WOLF, RidableWolf.class, RidableWolf::new);
    public static final RidableType ZOMBIE = inject(Config.ZOMBIE_ENABLED, "zombie", EntityTypes.ZOMBIE, RidableZombie.class, RidableZombie::new);
    public static final RidableType ZOMBIE_HORSE = inject(Config.ZOMBIE_HORSE_ENABLED, "zombie_horse", EntityTypes.ZOMBIE_HORSE, RidableZombieHorse.class, RidableZombieHorse::new);
    public static final RidableType ZOMBIE_PIGMAN = inject(Config.ZOMBIE_PIGMAN_ENABLED, "zombie_pigman", EntityTypes.ZOMBIE_PIGMAN, RidableZombiePigman.class, RidableZombiePigman::new);
    public static final RidableType ZOMBIE_VILLAGER = inject(Config.ZOMBIE_VILLAGER_ENABLED, "zombie_villager", EntityTypes.ZOMBIE_VILLAGER, RidableZombieVillager.class, RidableZombieVillager::new);

    public static final IAttribute RIDING_SPEED = (new AttributeRanged(null, "generic.rideSpeed", 1.0D, 0.0D, 1024.0D)).a("Ride Speed").a(true);
    public static final IAttribute RIDING_MAX_Y = (new AttributeRanged(null, "generic.rideMaxY", 256.0D, 0.0D, 1024.0D)).a("Ride Max Y").a(true);

    /**
     * Get all ridable types that are loaded
     *
     * @return All loaded ridable types
     */
    public Collection<RidableType> getAllRidableTypes() {
        return new HashSet<>(BY_BUKKIT_TYPE.values());
    }

    /**
     * Gets a ridable type from a Bukkit type
     *
     * @param bukkitType Entity type
     * @return RidableType
     */
    @Nullable
    public static RidableType getRidableType(EntityType bukkitType) {
        return BY_BUKKIT_TYPE.get(bukkitType);
    }

    /**
     * Get a ridable type from an NMS type
     *
     * @param nmsType Entity type
     * @return RidableType
     */
    @Nullable
    public static RidableType getRidableType(EntityTypes nmsType) {
        return BY_NMS_TYPE.get(nmsType);
    }

    /**
     * Get a ridable entity from a Bukkit entity
     *
     * @param entity Bukkit entity
     * @return RidableEntity
     */
    @Nullable
    public static RidableEntity getRidable(org.bukkit.entity.Entity entity) {
        net.minecraft.server.v1_13_R2.Entity craftEntity = ((CraftEntity) entity).getHandle();
        return craftEntity instanceof RidableEntity ? (RidableEntity) craftEntity : null;
    }

    private static RidableType inject(boolean enabled, String name, EntityTypes nmsType, Class<? extends RidableEntity> clazz, Function<? super World, ? extends RidableEntity> function) {
        return inject(enabled, name, nmsType, clazz, function, null);
    }

    private static RidableType inject(boolean enabled, String name, EntityTypes nmsType, Class<? extends RidableEntity> clazz, Function<? super World, ? extends RidableEntity> function, Bucket waterBucket) {
        if (enabled) {
            if (RegistryHax.injectReplacementEntityTypes(nmsType, clazz, function)) {
                Logger.info("Successfully injected replacement entity: &a" + name);
                RidableType ridableType = new RidableType(name, nmsType, clazz, waterBucket);
                BY_BUKKIT_TYPE.put(EntityType.fromName(name), ridableType);
                BY_NMS_TYPE.put(nmsType, ridableType);
                return ridableType;
            }
            Logger.error("Failed to inject replacement entity: &e" + name);
        } else {
            Logger.warn("Skipping disabled entity: &a" + name);
        }
        return null;
    }

    private final EntityTypes<?> entityTypes;
    private final Bucket waterBucket;
    private final String name;
    private final Class<? extends RidableEntity> clazz;

    private RidableType(String name, EntityTypes<?> entityTypes, Class<? extends RidableEntity> clazz, Bucket waterBucket) {
        this.entityTypes = entityTypes;
        this.waterBucket = waterBucket;
        this.name = name;
        this.clazz = clazz;
        getConfig(); // force configs to load on startup
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
     * Get mob's configuration for this ridable type
     *
     * @return MobConfig
     */
    public MobConfig getConfig() {
        try {
            return (MobConfig) clazz.getDeclaredField("CONFIG").get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get the bucket for this entity that can be used to place it into the world
     * <p>
     * This bucket works similar to Material.COD_BUCKET
     *
     * @return Bucket if one is set, null otherwise
     */
    @Nullable
    public Bucket getWaterBucket() {
        return waterBucket;
    }

    /**
     * Spawn this ridable entity in the world at given location
     *
     * @param loc Location to spawn at
     * @return Entity that was spawned, or null if entity could not be spawned
     */
    @Nullable
    public Entity spawn(Location loc) {
        return spawn(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    /**
     * Spawn this ridable entity in the world at given coordinates
     *
     * @param world World to spawn in
     * @param x     X coordinate
     * @param y     Y coordinate
     * @param z     Z coordinate
     * @return Entity that was spawned, or null if entity could not be spawned
     */
    @Nullable
    public Entity spawn(org.bukkit.World world, int x, int y, int z) {
        return spawn(((CraftWorld) world).getHandle(), x, y, z);
    }

    /**
     * Spawn this ridable entity in the world at given coordinates
     *
     * @param world World to spawn in
     * @param x     X coordinate
     * @param y     Y coordinate
     * @param z     Z coordinate
     * @return Entity that was spawned, or null if entity could not be spawned
     */
    @Nullable
    public Entity spawn(World world, int x, int y, int z) {
        return spawn(world, null, null, null, new BlockPosition(x, y, z), true, false);
    }

    /**
     * Spawns entity
     *
     * @param world  World to spawn in
     * @param nbt    EntityTag NBT
     * @param name   Custom entity name
     * @param player Player reference, used to check OP status for applying EntityTag NBT
     * @param pos    Position to spawn at
     * @param center Center entity on position and correct Y for entity height
     * @param fixY   Not exactly sure yet. Alters the Y position. This is only ever true when using a spawn egg and clicked block face is UP.
     * @return Entity that was spawned, or null if entity could not be spawned
     */
    @Nullable
    public Entity spawn(World world, @Nullable NBTTagCompound nbt, @Nullable IChatBaseComponent name, @Nullable EntityHuman player, BlockPosition pos, boolean center, boolean fixY) {
        net.minecraft.server.v1_13_R2.Entity nmsEntity = entityTypes.a(world, nbt, name, player, pos, center, fixY); // spawn
        return nmsEntity == null ? null : nmsEntity.getBukkitEntity();
    }
}
