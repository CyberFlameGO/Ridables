package net.pl3x.bukkit.ridables;

import net.minecraft.server.v1_13_R1.EntityTypes;
import net.pl3x.bukkit.ridables.command.CmdRidables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.data.Buckets;
import net.pl3x.bukkit.ridables.data.Creatures;
import net.pl3x.bukkit.ridables.entity.EntityDolphinSpit;
import net.pl3x.bukkit.ridables.entity.EntityRidableChicken;
import net.pl3x.bukkit.ridables.entity.EntityRidableCow;
import net.pl3x.bukkit.ridables.entity.EntityRidableDolphin;
import net.pl3x.bukkit.ridables.entity.EntityRidableEnderDragon;
import net.pl3x.bukkit.ridables.entity.EntityRidableLlama;
import net.pl3x.bukkit.ridables.entity.EntityRidableMushroomCow;
import net.pl3x.bukkit.ridables.entity.EntityRidableOcelot;
import net.pl3x.bukkit.ridables.entity.EntityRidablePhantom;
import net.pl3x.bukkit.ridables.entity.EntityRidablePolarBear;
import net.pl3x.bukkit.ridables.entity.EntityRidableSheep;
import net.pl3x.bukkit.ridables.entity.EntityRidableTurtle;
import net.pl3x.bukkit.ridables.entity.EntityRidableWolf;
import net.pl3x.bukkit.ridables.listener.DismountListener;
import net.pl3x.bukkit.ridables.listener.RideListener;
import net.pl3x.bukkit.ridables.listener.WaterBucketListener;
import net.pl3x.bukkit.ridables.util.Logger;
import net.pl3x.bukkit.ridables.util.RegistryHax;
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

        // setup chicken
        if (Config.CHICKEN_ENABLED) {
            RegistryHax.injectReplacementEntityTypes("chicken", EntityTypes.CHICKEN,
                    EntityTypes.a.a(EntityRidableChicken.class, EntityRidableChicken::new),
                    Material.CHICKEN_SPAWN_EGG);
            creatures.putCreature(EntityType.CHICKEN, EntityRidableChicken.class);
        } else {
            Logger.info("Chicken disabled. Skipping..");
        }

        // setup cow
        if (Config.COW_ENABLED) {
            RegistryHax.injectReplacementEntityTypes("cow", EntityTypes.COW,
                    EntityTypes.a.a(EntityRidableCow.class, EntityRidableCow::new),
                    Material.COW_SPAWN_EGG);
            creatures.putCreature(EntityType.COW, EntityRidableCow.class);
        } else {
            Logger.info("Cow disabled. Skipping..");
        }

        // setup dolphin
        if (Config.DOLPHIN_ENABLED) {
            if (serverType != ServerType.CRAFTBUKKIT) {
                RegistryHax.injectNewEntityTypes("dolphin_spit", "llama_spit",
                        EntityTypes.a.a(EntityDolphinSpit.class, EntityDolphinSpit::new));
                RegistryHax.injectReplacementEntityTypes("dolphin", EntityTypes.DOLPHIN,
                        EntityTypes.a.a(EntityRidableDolphin.class, EntityRidableDolphin::new),
                        Material.DOLPHIN_SPAWN_EGG);
                creatures.putCreature(EntityType.DOLPHIN, EntityRidableDolphin.class);
                buckets.createBucket(EntityType.DOLPHIN);
            } else {
                Logger.error("Dolphin cannot be enabled on CraftBukkit servers!");
            }
        } else {
            Logger.info("Dolphin disabled. Skipping..");
        }

        // setup ender_dragon
        if (Config.DRAGON_ENABLED) {
            RegistryHax.injectReplacementEntityTypes("ender_dragon", EntityTypes.ENDER_DRAGON,
                    EntityTypes.a.a(EntityRidableEnderDragon.class, EntityRidableEnderDragon::new),
                    null);
            creatures.putCreature(EntityType.ENDER_DRAGON, EntityRidableEnderDragon.class);
        } else {
            Logger.info("Dragon disabled. Skipping..");
        }

        // setup llama
        if (Config.LLAMA_ENABLED) {
            RegistryHax.injectReplacementEntityTypes("llama", EntityTypes.LLAMA,
                    EntityTypes.a.a(EntityRidableLlama.class, EntityRidableLlama::new),
                    Material.LLAMA_SPAWN_EGG);
            creatures.putCreature(EntityType.LLAMA, EntityRidableLlama.class);
        } else {
            Logger.info("Llama disabled. Skipping..");
        }

        // setup mooshroom
        if (Config.MOOSHROOM_ENABLED) {
            RegistryHax.injectReplacementEntityTypes("mooshroom", EntityTypes.MOOSHROOM,
                    EntityTypes.a.a(EntityRidableMushroomCow.class, EntityRidableMushroomCow::new),
                    Material.MOOSHROOM_SPAWN_EGG);
            creatures.putCreature(EntityType.MUSHROOM_COW, EntityRidableMushroomCow.class);
        } else {
            Logger.info("Mooshroom disabled. Skipping..");
        }

        // setup ocelot
        if (Config.OCELOT_ENABLED) {
            RegistryHax.injectReplacementEntityTypes("ocelot", EntityTypes.OCELOT,
                    EntityTypes.a.a(EntityRidableOcelot.class, EntityRidableOcelot::new),
                    Material.OCELOT_SPAWN_EGG);
            creatures.putCreature(EntityType.OCELOT, EntityRidableOcelot.class);
        } else {
            Logger.info("Ocelot disabled. Skipping..");
        }

        // setup phantom
        if (Config.PHANTOM_ENABLED) {
            RegistryHax.injectReplacementEntityTypes("phantom", EntityTypes.PHANTOM,
                    EntityTypes.a.a(EntityRidablePhantom.class, EntityRidablePhantom::new),
                    Material.PHANTOM_SPAWN_EGG);
            creatures.putCreature(EntityType.PHANTOM, EntityRidablePhantom.class);
        } else {
            Logger.info("Phantom disabled. Skipping..");
        }

        // setup polar bear
        if (Config.POLAR_BEAR_ENABLED) {
            RegistryHax.injectReplacementEntityTypes("polar_bear", EntityTypes.POLAR_BEAR,
                    EntityTypes.a.a(EntityRidablePolarBear.class, EntityRidablePolarBear::new),
                    Material.POLAR_BEAR_SPAWN_EGG);
            creatures.putCreature(EntityType.POLAR_BEAR, EntityRidablePolarBear.class);
        } else {
            Logger.info("Polar Bear disabled. Skipping..");
        }

        // setup sheep
        if (Config.SHEEP_ENABLED) {
            RegistryHax.injectReplacementEntityTypes("sheep", EntityTypes.SHEEP,
                    EntityTypes.a.a(EntityRidableSheep.class, EntityRidableSheep::new),
                    Material.SHEEP_SPAWN_EGG);
            creatures.putCreature(EntityType.SHEEP, EntityRidableSheep.class);
        } else {
            Logger.info("Sheep disabled. Skipping..");
        }

        // setup turtle
        if (Config.TURTLE_ENABLED) {
            RegistryHax.injectReplacementEntityTypes("turtle", EntityTypes.TURTLE,
                    EntityTypes.a.a(EntityRidableTurtle.class, EntityRidableTurtle::new),
                    Material.TURTLE_SPAWN_EGG);
            creatures.putCreature(EntityType.TURTLE, EntityRidableTurtle.class);
            buckets.createBucket(EntityType.TURTLE);
        } else {
            Logger.info("Turtle disabled. Skipping..");
        }

        // setup wolf
        if (Config.WOLF_ENABLED) {
            RegistryHax.injectReplacementEntityTypes("wolf", EntityTypes.WOLF,
                    EntityTypes.a.a(EntityRidableWolf.class, EntityRidableWolf::new),
                    Material.WOLF_SPAWN_EGG);
            creatures.putCreature(EntityType.WOLF, EntityRidableWolf.class);
        } else {
            Logger.info("Wolf disabled. Skipping..");
        }
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
        CRAFTBUKKIT("CraftBukkit"),
        SPIGOT("Spigot"),
        PAPER("Paper");

        public final String name;

        ServerType(String name) {
            this.name = name;
        }
    }
}
