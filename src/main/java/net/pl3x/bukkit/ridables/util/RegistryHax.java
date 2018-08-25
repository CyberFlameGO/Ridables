package net.pl3x.bukkit.ridables.util;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.mojang.datafixers.types.Type;
import net.minecraft.server.v1_13_R1.BiomeBase;
import net.minecraft.server.v1_13_R1.Biomes;
import net.minecraft.server.v1_13_R1.DataConverterRegistry;
import net.minecraft.server.v1_13_R1.DataConverterTypes;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.Item;
import net.minecraft.server.v1_13_R1.ItemFishBucket;
import net.minecraft.server.v1_13_R1.ItemMonsterEgg;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.RegistryID;
import net.minecraft.server.v1_13_R1.RegistryMaterials;
import net.minecraft.server.v1_13_R1.RegistrySimple;
import net.minecraft.server.v1_13_R1.World;
import net.minecraft.server.v1_13_R1.WorldGenDungeons;
import net.minecraft.server.v1_13_R1.WorldGenFeatureSwampHut;
import net.minecraft.server.v1_13_R1.WorldGenMonument;
import net.minecraft.server.v1_13_R1.WorldGenNether;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.function.Function;

public class RegistryHax {
    private static Field materials_field_a;
    private static Field registry_field_b;
    private static Field registry_field_c;
    private static Field registry_field_d;
    private static Field materials_field_b;
    private static Field simple_field_c;
    private static Field field_modifiers;
    private static Field item_field_a;
    private static Field item_field_d;
    private static Method registry_method_d;
    private static Method registry_method_e;
    private static Method biomebase_method_a;

    static {
        try {
            materials_field_a = RegistryMaterials.class.getDeclaredField("a");
            materials_field_a.setAccessible(true);
            registry_field_b = RegistryID.class.getDeclaredField("b");
            registry_field_b.setAccessible(true);
            registry_field_c = RegistryID.class.getDeclaredField("c");
            registry_field_c.setAccessible(true);
            registry_field_d = RegistryID.class.getDeclaredField("d");
            registry_field_d.setAccessible(true);
            materials_field_b = RegistryMaterials.class.getDeclaredField("b");
            materials_field_b.setAccessible(true);
            simple_field_c = RegistrySimple.class.getDeclaredField("c");
            simple_field_c.setAccessible(true);
            field_modifiers = Field.class.getDeclaredField("modifiers");
            field_modifiers.setAccessible(true);
            item_field_a = ItemFishBucket.class.getDeclaredField("a");
            item_field_a.setAccessible(true);
            item_field_d = ItemMonsterEgg.class.getDeclaredField("d");
            item_field_d.setAccessible(true);
            registry_method_d = RegistryID.class.getDeclaredMethod("d", Object.class);
            registry_method_d.setAccessible(true);
            registry_method_e = RegistryID.class.getDeclaredMethod("e", int.class);
            registry_method_e.setAccessible(true);
            biomebase_method_a = BiomeBase.class.getDeclaredMethod("a", int.class, String.class, BiomeBase.class);
            biomebase_method_a.setAccessible(true);
        } catch (NoSuchFieldException | NoSuchMethodException ignore) {
        }
    }

    public static void injectNewEntityTypes(String name, String extend_from, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function) {
        Logger.debug("Attempting to inject new entity: &3" + name);
        Logger.debug("Injecting new datatypes");
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(15190).findChoiceType(DataConverterTypes.n).types();
        dataTypes.put("minecraft:" + name, dataTypes.get("minecraft:" + extend_from));
        Logger.debug("Injecting new EntityTypes");
        EntityTypes.a(name, EntityTypes.a.a(clazz, function));
        Logger.info("Successfully injected new entity: &a" + name);
    }

