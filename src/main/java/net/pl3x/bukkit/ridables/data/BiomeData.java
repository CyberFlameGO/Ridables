package net.pl3x.bukkit.ridables.data;

public class BiomeData {
    private final String biome;
    private final int weight;
    private final int min_group;
    private final int max_group;

    public BiomeData(String biome, int weight, int min_group, int max_group) {
        this.biome = biome;
        this.weight = weight;
        this.min_group = min_group;
        this.max_group = max_group;
    }

    public String getBiome() {
        return biome;
    }

    public int getWeight() {
        return weight;
    }

    public int getMin() {
        return min_group;
    }

    public int getMax() {
        return max_group;
    }
}
