package net.pl3x.bukkit.ridables.entity;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.World;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.data.Bucket;
import net.pl3x.bukkit.ridables.util.Logger;
import net.pl3x.bukkit.ridables.util.RegistryHax;
import net.pl3x.bukkit.ridables.util.Utils;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_13_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftEntity;
import org.bukkit.entity.EntityType;

import java.util.Map;
import java.util.function.Function;

public class RidableType {
    public final static Map<EntityType, RidableType> BY_BUKKIT_TYPE = Maps.newHashMap();

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
    public static final RidableType ELDER_GUARDIAN = inject(Config.ELDER_GUARDIAN_ENABLED, "elder_guardian", EntityTypes.ELDER_GUARDIAN, RidableElderGuardian.class, RidableElderGuardian::new);
    public static final RidableType ENDER_DRAGON = inject(Config.DRAGON_ENABLED, "ender_dragon", EntityTypes.ENDER_DRAGON, RidableEnderDragon.class, RidableEnderDragon::new);
    public static final RidableType ENDERMAN = inject(Config.ENDERMAN_ENABLED, "enderman", EntityTypes.ENDERMAN, RidableEnderman.class, RidableEnderman::new);
    public static final RidableType ENDERMITE = inject(Config.ENDERMITE_ENABLED, "endermite", EntityTypes.ENDERMITE, RidableEndermite.class, RidableEndermite::new);
    public static final RidableType EVOKER = inject(Config.EVOKER_ENABLED, "evoker", EntityTypes.EVOKER, RidableEvoker.class, RidableEvoker::new);
    public static final RidableType GHAST = inject(Config.GHAST_ENABLED, "ghast", EntityTypes.GHAST, RidableGhast.class, RidableGhast::new);
    public static final RidableType GIANT = inject(Config.GIANT_ENABLED, "giant", EntityTypes.GIANT, RidableGiant.class, RidableGiant::new);
    public static final RidableType GUARDIAN = inject(Config.GUARDIAN_ENABLED, "guardian", EntityTypes.GUARDIAN, RidableGuardian.class, RidableGuardian::new);
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
    public static final RidableType SNOWMAN = inject(Config.SNOWMAN_ENABLED, "snow_golem", EntityTypes.SNOW_GOLEM, RidableSnowGolem.class, RidableSnowGolem::new);
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

    private static RidableType inject(boolean enabled, String name, EntityTypes nmsTypes, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function) {
        return inject(enabled, name, nmsTypes, clazz, function, null);
    }

    private static RidableType inject(boolean enabled, String name, EntityTypes entityTypes, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function, Bucket waterBucket) {
        if (enabled) {
            if (RegistryHax.injectReplacementEntityTypes(entityTypes, clazz, function)) {
                Logger.info("Successfully injected replacement entity: &a" + name);
                RidableType ridableTypes = new RidableType(entityTypes, waterBucket, name);
                BY_BUKKIT_TYPE.put(EntityType.fromName(name), ridableTypes);
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
        return entityTypes.a(((CraftWorld) loc.getWorld()).getHandle(), null, null, null, Utils.toBlockPosition(loc), true, false);
    }
}
