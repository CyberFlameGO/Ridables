package net.pl3x.bukkit.ridables;

import net.minecraft.server.v1_13_R1.EntityTypes;
import net.pl3x.bukkit.ridables.command.CmdRidables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.data.Buckets;
import net.pl3x.bukkit.ridables.data.Creatures;
import net.pl3x.bukkit.ridables.entity.EntityDolphinSpit;
import net.pl3x.bukkit.ridables.entity.EntityRidableDolphin;
import net.pl3x.bukkit.ridables.entity.EntityRidablePhantom;
import net.pl3x.bukkit.ridables.entity.EntityRidableTurtle;
import net.pl3x.bukkit.ridables.listener.DismountListener;
import net.pl3x.bukkit.ridables.listener.RideListener;
import net.pl3x.bukkit.ridables.listener.WaterBucketListener;
import net.pl3x.bukkit.ridables.util.Logger;
import net.pl3x.bukkit.ridables.util.RegistryHax;
import org.apache.commons.lang.WordUtils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

public class Ridables extends JavaPlugin implements Listener {
    private final Creatures creatures = new Creatures();
    private final Buckets buckets = new Buckets();

    private boolean disabled = false;
    private ServerType serverType;

    @Override
    public void onLoad() {
        // config files
        Config.reload();
        Lang.reload();

        // 1.13+ only!
        try {
            // test for 1.13+ by looking for the Dolphin interface
            Class.forName("org.bukkit.entity.Dolphin");
        } catch (ClassNotFoundException e) {
            Logger.error("This server is unsupported!");
            Logger.error("Only 1.13+ servers are supported!");
            disabled = true;
            return;
        }

        // server version
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            serverType = ServerType.PAPER;
        } catch (Exception e) {
            try {
                Class.forName("org.spigotmc.SpigotConfig");
                serverType = ServerType.SPIGOT;
            } catch (Exception e2) {
                serverType = ServerType.CRAFTBUKKIT;
            }
        }

        // setup dolphin
        if (serverType != ServerType.CRAFTBUKKIT) {
            RegistryHax.injectNewEntityTypes("dolphin_spit", "llama_spit",
                    EntityTypes.a.a(EntityDolphinSpit.class, EntityDolphinSpit::new));
            RegistryHax.injectReplacementEntityTypes("dolphin", EntityTypes.DOLPHIN,
                    EntityTypes.a.a(EntityRidableDolphin.class, EntityRidableDolphin::new),
                    Material.DOLPHIN_SPAWN_EGG);
            creatures.putCreature(EntityType.DOLPHIN, EntityRidableDolphin.class);
            if (serverType == ServerType.PAPER) {
                buckets.createBucket(EntityType.DOLPHIN);
            }
        }

        // setup turtle
        RegistryHax.injectReplacementEntityTypes("turtle", EntityTypes.TURTLE,
                EntityTypes.a.a(EntityRidableTurtle.class, EntityRidableTurtle::new),
                Material.TURTLE_SPAWN_EGG);
        creatures.putCreature(EntityType.TURTLE, EntityRidableTurtle.class);
        if (serverType == ServerType.PAPER) {
            buckets.createBucket(EntityType.TURTLE);
        }

        // setup phantom
        RegistryHax.injectReplacementEntityTypes("phantom", EntityTypes.PHANTOM,
                EntityTypes.a.a(EntityRidablePhantom.class, EntityRidablePhantom::new),
                Material.PHANTOM_SPAWN_EGG);
        creatures.putCreature(EntityType.PHANTOM, EntityRidablePhantom.class);
    }

    @Override
    public void onEnable() {
        if (disabled) {
            Logger.error("Plugin is now disabling itself!");
            Logger.error("Scroll up in the log to see more info!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (serverType == ServerType.PAPER) {
            Logger.info("Paper server detected. All features enabled");
        } else if (serverType == ServerType.SPIGOT) {
            Logger.info("Spigot server detected. Some features disabled");
            Logger.info("Please upgrade to Paper to enable all features");
            Logger.info("See project page on spigotmc.org for more details");
        } else {
            Logger.info("CraftBukkit server detected. Most features disabled");
            Logger.info("Please upgrade to Spigot or Paper to enable more features");
            Logger.info("See project page on spigotmc.org for more details");
        }

        // listeners \o/
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new RideListener(this), this);
        getServer().getPluginManager().registerEvents(new WaterBucketListener(this), this);
        if (serverType != ServerType.CRAFTBUKKIT) {
            getServer().getPluginManager().registerEvents(new DismountListener(this), this);
        }

        // commands \o/ idky i'm so excited
        getCommand("ridables").setExecutor(new CmdRidables(this));

        // bStats
        new Metrics(this).addCustomChart(new Metrics.SimplePie("server_type", () -> serverType.name));

        // update checker
        checkForUpdate(null);

        Logger.info("Finished enabling");
    }

    @Override
    public void onDisable() {
        Logger.info("Finished disabling");
    }

    public Creatures creatures() {
        return creatures;
    }

    public Buckets getBuckets() {
        return buckets;
    }

    private void checkForUpdate(Player player) {
        if (!Config.CHECK_FOR_UPDATES) {
            return; // update checker disabled
        }

        if (player != null && !player.hasPermission("command.ridables")) {
            return; // player doesnt have permission
        }

        final int id = 58985; // spigot resource id
        SpigetUpdate updater = new SpigetUpdate(this, id);
        updater.setVersionComparator(VersionComparator.SEM_VER);
        updater.checkForUpdate(new UpdateCallback() {
            @Override
            public void updateAvailable(String newVersion, String downloadUrl, boolean hasDirectDownload) {
                if (player != null) {
                    Lang.send(player, "&e[&3Ridables&e]&a Update is available! v" + newVersion);
                    Lang.send(player, "&e[&3Ridables&e]&b https://spigotmc.org/resources/." + id);
                } else {
                    Logger.info("Update is available! v" + newVersion);
                    Logger.info("https://spigotmc.org/resources/." + id);
                }
            }

            @Override
            public void upToDate() {
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        checkForUpdate(event.getPlayer());
    }

    public enum ServerType {
        CRAFTBUKKIT,
        SPIGOT,
        PAPER;

        public final String name;

        ServerType() {
            this.name = WordUtils.capitalizeFully(name());
        }
    }
}
