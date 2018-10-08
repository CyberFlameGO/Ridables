package net.pl3x.bukkit.ridables.util;

import co.aikar.timings.lib.MCTiming;
import co.aikar.timings.lib.TimingManager;
import net.pl3x.bukkit.ridables.Ridables;

public class Timings {
    public final MCTiming chickenLayEgg;

    public Timings(Ridables plugin) {
        TimingManager timings = TimingManager.of(plugin);

        chickenLayEgg = timings.of("Chicken Lay Egg");
    }
}
