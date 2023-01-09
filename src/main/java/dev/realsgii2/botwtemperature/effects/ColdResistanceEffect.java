package dev.realsgii2.botwtemperature.effects;

import javax.annotation.Nonnull;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class ColdResistanceEffect extends Effect {
    public ColdResistanceEffect() {
        super(EffectType.BENEFICIAL, 8900077);
    }

    @Nonnull
    public String getName() {
        return "effect.cold_resistance";
    }

    public boolean isInstance() {
        return false;
    }
}
