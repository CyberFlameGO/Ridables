package net.pl3x.bukkit.ridables.entity.controller;

import net.minecraft.server.v1_13_R1.ControllerLook;
import net.minecraft.server.v1_13_R1.Entity;
import net.minecraft.server.v1_13_R1.EntityInsentient;

public class BlankLookController extends ControllerLook {
    public BlankLookController(EntityInsentient entityInsentient) {
        super(entityInsentient);
    }

    public void a(Entity var1, float var2, float var3) {
        g = var1.locZ;
        b = var2;
        c = var3;
        d = true;
    }

    public void a(double var1, double var3, double var5, float var7, float var8) {
        e = var1;
        f = var3;
        g = var5;
        b = var7;
        c = var8;
        d = true;
    }

    public void a() {
    }

    protected float a(float var1, float var2, float var3) {
        return 0;
    }

    public boolean b() {
        return d;
    }

    public double e() {
        return e;
    }

    public double f() {
        return f;
    }

    public double g() {
        return g;
    }
}
