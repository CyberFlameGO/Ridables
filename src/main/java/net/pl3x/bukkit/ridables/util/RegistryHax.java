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
    public static void injectNewEntityTypes(String name, String extend_from, EntityTypes.a<?> entityTypes_a) {
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(15190).findChoiceType(DataConverterTypes.n).types();
        dataTypes.put("minecraft:" + name, dataTypes.get("minecraft:" + extend_from));
        EntityTypes.a(name, entityTypes_a);
        Logger.info("Successfully injected new entity: " + Logger.ANSI_GREEN + name);
    }

    public static void injectReplacementEntityTypes(String name, EntityTypes entityTypes, EntityTypes.a entityTypes_a, Material spawnEggMaterial) {
        try {
            MinecraftKey key = new MinecraftKey(name);
            try {
                Field a = EntityTypes.a.class.getDeclaredField("a");
                a.setAccessible(true);
                Class<? extends Entity> clazz = (Class<? extends Entity>) a.get(entityTypes_a);
                // these fields are only available on Paper
                EntityTypes.clsToKeyMap.put(clazz, key);
                EntityTypes.clsToTypeMap.put(clazz, EntityType.fromName(name));
            } catch (NoSuchFieldError ignore) {
            }
            EntityTypes<?> newType = entityTypes_a.a(name);
            Field materials_a = RegistryMaterials.class.getDeclaredField("a");
            materials_a.setAccessible(true);
            RegistryID<EntityTypes<?>> registry = (RegistryID<EntityTypes<?>>) materials_a.get(EntityTypes.REGISTRY);
            int id = registry.getId(entityTypes);
            Method method_d = RegistryID.class.getDeclaredMethod("d", Object.class);
            method_d.setAccessible(true);
            int newIndex = (int) method_d.invoke(registry, newType);
            Field registry_b = RegistryID.class.getDeclaredField("b");
            registry_b.setAccessible(true);
            Object[] array_b = (Object[]) registry_b.get(registry);
            array_b[newIndex] = newType;
            int oldIndex = -1;
            for (int i = 0; i < array_b.length; i++) {
                if (array_b[i] == entityTypes) {
                    array_b[i] = null;
                    oldIndex = i;
                    break;
                }
            }
            registry_b.set(registry, array_b);
            Field registry_c = RegistryID.class.getDeclaredField("c");
            registry_c.setAccessible(true);
            int[] array_c = (int[]) registry_c.get(registry);
            array_c[oldIndex] = 0;
            array_c[newIndex] = id;
            registry_c.set(registry, array_c);
            Field registry_d = RegistryID.class.getDeclaredField("d");
            registry_d.setAccessible(true);
            Object[] array_d = (Object[]) registry_d.get(registry);
            array_d[id] = newType;
            registry_d.set(registry, array_d);
            materials_a.set(EntityTypes.REGISTRY, registry);
            Field materials_b = RegistryMaterials.class.getDeclaredField("b");
            materials_b.setAccessible(true);
            Map<EntityTypes<?>, MinecraftKey> map_b_old = (Map<EntityTypes<?>, MinecraftKey>) materials_b.get(EntityTypes.REGISTRY);
            Map<EntityTypes<?>, MinecraftKey> map_b_new = HashBiMap.create();
            for (Map.Entry<EntityTypes<?>, MinecraftKey> entry : map_b_old.entrySet()) {
                if (entry.getKey() != entityTypes) map_b_new.put(entry.getKey(), entry.getValue());
                else map_b_new.put(newType, key);
            }
            materials_b.set(EntityTypes.REGISTRY, map_b_new);
            Field simple_c = RegistrySimple.class.getDeclaredField("c");
            simple_c.setAccessible(true);
            Map<MinecraftKey, EntityTypes<?>> map_c = (Map<MinecraftKey, EntityTypes<?>>) simple_c.get(EntityTypes.REGISTRY);
            map_c.put(key, newType);
            simple_c.set(EntityTypes.REGISTRY, map_c);
            Field types_field = getField(entityTypes);
            types_field.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(types_field, types_field.getModifiers() & ~Modifier.FINAL);
            types_field.set(null, newType);
            if (spawnEggMaterial != null) {
                Item spawnEgg = CraftItemStack.asNMSCopy(new ItemStack(spawnEggMaterial)).getItem();
                Field item_d = ItemMonsterEgg.class.getDeclaredField("d");
                item_d.setAccessible(true);
                item_d.set(spawnEgg, newType);
            }
            Logger.info("Successfully injected replacement entity: " + Logger.ANSI_GREEN + name);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            Logger.error("Could not inject new ridable entity to registry!");
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
