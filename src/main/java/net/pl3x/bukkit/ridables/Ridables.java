package net.pl3x.bukkit.ridables;

import net.pl3x.bukkit.ridables.command.CmdRidables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.data.DisabledReason;
import net.pl3x.bukkit.ridables.data.ServerType;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.projectile.CustomEvokerFangs;
import net.pl3x.bukkit.ridables.entity.projectile.CustomFireball;
import net.pl3x.bukkit.ridables.entity.projectile.CustomShulkerBullet;
import net.pl3x.bukkit.ridables.entity.projectile.CustomThrownTrident;
import net.pl3x.bukkit.ridables.entity.projectile.CustomWitherSkull;
import net.pl3x.bukkit.ridables.entity.projectile.DolphinSpit;
import net.pl3x.bukkit.ridables.entity.projectile.PhantomFlames;
import net.pl3x.bukkit.ridables.listener.ClickListener;
import net.pl3x.bukkit.ridables.listener.RidableListener;
import net.pl3x.bukkit.ridables.listener.UpdateListener;
import net.pl3x.bukkit.ridables.listener.WaterBucketListener;
import net.pl3x.bukkit.ridables.util.Logger;
import net.pl3x.bukkit.ridables.util.RegistryHax;
import net.pl3x.bukkit.ridables.util.Timings;
import org.bstats.bukkit.Metrics;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public class Ridables extends JavaPlugin {
    private static Ridables instance;

    private DisabledReason disabledReason = null;
    private final ServerType serverType;
    private final Timings timings;

    public Ridables() {
        instance = this;
        timings = new Timings(this);

        ServerType type;
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            type = ServerType.PAPER;
        } catch (Exception e) {
            try {
                Class.forName("org.spigotmc.SpigotConfig");
                type = ServerType.SPIGOT;
            } catch (Exception e2) {
                type = ServerType.CRAFTBUKKIT;
            }
        }
        serverType = type;
    }

    @Override
    public void onLoad() {
        Config.reload();
        Lang.reload();

        // TODO
        // change to Paper only (eventually)

        // 1.13.1 only!
        try {
            Class.forName("net.minecraft.server.v1_13_R2.Entity");
        } catch (ClassNotFoundException e) {
            disabledReason = DisabledReason.UNSUPPORTED_SERVER_VERSION;
            disabledReason.printError();
            return;
        }

        // setup creatures by calling something in the class
        RidableType.getRidableType(EntityType.DOLPHIN);

        // check if any entities are enabled
        if (RidableType.BY_BUKKIT_TYPE.isEmpty()) {
            disabledReason = DisabledReason.ALL_ENTITIES_DISABLED;
            disabledReason.printError();
            return;
        }

        // inject new custom entities
        RegistryHax.injectNewEntityTypes("custom_evoker_fangs", "evoker_fangs", CustomEvokerFangs.class, CustomEvokerFangs::new);
        RegistryHax.injectNewEntityTypes("custom_fireball", "large_fireball", CustomFireball.class, CustomFireball::new);
        RegistryHax.injectNewEntityTypes("custom_shulker_bullet", "shulker_bullet", CustomShulkerBullet.class, CustomShulkerBullet::new);
        RegistryHax.injectNewEntityTypes("custom_trident", "trident", CustomThrownTrident.class, CustomThrownTrident::new);
        RegistryHax.injectNewEntityTypes("custom_wither_skull", "wither_skull", CustomWitherSkull.class, CustomWitherSkull::new);
        RegistryHax.injectNewEntityTypes("dolphin_spit", "llama_spit", DolphinSpit.class, DolphinSpit::new);
        RegistryHax.injectNewEntityTypes("phantom_flames", "llama_spit", PhantomFlames.class, PhantomFlames::new);
    }

    @Override
    public void onEnable() {
        new Metrics(this).addCustomChart(new Metrics.SimplePie("server_type", () -> serverType.name));

        UpdateListener.checkForUpdate();

        if (disabledReason != null) {
            disabledReason.printError(true);
            return;
        }

        getServer().getPluginManager().registerEvents(new UpdateListener(), this);
        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        getServer().getPluginManager().registerEvents(new RidableListener(), this);
        getServer().getPluginManager().registerEvents(new WaterBucketListener(), this);

        getCommand("ridables").setExecutor(new CmdRidables(this));

        if (serverType == ServerType.PAPER) {
            try {
                World.class.getDeclaredMethod("getChunkAtAsync", Location.class);
            } catch (Exception e) {
                Logger.warn("############################################");
                Logger.warn("#                                          #");
                Logger.warn("#     Detected an old build of Paper!      #");
                Logger.warn("#                                          #");
                Logger.warn("#   Upgrading to build 302+ can severely   #");
                Logger.warn("#      help your server's performance      #");
                Logger.warn("#                                          #");
                Logger.warn("#       https://papermc.io/downloads       #");
                Logger.warn("#                                          #");
                Logger.warn("############################################");
            }
        } else {
            Logger.warn("############################################");
            Logger.warn("#                                          #");
            Logger.warn("#     Detected non-Paper server type!      #");
            Logger.warn("#                                          #");
            Logger.warn("#     Upgrading to Paper can severely      #");
            Logger.warn("#      help your server's performance      #");
            Logger.warn("#                                          #");
            Logger.warn("#       https://papermc.io/downloads       #");
            Logger.warn("#                                          #");
            Logger.warn("############################################");
        }

        Logger.info("Finished enabling");
    }

    @Override
    public void onDisable() {
        getServer().getOnlinePlayers().forEach(Entity::leaveVehicle);

        Logger.info("Finished disabling");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        disabledReason.printError(sender);
        return true;
    }

    /**
     * Gets the current server's detected type
     *
     * @return Detected server type
     */
    public ServerType getServerType() {
        return serverType;
    }

    /**
     * Get the instance of this plugin
     *
     * @return Ridables instance
     */
    public static Ridables getInstance() {
        return instance;
    }

    /**
     * Check if server environment is running Paper
     *
     * @return True if running Paper
     */
    public static boolean isPaper() {
        return instance.serverType == ServerType.PAPER;
    }

    /**
     * Check if server environment is running Spigot
     *
     * @return True if running Spigot
     */
    public static boolean isSpigot() {
        return instance.serverType == ServerType.SPIGOT;
    }

    public static Timings timings() {
        return instance.timings;
    }
}
