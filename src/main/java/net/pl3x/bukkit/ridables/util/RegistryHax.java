package net.pl3x.bukkit.ridables.util;

import com.mojang.datafixers.types.Type;
import net.minecraft.server.v1_13_R2.DataConverterRegistry;
import net.minecraft.server.v1_13_R2.DataConverterTypes;
import net.minecraft.server.v1_13_R2.Entity;
import net.minecraft.server.v1_13_R2.EntityTypes;
import net.minecraft.server.v1_13_R2.IRegistry;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import net.minecraft.server.v1_13_R2.World;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

public class RegistryHax {
    private static Field entityClass;
    private static Field entityFunction;

    static {
        try {
            entityClass = EntityTypes.class.getDeclaredField("aS");
            entityClass.setAccessible(true);
            entityFunction = EntityTypes.class.getDeclaredField("aT");
            entityFunction.setAccessible(true);
        } catch (NoSuchFieldException ignore) {
        }
    }

    public static void injectNewEntityTypes(String name, String extend_from, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function) {
        Map<Object, Type<?>> dataTypes = (Map<Object, Type<?>>) DataConverterRegistry.a().getSchema(15190).findChoiceType(DataConverterTypes.n).types();
        dataTypes.put("minecraft:" + name, dataTypes.get("minecraft:" + extend_from));
        EntityTypes.a(name, EntityTypes.a.a(clazz, function));
        Logger.info("Successfully injected new entity: &a" + name);
    }

    public static boolean injectReplacementEntityTypes(EntityTypes entityTypes, Class<? extends Entity> clazz, Function<? super World, ? extends Entity> function) {
        MinecraftKey key = IRegistry.ENTITY_TYPE.getKey(entityTypes);
        try {
            entityClass.set(entityTypes, clazz);
            entityFunction.set(entityTypes, function);
        } catch (IllegalAccessException ignore) {
            return false;
        }
        IRegistry.ENTITY_TYPE.a(IRegistry.ENTITY_TYPE.a(entityTypes), key, entityTypes);
        return true;
    }
}
