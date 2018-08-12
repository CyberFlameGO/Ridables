package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.entity.EntityRidableSnowman;
import net.pl3x.bukkit.ridables.entity.RidableType;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnListener implements Listener {
    private final Ridables plugin;

    public SpawnListener(Ridables plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLetsBuildASnowman(CreatureSpawnEvent event) {
        if (!Config.SNOWMAN_ENABLED) {
            return; // snowman is disabled
        }

        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.BUILD_SNOWMAN) {
            return; // not building a snowman :(
        }

        if (event.getEntityType() != EntityType.SNOWMAN) {
            return; // not a snowman. what?
        }

        Entity entity = event.getEntity();
        net.minecraft.server.v1_13_R1.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        if (nmsEntity instanceof EntityRidableSnowman) {
            return; // already a ridable snowman
        }

        RidableType.getRidable(EntityType.SNOWMAN).spawn(entity.getLocation());

        // kill original snowman on next tick so the
        // ShapeDetector removes snowman blocks properly
        new BukkitRunnable() {
            @Override
            public void run() {
                entity.remove();
            }
        }.runTask(plugin);
    }
}
