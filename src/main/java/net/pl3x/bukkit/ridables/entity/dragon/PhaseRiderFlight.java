package net.pl3x.bukkit.ridables.entity.dragon;

import net.minecraft.server.v1_13_R1.AbstractDragonController;
import net.minecraft.server.v1_13_R1.DragonControllerPhase;
import net.minecraft.server.v1_13_R1.EntityEnderDragon;
import net.minecraft.server.v1_13_R1.IDragonController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class PhaseRiderFlight extends AbstractDragonController {
    public static final DragonControllerPhase<PhaseRiderFlight> RIDER_CONTROLLING = a(PhaseRiderFlight.class, "HoldingPattern");

    public PhaseRiderFlight(EntityEnderDragon entityEnderDragon) {
        super(entityEnderDragon);
    }

    @Override
    public DragonControllerPhase<? extends IDragonController> getControllerPhase() {
        return null;
    }

    private static DragonControllerPhase a(Class clazz, String name) {
        try {
            Field l = DragonControllerPhase.class.getDeclaredField("l");
            l.setAccessible(true);
            DragonControllerPhase<?>[] list = (DragonControllerPhase<?>[]) l.get(null);

            Class<?> phaseClass = Class.forName("net.minecraft.server.DragonControllerPhase");
            Constructor<?> cons = phaseClass.getConstructor(int.class, Class.class, String.class);

            DragonControllerPhase var2 = (DragonControllerPhase) cons.newInstance(list, clazz, name);
            list = Arrays.copyOf(list, list.length + 1);
            list[var2.b()] = var2;
            l.set(null, list);
            return var2;
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException ignore) {
        }
        return null;
    }
}
