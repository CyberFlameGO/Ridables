package net.pl3x.bukkit.ridables;

import net.minecraft.server.v1_13_R2.EntityThrownTrident;
import net.pl3x.bukkit.ridables.bstats.Metrics;
import net.pl3x.bukkit.ridables.command.CmdRidables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.data.ServerType;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.projectile.EntityCustomFireball;
import net.pl3x.bukkit.ridables.entity.projectile.EntityCustomShulkerBullet;
import net.pl3x.bukkit.ridables.entity.projectile.EntityCustomTrident;
import net.pl3x.bukkit.ridables.entity.projectile.EntityDolphinSpit;
import net.pl3x.bukkit.ridables.entity.projectile.EntityPhantomFlames;
import net.pl3x.bukkit.ridables.entity.projectile.EntitySafeWitherSkull;
import net.pl3x.bukkit.ridables.listener.ClickListener;
import net.pl3x.bukkit.ridables.listener.ExplosionListener;
import net.pl3x.bukkit.ridables.listener.RideListener;
import net.pl3x.bukkit.ridables.listener.UpdateListener;
import net.pl3x.bukkit.ridables.listener.WaterBucketListener;
import net.pl3x.bukkit.ridables.util.Logger;
import net.pl3x.bukkit.ridables.util.RegistryHax;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public class Ridables extends JavaPlugin {
    private static Ridables instance;

    private boolean disabled = false;
    private final ServerType serverType;

    public Ridables() {
        instance = this;

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

        // 1.13.1 only!
        try {
            Class.forName("net.minecraft.server.v1_13_R2.Entity");
        } catch (ClassNotFoundException e) {
            Logger.error("############################################");
            Logger.error("#                                          #");
            Logger.error("#       This server is unsupported!        #");
            Logger.error("#    Only 1.13.1 servers are supported!    #");
            Logger.error("# Download Ridables v2.35 for 1.13 support #");
            Logger.error("#                                          #");
            Logger.error("############################################");
            disabled = true;
            return;
        }

        // setup creatures by calling something in the class
        RidableType.getRidableType(EntityType.DOLPHIN);

        // inject new entities
        RegistryHax.injectNewEntityTypes("custom_fireball", "large_fireball", EntityCustomFireball.class, EntityCustomFireball::new);
        RegistryHax.injectNewEntityTypes("custom_trident", "trident", EntityCustomTrident.class, EntityCustomTrident::new);
        RegistryHax.injectNewEntityTypes("custom_shulker_bullet", "shulker_bullet", EntityCustomShulkerBullet.class, EntityCustomShulkerBullet::new);
        RegistryHax.injectNewEntityTypes("dolphin_spit", "llama_spit", EntityDolphinSpit.class, EntityDolphinSpit::new);
        RegistryHax.injectNewEntityTypes("phantom_flames", "llama_spit", EntityPhantomFlames.class, EntityPhantomFlames::new);
        RegistryHax.injectNewEntityTypes("safe_wither_skull", "wither_skull", EntitySafeWitherSkull.class, EntitySafeWitherSkull::new);

        // Fix worldgen mob features
        RegistryHax.rebuildWorldGenMobs();

        // Fix biome's mobs
        RegistryHax.rebuildBiomes();
    }

    @Override
    public void onEnable() {
        new Metrics(this).addCustomChart(new Metrics.SimplePie("server_type", () -> serverType.name));

        UpdateListener.checkForUpdate();

        if (disabled) {
            Logger.error("##########################################");
            Logger.error("#                                        #");
            Logger.error("#    Plugin is now disabling itself!     #");
            Logger.error("# Scroll up in the log to see more info! #");
            Logger.error("#                                        #");
            Logger.error("##########################################");
            return;
        }

        getServer().getPluginManager().registerEvents(new UpdateListener(), this);
        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        getServer().getPluginManager().registerEvents(new ExplosionListener(), this);
        getServer().getPluginManager().registerEvents(new RideListener(this), this);
        getServer().getPluginManager().registerEvents(new WaterBucketListener(this), this);

        getCommand("ridables").setExecutor(new CmdRidables(this));

        Logger.info("Finished enabling");
    }

    @Override
    public void onDisable() {
        Logger.info("Finished disabling");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang.send(sender, "&cThis plugin is currently disabled!");
        Lang.send(sender, "&cCheck your startup log to find out why.");
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
}
