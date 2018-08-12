package net.pl3x.bukkit.ridables.listener;

import net.pl3x.bukkit.ridables.Logger;
import net.pl3x.bukkit.ridables.Ridables;
import net.pl3x.bukkit.ridables.configuration.Config;
import net.pl3x.bukkit.ridables.configuration.Lang;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.inventivetalent.update.spiget.SpigetUpdate;
import org.inventivetalent.update.spiget.UpdateCallback;
import org.inventivetalent.update.spiget.comparator.VersionComparator;

public class UpdateListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        checkForUpdate(event.getPlayer());
    }

    /**
     * Check if a plugin update exists and inform the console if one is found
     */
    public static void checkForUpdate() {
        checkForUpdate(null);
    }

    /**
     * Check if a plugin update exists and inform the player if one is found
     * <p>
     * If no player is specified the notification will go to console
     *
     * @param player Player to notify if an update is found
     */
    public static void checkForUpdate(Player player) {
        if (!Config.CHECK_FOR_UPDATES) {
            return; // update checker disabled
        }

        if (player != null && !player.hasPermission("command.ridables")) {
            return; // player doesnt have permission
        }

        final int id = 58985; // spigot resource id
        SpigetUpdate updater = new SpigetUpdate(Ridables.getInstance(), id);
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
}
