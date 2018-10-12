package net.pl3x.bukkit.ridables.entity.ai.sheep;

import net.minecraft.server.v1_13_R2.PathfinderGoalEatTile;
import net.pl3x.bukkit.ridables.entity.RidableSheep;

public class AISheepEatGrass extends PathfinderGoalEatTile {
    private final RidableSheep sheep;

    public AISheepEatGrass(RidableSheep sheep) {
        super(sheep);
        this.sheep = sheep;
    }

    // shouldExecute
    public boolean a() {
        return sheep.getRider() == null && super.a();
    }

    // shouldContinueExecuting
    public boolean b() {
        return sheep.getRider() == null && super.b();
    }
}
