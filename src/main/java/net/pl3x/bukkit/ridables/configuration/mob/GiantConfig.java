package net.pl3x.bukkit.ridables.configuration.mob;

import net.pl3x.bukkit.ridables.configuration.MobConfig;
import net.pl3x.bukkit.ridables.data.BiomeData;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GiantConfig extends MobConfig {
    public float SPEED = 1.0F;
    public float JUMP_POWER = 1.0F;
    public float STEP_HEIGHT = 3.0F;
    public boolean RIDABLE_IN_WATER = true;

    public boolean AI_ENABLED = true;
    public float AI_HEALTH = 100.0F;
    public float AI_SPEED = 0.5F;
    public float AI_FOLLOW_RANGE = 32.0F;
    public boolean AI_HOSTILE = true;
    public float AI_MELEE_DAMAGE = 5.0F;

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

    public GiantConfig() {
        super("giant.yml");
        reload();
    }

    @Override
    public void reload() {
        super.reload();

        if (firstLoad) {
            firstLoad = false;

            addDefault("speed", SPEED);
            addDefault("jump-power", JUMP_POWER);
            addDefault("step-height", STEP_HEIGHT);
            addDefault("ride-in-water", RIDABLE_IN_WATER);

            addDefault("ai.enabled", AI_ENABLED);
            addDefault("ai.health", AI_HEALTH);
            addDefault("ai.speed", AI_SPEED);
            addDefault("ai.follow", AI_FOLLOW_RANGE);
            addDefault("ai.hostile", AI_HOSTILE);
            addDefault("ai.attack", AI_MELEE_DAMAGE);

            addDefault("spawn.natural", SPAWN_NATURALLY);
            addDefault("spawn.max-light", SPAWN_LIGHT_LEVEL);

            SPAWN_BIOMES.forEach(data -> {
                addDefault("spawn.biomes." + data.getBiome() + ".weight", data.getWeight());
                addDefault("spawn.biomes." + data.getBiome() + ".group-min", data.getMin());
                addDefault("spawn.biomes." + data.getBiome() + ".group-max", data.getMax());
            });

            save();
        }

        SPEED = (float) getDouble("speed");
        JUMP_POWER = (float) getDouble("jump-power", 0.5D);
        STEP_HEIGHT = (float) getDouble("step-height", 0.6D);
        RIDABLE_IN_WATER = getBoolean("ride-in-water", true);

        AI_ENABLED = getBoolean("ai.enabled", true);
        AI_HEALTH = (float) getDouble("ai.health", 100D);
        AI_SPEED = (float) getDouble("ai.speed", 0.3D);
        AI_FOLLOW_RANGE = (float) getDouble("ai.follow", 32.0D);
        AI_HOSTILE = getBoolean("ai.hostile", true);
        AI_MELEE_DAMAGE = (float) getDouble("ai.attack", 5.0D);

        SPAWN_NATURALLY = getBoolean("spawn.natural", false);
        SPAWN_LIGHT_LEVEL = (int) getDouble("spawn.max-light", 8);

        SPAWN_BIOMES.clear();
        ConfigurationSection section = getConfigurationSection("spawn.biomes");
        section.getKeys(false).forEach(biomeName ->
                SPAWN_BIOMES.add(new BiomeData(biomeName,
                        section.getInt(biomeName + ".weight"),
                        section.getInt(biomeName + ".group-min"),
                        section.getInt(biomeName + ".group-max")))
        );
    }
}
