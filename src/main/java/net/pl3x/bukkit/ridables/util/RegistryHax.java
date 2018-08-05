package net.pl3x.bukkit.ridables.util;

import com.google.common.collect.HashBiMap;
import com.mojang.datafixers.types.Type;
import net.minecraft.server.v1_13_R1.DataConverterRegistry;
import net.minecraft.server.v1_13_R1.DataConverterTypes;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityTypes;
import net.minecraft.server.v1_13_R1.Item;
import net.minecraft.server.v1_13_R1.ItemMonsterEgg;
import net.minecraft.server.v1_13_R1.MinecraftKey;
import net.minecraft.server.v1_13_R1.RegistryID;
import net.minecraft.server.v1_13_R1.RegistryMaterials;
import net.minecraft.server.v1_13_R1.RegistrySimple;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

@SuppressWarnings("unchecked")
public class RegistryHax {
    private static Field a;
    private static Field materials_a;
    private static Field registry_a;
    private static Field registry_b;
    private static Field registry_c;
    private static Field registry_d;
    private static Field materials_b;
    private static Field simple_c;
    private static Field modifiers;
    private static Field item_d;
    private static Method method_d;

    static {
        try {
            a = EntityTypes.a.class.getDeclaredField("a");
            a.setAccessible(true);
            materials_a = RegistryMaterials.class.getDeclaredField("a");
            materials_a.setAccessible(true);
            registry_a = RegistryID.class.getDeclaredField("a");
            registry_a.setAccessible(true);
            registry_b = RegistryID.class.getDeclaredField("b");
            registry_b.setAccessible(true);
            registry_c = RegistryID.class.getDeclaredField("c");
            registry_c.setAccessible(true);
            registry_d = RegistryID.class.getDeclaredField("d");
            registry_d.setAccessible(true);
            materials_b = RegistryMaterials.class.getDeclaredField("b");
            materials_b.setAccessible(true);
            simple_c = RegistrySimple.class.getDeclaredField("c");
            simple_c.setAccessible(true);
            modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            item_d = ItemMonsterEgg.class.getDeclaredField("d");
            item_d.setAccessible(true);
            method_d = RegistryID.class.getDeclaredMethod("d", Object.class);
            method_d.setAccessible(true);
        } catch (NoSuchFieldException | NoSuchMethodException ignore) {
        }
    }

    public static void injectNewEntityTypes(String name, String extend_from, EntityTypes.a<?> entityTypes_a) {
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(15190).findChoiceType(DataConverterTypes.n).types();
        dataTypes.put("minecraft:" + name, dataTypes.get("minecraft:" + extend_from));
        EntityTypes.a(name, entityTypes_a);
        Logger.info("Successfully injected new entity: " + Logger.ANSI_GREEN + name);
    }

    public static void injectReplacementEntityTypes(String name, EntityTypes entityTypes, EntityTypes.a entityTypes_a, Material spawnEggMaterial) {
        try {
            MinecraftKey key = new MinecraftKey(name);
            EntityTypes<?> newType = entityTypes_a.a(name);

            RegistryID<EntityTypes<?>> registry = (RegistryID<EntityTypes<?>>) materials_a.get(EntityTypes.REGISTRY);
            int id = registry.getId(entityTypes);

            Object[] array_b = (Object[]) registry_b.get(registry);
            Object[] array_d = (Object[]) registry_d.get(registry);

            if (id < 0) {
                for (int i = 0; i < array_d.length; i++) {
                    if (array_d[i] == entityTypes) {
                        id = i;
                        break;
                    }
                }
            }

            int oldIndex = -1;
            for (int i = 0; i < array_b.length; i++) {
                if (array_b[i] == entityTypes) {
                    array_b[i] = null;
                    oldIndex = i;
                    break;
                }
            }

            if (oldIndex < 0) {
                array_b = (Object[]) registry_b.get(registry);
                for (int i = 0; i < array_b.length; i++) {
                    if (array_b[i] == entityTypes) {
                        array_b[i] = null;
                        oldIndex = i;
                        break;
                    }
                }
            }

            int newIndex = (int) method_d.invoke(registry, newType);
            array_b[newIndex] = newType;
            array_d[id] = newType;

            int[] array_c = (int[]) registry_c.get(registry);
            if (oldIndex >= 0) {
                array_c[oldIndex] = 0;
            }
            array_c[newIndex] = id;

            Map<EntityTypes<?>, MinecraftKey> map_b_old = (Map<EntityTypes<?>, MinecraftKey>) materials_b.get(EntityTypes.REGISTRY);
            Map<EntityTypes<?>, MinecraftKey> map_b_new = HashBiMap.create();
            for (Map.Entry<EntityTypes<?>, MinecraftKey> entry : map_b_old.entrySet()) {
                if (entry.getKey() != entityTypes) map_b_new.put(entry.getKey(), entry.getValue());
                else map_b_new.put(newType, key);
            }

            Map<MinecraftKey, EntityTypes<?>> map_c = (Map<MinecraftKey, EntityTypes<?>>) simple_c.get(EntityTypes.REGISTRY);
            map_c.put(key, newType);

            Field types_field = getField(entityTypes);
            types_field.setAccessible(true);
            modifiers.setInt(types_field, types_field.getModifiers() & ~Modifier.FINAL);
            types_field.set(null, newType);

            registry_b.set(registry, array_b);
            registry_c.set(registry, array_c);
            registry_d.set(registry, array_d);
            materials_a.set(EntityTypes.REGISTRY, registry);
            materials_b.set(EntityTypes.REGISTRY, map_b_new);
            simple_c.set(EntityTypes.REGISTRY, map_c);

            try {
                a.setAccessible(true);
                Class<? extends Entity> clazz = (Class<? extends Entity>) a.get(entityTypes_a);
                // these fields are only available on Paper
                EntityTypes.clsToKeyMap.put(clazz, key);
                EntityTypes.clsToTypeMap.put(clazz, EntityType.fromName(name));
            } catch (NoSuchFieldError ignore) {
            }

            if (spawnEggMaterial != null) {
                Item spawnEgg = CraftItemStack.asNMSCopy(new ItemStack(spawnEggMaterial)).getItem();
                item_d.set(spawnEgg, newType);
            }
            Logger.info("Successfully injected replacement entity: " + Logger.ANSI_GREEN + name);
        } catch (IllegalAccessException | InvocationTargetException | ArrayIndexOutOfBoundsException e) {
            Logger.error("Could not inject new ridable entity to registry! Restart your server to try again! "
                    + "(" + Logger.ANSI_YELLOW + name + Logger.ANSI_RED + ")");
            e.printStackTrace();
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
}
