package net.pl3x.bukkit.ridables.data;

import net.minecraft.server.v1_13_R1.EntityLiving;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.HashMap;

public class Creatures extends HashMap<EntityType, Class<? extends EntityLiving>> {
    public boolean isEnabled(Entity entity) {
        return entity != null && containsKey(entity.getType());
    }

    public void putCreature(EntityType entityType, Class<? extends EntityLiving> entityLiving) {
        put(entityType, entityLiving);
    }

    public Class<? extends EntityLiving> getCreature(EntityType entityType) {
        return get(entityType);
    }

    public boolean spawn(EntityType entityType, Location loc) {

        try {
            EntityLiving nmsCreature = getCreature(entityType).newInstance();
            nmsCreature.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            nmsCreature.world.addEntity(nmsCreature);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
