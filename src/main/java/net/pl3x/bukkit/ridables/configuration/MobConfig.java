package net.pl3x.bukkit.ridables.configuration;

import net.pl3x.bukkit.ridables.util.Logger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public abstract class MobConfig extends YamlConfiguration {
    private final Object saveLock = new Object();
    private final File file;
    protected boolean firstLoad = true;

    public MobConfig(String fileName) {
        file = new File(Config.getMobsDirectory(), fileName);
    }

    public void save() {
        synchronized (saveLock) {
            try {
                save(file);
            } catch (Exception ignore) {
            }
        }
    }

    public void reload() {
        synchronized (saveLock) {
            try {
                load(file);
            } catch (Exception ignore) {
            }
        }
    }

    public void addDefault(String key, Object value) {
        if (!isSet(key)) {
            Logger.debug("Adding new default to " + file.getName() + ": " + key + ": " + value.toString());
            set(key, value);
        }
    }
}
