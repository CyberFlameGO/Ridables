package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;
import net.pl3x.bukkit.ridables.data.BiomeData;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IllusionerConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float JUMP_POWER = 0.5F;
    public float STEP_HEIGHT = 0.6F;
    public boolean RIDABLE_IN_WATER = true;

    public boolean SPAWN_NATURALLY = false;
    public int SPAWN_LIGHT_LEVEL = 8;

    public Set<BiomeData> SPAWN_BIOMES = Stream.of(
            new BiomeData("plains", 5, 1, 1),
            new BiomeData("sunflower_plains", 5, 1, 1),
            new BiomeData("desert", 5, 1, 1),
            new BiomeData("desert_hills", 5, 1, 1),
            new BiomeData("desert_lakes", 5, 1, 1),
            new BiomeData("mountains", 5, 1, 1),
            new BiomeData("gravelly_mountains", 5, 1, 1),
            new BiomeData("modified_gravelly_mountains", 5, 1, 1),
            new BiomeData("savanna", 5, 1, 1),
            new BiomeData("savanna_plateau", 5, 1, 1),
            new BiomeData("shattered_savanna", 5, 1, 1),
            new BiomeData("shattered_savanna_plateau", 5, 1, 1),
            new BiomeData("badlands", 5, 1, 1),
            new BiomeData("badlands_plateau", 5, 1, 1),
            new BiomeData("eroded_badlands", 5, 1, 1),
            new BiomeData("modified_badlands_plateau", 5, 1, 1)
    ).collect(Collectors.toSet());

    public IllusionerConfig() {
        super("illusioner.yml");
        reload();
    }

    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;

            addDefault("speed", SPEED);
            addDefault("jump-power", JUMP_POWER);
            addDefault("step-height", STEP_HEIGHT);
            addDefault("ride-in-water", RIDABLE_IN_WATER);

            addDefault("spawn.natural", SPAWN_NATURALLY);
            addDefault("spawn.max-light", SPAWN_LIGHT_LEVEL);

            if (!isSet("spawn.biomes")) {
                SPAWN_BIOMES.forEach(data -> {
                    addDefault("spawn.biomes." + data.getBiome() + ".weight", data.getWeight());
                    addDefault("spawn.biomes." + data.getBiome() + ".group-min", data.getMin());
                    addDefault("spawn.biomes." + data.getBiome() + ".group-max", data.getMax());
                });
            }

            save();
        }

        SPEED = (float) getDouble("speed");
        JUMP_POWER = (float) getDouble("jump-power", 0.5D);
        STEP_HEIGHT = (float) getDouble("step-height", 0.6D);
        RIDABLE_IN_WATER = getBoolean("ride-in-water", true);

        SPAWN_NATURALLY = getBoolean("spawn.natural", false);
        SPAWN_LIGHT_LEVEL = (int) getDouble("spawn.max-light", 8);

        SPAWN_BIOMES.clear();
        if (isSet("spawn.biomes")) {
            ConfigurationSection section = getConfigurationSection("spawn.biomes");
            if (section != null && !section.getKeys(false).isEmpty())
                section.getKeys(false).forEach(biomeName ->
                        SPAWN_BIOMES.add(new BiomeData(biomeName,
                                section.getInt(biomeName + ".weight"),
                                section.getInt(biomeName + ".group-min"),
                                section.getInt(biomeName + ".group-max")))
                );
        }
    }
}
