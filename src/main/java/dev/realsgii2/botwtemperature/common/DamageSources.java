package dev.realsgii2.botwtemperature.common;

import net.minecraft.util.DamageSource;

public final class DamageSources {
    // Flame also exists for when the player is in the nether
    // We just set the player on fire instead

    public static final DamageSource COLD = new DamageSource("cold")
            .bypassArmor()
            .bypassMagic();

    public static final DamageSource HEAT = new DamageSource("heat")
            .bypassArmor()
            .bypassMagic();
}
