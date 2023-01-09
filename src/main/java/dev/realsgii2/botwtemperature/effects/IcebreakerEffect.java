package dev.realsgii2.botwtemperature.effects;

import javax.annotation.Nonnull;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class IcebreakerEffect extends Effect {
    public IcebreakerEffect() {
        super(EffectType.BENEFICIAL, 4965370);
    }

    @Nonnull
    public String getName() {
        return "effect.icebreaker";
    }

    public boolean isInstance() {
        return false;
    }
}
