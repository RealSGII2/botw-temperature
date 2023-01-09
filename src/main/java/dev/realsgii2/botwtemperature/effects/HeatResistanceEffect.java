package dev.realsgii2.botwtemperature.effects;

import javax.annotation.Nonnull;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class HeatResistanceEffect extends Effect {
    public HeatResistanceEffect() {
        super(EffectType.BENEFICIAL, 14258530);
    }

    @Nonnull
    public String getName() {
        return "effect.heat_resistance";
    }

    public boolean isInstance() {
        return false;
    }
}
