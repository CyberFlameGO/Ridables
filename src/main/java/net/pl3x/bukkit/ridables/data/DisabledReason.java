package net.pl3x.bukkit.ridables.data;

import net.pl3x.bukkit.ridables.configuration.Lang;
import net.pl3x.bukkit.ridables.util.Logger;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public enum DisabledReason {
    UNSUPPORTED_SERVER_TYPE(
            "#                                        #"
    ),
    UNSUPPORTED_SERVER_VERSION(
            "#       This server is unsupported!        #",
            "#    Only 1.13.1 servers are supported!    #",
            "# Download Ridables v2.35 for 1.13 support #"
    ),
    ALL_ENTITIES_DISABLED(
            "#        All entities are disabled!        #",
            "#  Please follow the instructions on wiki  #",
            "#          http://git.io/ridables          #"
    );

    private List<String> reason;

    DisabledReason(String... reason) {
        this.reason = Arrays.asList(reason);
    }

    public List<String> getReason() {
        return reason;
    }

    public void printError() {
        printError(false);
    }

    public void printError(boolean disabling) {
        Logger.error("############################################");
        Logger.error("#                                          #");
        if (disabling) {
            Logger.error("#     Plugin is now disabling itself!      #");
            Logger.error("#                                          #");
        }
        reason.forEach(Logger::error);
        Logger.error("#                                          #");
        Logger.error("############################################");
    }

    public void printError(CommandSender sender) {
        Lang.send(sender, "&c############################################");
        Lang.send(sender, "&c#                                          #");
        Lang.send(sender, "&c#  Ridables plugin is currently disabled!  #");
        Lang.send(sender, "&c#                                          #");
        reason.forEach(reason -> Lang.send(sender, "&c" + reason));
        Lang.send(sender, "&c#                                          #");
        Lang.send(sender, "&c############################################");
    }
}