    public static boolean injectReplacementEntityTypes(String name, EntityTypes entityTypes, MinecraftKey key, EntityTypes<?> newType, Material spawnEggMaterial, Material fishBucketMaterial) {
        Logger.debug("Attempting to inject replacement entity: &3" + name);
        try {
            RegistryID<EntityTypes<?>> registry = (RegistryID<EntityTypes<?>>) materials_field_a.get(EntityTypes.REGISTRY);
            int id = registry.getId(entityTypes);

            Logger.debug("Detected original id: " + id);

            Object[] array_b = (Object[]) registry_field_b.get(registry);
            Object[] array_d = (Object[]) registry_field_d.get(registry);

            if (id < 0) {
                Logger.debug("&cInvalid id detected. Trying again!");
                for (int i = 0; i < array_d.length; i++) {
                    if (array_d[i] != null) {
                        if (array_d[i] == entityTypes) {
                            Logger.debug("Found EntityTypes id by reference");
                            id = i;
                            break;
                        }
                        if (((EntityTypes) array_d[i]).d().equals("minecraft:" + name)) {
                            Logger.debug("&cFound EntityTypes id using name but not reference! What?!");
                            id = i;
                            break;
                        }
                    }
                }
                Logger.debug("New detected id: " + id);
            }

            Logger.debug("EntityType at id " + id + ": " + ((EntityTypes) array_d[id]).d());

            int oldIndex = -1;
            for (int i = 0; i < array_b.length; i++) {
                if (array_b[i] != null) {
                    if (array_b[i] == entityTypes) {
                        //array_b[i] = null; // do not remove old reference (might be causing issues?)
                        oldIndex = i;
                        break;
                    }
                    if (((EntityTypes) array_b[i]).d().equals("minecraft:" + name)) {
                        Logger.debug("&cFound EntityTypes oldIndex using name but not reference! What?!");
                        //array_b[i] = null; // do not remove old reference (might be causing issues?)
                        oldIndex = i;
                        break;
                    }
                }
            }

            Logger.debug("Detected oldIndex: " + oldIndex);

            if (oldIndex < 0) {
                Logger.debug("&cInvalid oldIndex detected. Trying again!");
                array_b = (Object[]) registry_field_b.get(registry);
                for (int i = 0; i < array_b.length; i++) {
                    if (array_b[i] != null) {
                        if (array_b[i] == entityTypes) {
                            //array_b[i] = null; // do not remove old reference (might be causing issues?)
                            oldIndex = i;
                            break;
                        }
                        if (((EntityTypes) array_b[i]).d().equals("minecraft:" + name)) {
                            Logger.debug("&cFound EntityTypes oldIndex using name but not reference! What?!");
                            //array_b[i] = null; // do not remove old reference (might be causing issues?)
                            oldIndex = i;
                            break;
                        }
                    }
                }
                Logger.debug("New detected oldIndex: " + oldIndex);
            }

            Logger.debug("EntityType at oldIndex " + oldIndex + ": " + ((EntityTypes) array_b[oldIndex]).d());

            int newIndex = (int) registry_method_e.invoke(registry, registry_method_d.invoke(registry, newType));
            Logger.debug("Generated newIndex: " + newIndex);

            EntityTypes e = (EntityTypes) array_b[newIndex];
            Logger.debug("EntityType at newIndex " + newIndex + ": " + (e == null ? "null" : "&c" + e.d()));

            Logger.debug("Injecting new EntityTypes to b[newIndex]: " + newIndex);
            array_b[newIndex] = newType;
            Logger.debug("Injecting new EntityTypes to d[id]: " + id);
            array_d[id] = newType;

            int[] array_c = (int[]) registry_field_c.get(registry);
            if (oldIndex >= 0) {
                Logger.debug("Removing c[oldIndex] reference: " + oldIndex + ":0");
                array_c[oldIndex] = 0;
            } else {
                Logger.debug("&cSkipping c[oldIndex] reference: " + oldIndex + ":0");
            }
            Logger.debug("Injecting c[newIndex] reference: " + newIndex + ":" + id);
            array_c[newIndex] = id;

            Logger.debug("Updating RegistryMaterials mapB");
            Map<EntityTypes<?>, MinecraftKey> map_b_old = (Map<EntityTypes<?>, MinecraftKey>) materials_field_b.get(EntityTypes.REGISTRY);
            Map<EntityTypes<?>, MinecraftKey> map_b_new = HashBiMap.create();
            for (Map.Entry<EntityTypes<?>, MinecraftKey> entry : map_b_old.entrySet()) {
                if (entry.getKey() != entityTypes) map_b_new.put(entry.getKey(), entry.getValue());
                else map_b_new.put(newType, key);
            }

            Logger.debug("Updating RegistrySimple mapC");
            Map<MinecraftKey, EntityTypes<?>> map_c = (Map<MinecraftKey, EntityTypes<?>>) simple_field_c.get(EntityTypes.REGISTRY);
            map_c.put(key, newType);

            Logger.debug("Updating EntityTypes static field");
            Field types_field = getField(entityTypes);
            types_field.setAccessible(true);
            field_modifiers.setInt(types_field, types_field.getModifiers() & ~Modifier.FINAL);
            types_field.set(null, newType);

            registry_field_b.set(registry, array_b);
            registry_field_c.set(registry, array_c);
            registry_field_d.set(registry, array_d);
            materials_field_a.set(EntityTypes.REGISTRY, registry);
            materials_field_b.set(EntityTypes.REGISTRY, map_b_new);
            simple_field_c.set(EntityTypes.REGISTRY, map_c);

            if (spawnEggMaterial != null) {
                Logger.debug("Updating spawn egg reference");
                Item spawnEgg = CraftItemStack.asNMSCopy(new ItemStack(spawnEggMaterial)).getItem();
                item_field_d.set(spawnEgg, newType);
            }

            if (fishBucketMaterial != null) {
                Logger.debug("Updating fish bucket");
                Item fishBucket = CraftItemStack.asNMSCopy(new ItemStack(fishBucketMaterial)).getItem();
                field_modifiers.setInt(item_field_a, item_field_a.getModifiers() & ~Modifier.FINAL);
                item_field_a.set(fishBucket, newType);
            }
            return true;
        } catch (IllegalAccessException | InvocationTargetException | ArrayIndexOutOfBoundsException e) {
            Logger.error("Could not inject new ridable entity to registry! Restart your server to try again! "
                    + "(&e" + name + "&c)");
            e.printStackTrace();
            return false;
        }
    }

