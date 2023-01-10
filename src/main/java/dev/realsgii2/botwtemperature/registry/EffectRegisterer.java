package dev.realsgii2.botwtemperature.registry;

import javax.annotation.Nonnull;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.registries.DeferredRegister;

public class EffectRegisterer {
    public static final NamedEffect COLD_RESISTANCE = new NamedEffect("cold_resistance", EffectType.BENEFICIAL,
            8900077);

    public static final NamedEffect HEAT_RESISTANCE = new NamedEffect("cold_resistance", EffectType.BENEFICIAL,
            14258530);

    public static final NamedEffect ICEBREAKER = new NamedEffect("icebreaker", EffectType.BENEFICIAL, 4965370);

    private static NamedEffect[] TO_REGISTER = new NamedEffect[] {
            COLD_RESISTANCE,
            HEAT_RESISTANCE,
            ICEBREAKER
    };

    public static void register(DeferredRegister<Effect> registerer) {
        for (NamedEffect item : TO_REGISTER)
            registerer.register(item.name, () -> item);
    }

    private static class NamedEffect extends Effect {
        public final String name;

        public NamedEffect(String name, EffectType type, int color) {
            super(type, color);
            this.name = name;
        }

        @Nonnull
        public String getName() {
            return "effect." + name;
        }

        @SuppressWarnings("unused")
        public boolean isInstant() {
            return false;
        }
    }
}
