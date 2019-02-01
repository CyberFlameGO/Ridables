package net.pl3x.bukkit.ridables;

import io.papermc.lib.PaperLib;
import net.minecraft.server.v1_13_R2.Biomes;
import net.pl3x.bukkit.ridables.command.CmdRidables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.data.DisabledReason;
import net.pl3x.bukkit.ridables.entity.RidableType;
import net.pl3x.bukkit.ridables.entity.item.CustomEnderCrystal;
import net.pl3x.bukkit.ridables.entity.projectile.CustomEvokerFangs;
import net.pl3x.bukkit.ridables.entity.projectile.CustomFireball;
import net.pl3x.bukkit.ridables.entity.projectile.CustomShulkerBullet;
import net.pl3x.bukkit.ridables.entity.projectile.CustomThrownTrident;
import net.pl3x.bukkit.ridables.entity.projectile.CustomWitherSkull;
import net.pl3x.bukkit.ridables.entity.projectile.DolphinSpit;
import net.pl3x.bukkit.ridables.entity.projectile.PhantomFlames;
import net.pl3x.bukkit.ridables.listener.ClickListener;
import net.pl3x.bukkit.ridables.listener.RidableListener;
import net.pl3x.bukkit.ridables.listener.WaterBucketListener;
import net.pl3x.bukkit.ridables.util.Logger;
import net.pl3x.bukkit.ridables.util.RegistryHax;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static net.pl3x.bukkit.ridables.data.DisabledReason.SERVER_SHUTTING_DOWN;

public class Ridables extends JavaPlugin implements Listener {
    private static Ridables instance;

    private DisabledReason disabledReason = null;

    public Ridables() {
        instance = this;
    }

    @Override
    public void onLoad() {
        Config.reload();
        Lang.reload();

        // Paper only
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
        } catch (ClassNotFoundException e) {
            disabledReason = DisabledReason.UNSUPPORTED_SERVER_TYPE;
            disabledReason.printError();
            return;
        }

        // 1.13.2 only!
        try {
            Class.forName("net.minecraft.server.v1_13_R2.Entity"); // 1.13.1 and 1.13.2
            Biomes.class.getDeclaredField("OCEAN"); // deobfuscated in 1.13.2
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            disabledReason = DisabledReason.UNSUPPORTED_SERVER_VERSION;
            disabledReason.printError();
            return;
        }

        // check for recent build of Paper
        try {
            org.spigotmc.event.entity.EntityDismountEvent.class.getDeclaredMethod("isCancellable");
        } catch (NoSuchMethodException e) {
            disabledReason = DisabledReason.SERVER_VERSION_OUTDATED;
            disabledReason.printError();
            return;
        }

        if (System.getProperty("RidablesAlreadyLoaded") != null && System.getProperty("RidablesAlreadyLoaded").equals("true")) {
            disabledReason = DisabledReason.PLUGIN_DETECTED_RELOAD;
            disabledReason.printError();
            return;
        }
        System.setProperty("RidablesAlreadyLoaded", "true");

        // setup creatures by calling something in the class
        RidableType.getRidableType(EntityType.DOLPHIN);

        // check if any entities are enabled
        if (RidableType.BY_BUKKIT_TYPE.isEmpty()) {
            disabledReason = DisabledReason.ALL_ENTITIES_DISABLED;
            disabledReason.printError();
            return;
        }

        // inject new custom entities
        RegistryHax.injectNewEntityTypes("custom_ender_crystal", "ender_crystal", CustomEnderCrystal.class, CustomEnderCrystal::new);
        RegistryHax.injectNewEntityTypes("custom_evoker_fangs", "evoker_fangs", CustomEvokerFangs.class, CustomEvokerFangs::new);
        RegistryHax.injectNewEntityTypes("custom_fireball", "large_fireball", CustomFireball.class, CustomFireball::new);
        RegistryHax.injectNewEntityTypes("custom_shulker_bullet", "shulker_bullet", CustomShulkerBullet.class, CustomShulkerBullet::new);
        RegistryHax.injectNewEntityTypes("custom_trident", "trident", CustomThrownTrident.class, CustomThrownTrident::new);
        RegistryHax.injectNewEntityTypes("custom_wither_skull", "wither_skull", CustomWitherSkull.class, CustomWitherSkull::new);
        RegistryHax.injectNewEntityTypes("dolphin_spit", "llama_spit", DolphinSpit.class, DolphinSpit::new);
        RegistryHax.injectNewEntityTypes("phantom_flames", "llama_spit", PhantomFlames.class, PhantomFlames::new);

        // inject new mob spawns into biomes
        RegistryHax.addMobsToBiomes();
    }

    @Override
    public void onEnable() {
        new Metrics(this).addCustomChart(new Metrics.SimplePie("server_type", () -> PaperLib.getEnvironment().getName()));

        PaperLib.suggestPaper(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlugMan")) {
            Logger.warn("PlugMan is detected to be installed!");
            com.rylinaux.plugman.PlugMan.getInstance().getIgnoredPlugins().add("Ridables");
            Logger.warn("Forcing PlugMan to ignore Ridables");
        }

        if (disabledReason != null) {
            if (disabledReason == DisabledReason.PLUGIN_DETECTED_RELOAD) {
                SERVER_SHUTTING_DOWN.printError();
                Bukkit.shutdown();
                return;
            }
            disabledReason.printError(true);
            return;
        }

        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        getServer().getPluginManager().registerEvents(new RidableListener(), this);
        getServer().getPluginManager().registerEvents(new WaterBucketListener(), this);

        getCommand("ridables").setExecutor(new CmdRidables(this));

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
     * Get the instance of this plugin
     *
     * @return Ridables instance
     */
    public static Ridables getInstance() {
        return instance;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onServerLoaded(ServerLoadEvent event) {
        // ensure all blacklisted commands are loaded
        Config.reloadCommandsList(getConfig());
    }
}