    private static Field getField(EntityTypes entityTypes) {
        for (Field field : EntityTypes.class.getDeclaredFields()) {
            try {
                if (entityTypes == field.get(null)) {
                    return field;
                }
            } catch (IllegalAccessException ignore) {
            }
        }
        throw new IllegalArgumentException("Could not get Field of " + entityTypes.getClass().getSimpleName());
    }

    public static void rebuildWorldGenMobs() {
        setPrivateFinalField(WorldGenDungeons.class, "b", new EntityTypes[]{EntityTypes.SKELETON, EntityTypes.ZOMBIE, EntityTypes.ZOMBIE, EntityTypes.SPIDER});
        setPrivateFinalField(WorldGenFeatureSwampHut.class, "b", Lists.newArrayList(new BiomeBase.BiomeMeta(EntityTypes.WITCH, 1, 1, 1)));
        setPrivateFinalField(WorldGenMonument.class, "b", Lists.newArrayList(new BiomeBase.BiomeMeta(EntityTypes.GUARDIAN, 1, 2, 4)));
        setPrivateFinalField(WorldGenNether.class, "b", Lists.newArrayList(new BiomeBase.BiomeMeta(EntityTypes.BLAZE, 10, 2, 3), new BiomeBase.BiomeMeta(EntityTypes.ZOMBIE_PIGMAN, 5, 4, 4), new BiomeBase.BiomeMeta(EntityTypes.WITHER_SKELETON, 8, 5, 5), new BiomeBase.BiomeMeta(EntityTypes.SKELETON, 2, 5, 5), new BiomeBase.BiomeMeta(EntityTypes.MAGMA_CUBE, 3, 4, 4)));
    }

