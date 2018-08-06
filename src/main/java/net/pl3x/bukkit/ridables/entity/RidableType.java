package net.pl3x.bukkit.ridables.entity;

import com.google.common.collect.Maps;
import net.minecraft.server.v1_13_R1.BlockPosition;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityInsentient;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.World;
import net.pl3x.bukkit.ridables.configuration.Config;
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

    public static final RidableType CHICKEN = inject("chicken", EntityTypes.CHICKEN, Material.CHICKEN_SPAWN_EGG, EntityRidableChicken.class, EntityRidableChicken::new);
    public static final RidableType COW = inject("cow", EntityTypes.COW, Material.COW_SPAWN_EGG, EntityRidableCow.class, EntityRidableCow::new);
    public static final RidableType DOLPHIN = inject("dolphin", EntityTypes.DOLPHIN, Material.DOLPHIN_SPAWN_EGG, EntityRidableDolphin.class, EntityRidableDolphin::new);
    public static final RidableType ENDER_DRAGON = inject("ender_dragon", EntityTypes.ENDER_DRAGON, null, EntityRidableEnderDragon.class, EntityRidableEnderDragon::new);
    public static final RidableType LLAMA = inject("llama", EntityTypes.LLAMA, Material.LLAMA_SPAWN_EGG, EntityRidableLlama.class, EntityRidableLlama::new);
    public static final RidableType MOOSHROOM = inject("mooshroom", EntityTypes.MOOSHROOM, Material.MOOSHROOM_SPAWN_EGG, EntityRidableMushroomCow.class, EntityRidableMushroomCow::new);
    public static final RidableType OCELOT = inject("ocelot", EntityTypes.OCELOT, Material.OCELOT_SPAWN_EGG, EntityRidableOcelot.class, EntityRidableOcelot::new);
    public static final RidableType PHANTOM = inject("phantom", EntityTypes.PHANTOM, Material.PHANTOM_SPAWN_EGG, EntityRidablePhantom.class, EntityRidablePhantom::new);
    public static final RidableType POLAR_BEAR = inject("polar_bear", EntityTypes.POLAR_BEAR, Material.POLAR_BEAR_SPAWN_EGG, EntityRidablePolarBear.class, EntityRidablePolarBear::new);
    public static final RidableType SHEEP = inject("sheep", EntityTypes.SHEEP, Material.SHEEP_SPAWN_EGG, EntityRidableSheep.class, EntityRidableSheep::new);
    public static final RidableType TURTLE = inject("turtle", EntityTypes.TURTLE, Material.TURTLE_SPAWN_EGG, EntityRidableTurtle.class, EntityRidableTurtle::new);
    public static final RidableType WOLF = inject("wolf", EntityTypes.WOLF, Material.WOLF_SPAWN_EGG, EntityRidableWolf.class, EntityRidableWolf::new);

    public static RidableType getRidable(EntityType bukkitType) {
        return BY_BUKKIT_TYPE.get(bukkitType);
    }

    public static RidableType inject(String name, EntityTypes nmsTypes, Material spawnEgg, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function) {
        if (Config.CHICKEN_ENABLED) {
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
                //plugin.creatures().putCreature(bukkitType, clazz);

                Logger.info("Successfully injected replacement entity: " + Logger.ANSI_GREEN + name);

                RidableType ridableTypes = new RidableType(newType);
                BY_BUKKIT_TYPE.put(bukkitType, ridableTypes);
                return ridableTypes;
            }
        } else {
            Logger.info("Chicken disabled. Skipping..");
        }
        return null;
    }

    private final EntityTypes<?> entityTypes;

    private RidableType(EntityTypes<?> entityTypes) {
        this.entityTypes = entityTypes;
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