    private static void setPrivateFinalField(Class clazz, String name, Object value) {
        Logger.debug("Rebuilding world gen mob features for: " + clazz.getSimpleName());
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            field_modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void rebuildBiomes() {
        Logger.info("Rebuilding biome mob lists");
        rebuildBiome("a", 0, "ocean", new net.minecraft.server.v1_13_R1.BiomeOcean());
        try {
            Field biomes_field = Biomes.class.getDeclaredField("b");
            biomes_field.setAccessible(true);
            field_modifiers.setInt(biomes_field, biomes_field.getModifiers() & ~Modifier.FINAL);
            biomes_field.set(null, Biomes.a);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        rebuildBiome("c", 1, "plains", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomePlains.class));
        rebuildBiome("d", 2, "desert", new net.minecraft.server.v1_13_R1.BiomeDesert());
        rebuildBiome("e", 3, "mountains", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomeBigHills.class));
        rebuildBiome("f", 4, "forest", new net.minecraft.server.v1_13_R1.BiomeForest());
        rebuildBiome("g", 5, "taiga", new net.minecraft.server.v1_13_R1.BiomeTaiga());
        rebuildBiome("h", 6, "swamp", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomeSwamp.class));
        rebuildBiome("i", 7, "river", new net.minecraft.server.v1_13_R1.BiomeRiver());
        rebuildBiome("j", 8, "nether", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomeHell.class));
        rebuildBiome("k", 9, "the_end", new net.minecraft.server.v1_13_R1.BiomeTheEnd());
        rebuildBiome("l", 10, "frozen_ocean", new net.minecraft.server.v1_13_R1.BiomeFrozenOcean());
        rebuildBiome("m", 11, "frozen_river", new net.minecraft.server.v1_13_R1.BiomeFrozenRiver());
        rebuildBiome("n", 12, "snowy_tundra", new net.minecraft.server.v1_13_R1.BiomeIcePlains());
        rebuildBiome("o", 13, "snowy_mountains", new net.minecraft.server.v1_13_R1.BiomeIceMountains());
        rebuildBiome("p", 14, "mushroom_fields", new net.minecraft.server.v1_13_R1.BiomeMushrooms());
        rebuildBiome("q", 15, "mushroom_field_shore", new net.minecraft.server.v1_13_R1.BiomeMushroomIslandShore());
        rebuildBiome("r", 16, "beach", new net.minecraft.server.v1_13_R1.BiomeBeach());
        rebuildBiome("s", 17, "desert_hills", new net.minecraft.server.v1_13_R1.BiomeDesertHills());
        rebuildBiome("t", 18, "wooded_hills", new net.minecraft.server.v1_13_R1.BiomeForestHills());
        rebuildBiome("u", 19, "taiga_hills", new net.minecraft.server.v1_13_R1.BiomeTaigaHills());
        rebuildBiome("v", 20, "mountain_edge", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomeExtremeHillsEdge.class));
        rebuildBiome("w", 21, "jungle", new net.minecraft.server.v1_13_R1.BiomeJungle());
        rebuildBiome("x", 22, "jungle_hills", new net.minecraft.server.v1_13_R1.BiomeJungleHills());
        rebuildBiome("y", 23, "jungle_edge", new net.minecraft.server.v1_13_R1.BiomeJungleEdge());
        rebuildBiome("z", 24, "deep_ocean", new net.minecraft.server.v1_13_R1.BiomeDeepOcean());
        rebuildBiome("A", 25, "stone_shore", new net.minecraft.server.v1_13_R1.BiomeStoneBeach());
        rebuildBiome("B", 26, "snowy_beach", new net.minecraft.server.v1_13_R1.BiomeColdBeach());
        rebuildBiome("C", 27, "birch_forest", new net.minecraft.server.v1_13_R1.BiomeBirchForest());
        rebuildBiome("D", 28, "birch_forest_hills", new net.minecraft.server.v1_13_R1.BiomeBirchForestHills());
        rebuildBiome("E", 29, "dark_forest", new net.minecraft.server.v1_13_R1.BiomeRoofedForest());
        rebuildBiome("F", 30, "snowy_taiga", new net.minecraft.server.v1_13_R1.BiomeColdTaiga());
        rebuildBiome("G", 31, "snowy_taiga_hills", new net.minecraft.server.v1_13_R1.BiomeColdTaigaHills());
        rebuildBiome("H", 32, "giant_tree_taiga", new net.minecraft.server.v1_13_R1.BiomeMegaTaiga());
        rebuildBiome("I", 33, "giant_tree_taiga_hills", new net.minecraft.server.v1_13_R1.BiomeMegaTaigaHills());
        rebuildBiome("J", 34, "wooded_mountains", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomeExtremeHillsWithTrees.class));
        rebuildBiome("K", 35, "savanna", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomeSavanna.class));
        rebuildBiome("L", 36, "savanna_plateau", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomeSavannaPlateau.class));
        rebuildBiome("M", 37, "badlands", new net.minecraft.server.v1_13_R1.BiomeMesa());
        rebuildBiome("N", 38, "wooded_badlands_plateau", new net.minecraft.server.v1_13_R1.BiomeMesaPlataeu());
        rebuildBiome("O", 39, "badlands_plateau", new net.minecraft.server.v1_13_R1.BiomeMesaPlataeuClear());
        rebuildBiome("P", 40, "small_end_islands", new net.minecraft.server.v1_13_R1.BiomeTheEndFloatingIslands());
        rebuildBiome("Q", 41, "end_midlands", new net.minecraft.server.v1_13_R1.BiomeTheEndMediumIsland());
        rebuildBiome("R", 42, "end_highlands", new net.minecraft.server.v1_13_R1.BiomeTheEndHighIsland());
        rebuildBiome("S", 43, "end_barrens", new net.minecraft.server.v1_13_R1.BiomeTheEndBarrenIsland());
        rebuildBiome("T", 44, "warm_ocean", new net.minecraft.server.v1_13_R1.BiomeWarmOcean());
        rebuildBiome("U", 45, "lukewarm_ocean", new net.minecraft.server.v1_13_R1.BiomeLukewarmOcean());
        rebuildBiome("V", 46, "cold_ocean", new net.minecraft.server.v1_13_R1.BiomeColdOcean());
        rebuildBiome("W", 47, "deep_warm_ocean", new net.minecraft.server.v1_13_R1.BiomeWarmDeepOcean());
        rebuildBiome("X", 48, "deep_lukewarm_ocean", new net.minecraft.server.v1_13_R1.BiomeLukewarmDeepOcean());
        rebuildBiome("Y", 49, "deep_cold_ocean", new net.minecraft.server.v1_13_R1.BiomeColdDeepOcean());
        rebuildBiome("Z", 50, "deep_frozen_ocean", new net.minecraft.server.v1_13_R1.BiomeFrozenDeepOcean());
        rebuildBiome("aa", 127, "the_void", new net.minecraft.server.v1_13_R1.BiomeVoid());
        rebuildBiome("ab", 129, "sunflower_plains", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomeSunflowerPlains.class));
        rebuildBiome("ac", 130, "desert_lakes", new net.minecraft.server.v1_13_R1.BiomeDesertMutated());
        rebuildBiome("ad", 131, "gravelly_mountains", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomeExtremeHillsMutated.class));
        rebuildBiome("ae", 132, "flower_forest", new net.minecraft.server.v1_13_R1.BiomeFlowerForest());
        rebuildBiome("af", 133, "taiga_mountains", new net.minecraft.server.v1_13_R1.BiomeTaigaMutated());
        rebuildBiome("ag", 134, "swamp_hills", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomeSwamplandMutated.class));
        rebuildBiome("ah", 140, "ice_spikes", new net.minecraft.server.v1_13_R1.BiomeIcePlainsSpikes());
        rebuildBiome("ai", 149, "modified_jungle", new net.minecraft.server.v1_13_R1.BiomeJungleMutated());
        rebuildBiome("aj", 151, "modified_jungle_edge", new net.minecraft.server.v1_13_R1.BiomeJungleEdgeMutated());
        rebuildBiome("ak", 155, "tall_birch_forest", new net.minecraft.server.v1_13_R1.BiomeBirchForestMutated());
        rebuildBiome("al", 156, "tall_birch_hills", new net.minecraft.server.v1_13_R1.BiomeBirchForestHillsMutated());
        rebuildBiome("am", 157, "dark_forest_hills", new net.minecraft.server.v1_13_R1.BiomeRoofedForestMutated());
        rebuildBiome("an", 158, "snowy_taiga_mountains", new net.minecraft.server.v1_13_R1.BiomeColdTaigaMutated());
        rebuildBiome("ao", 160, "giant_spruce_taiga", new net.minecraft.server.v1_13_R1.BiomeMegaSpruceTaiga());
        rebuildBiome("ap", 161, "giant_spruce_taiga_hills", new net.minecraft.server.v1_13_R1.BiomeRedwoodTaigaHillsMutated());
        rebuildBiome("aq", 162, "modified_gravelly_mountains", createNonPublicBiome(net.minecraft.server.v1_13_R1.BiomeExtremeHillsWithTreesMutated.class));
        rebuildBiome("ar", 163, "shattered_savanna", new net.minecraft.server.v1_13_R1.BiomeSavannaMutated());
        rebuildBiome("as", 164, "shattered_savanna_plateau", new net.minecraft.server.v1_13_R1.BiomeSavannaPlateauMutated());
        rebuildBiome("at", 165, "eroded_badlands", new net.minecraft.server.v1_13_R1.BiomeMesaBryce());
        rebuildBiome("au", 166, "modified_wooded_badlands_plateau", new net.minecraft.server.v1_13_R1.BiomeMesaPlateauMutated());
        rebuildBiome("av", 167, "modified_badlands_plateau", new net.minecraft.server.v1_13_R1.BiomeMesaPlateauClearMutated());
    }

    private static BiomeBase createNonPublicBiome(Class<? extends BiomeBase> clazz) {
        try {
            Constructor<? extends BiomeBase> biomebase_constructor = clazz.getDeclaredConstructor();
            biomebase_constructor.setAccessible(true);
            return biomebase_constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void rebuildBiome(String field, int id, String name, BiomeBase biome) {
        Logger.debug(" - rebuilding biome: " + name);
        try {
            biomebase_method_a.invoke(null, id, name, biome);
            Field biomes_field = Biomes.class.getDeclaredField(field);
            biomes_field.setAccessible(true);
            field_modifiers.setInt(biomes_field, biomes_field.getModifiers() & ~Modifier.FINAL);
            biomes_field.set(null, biome);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
