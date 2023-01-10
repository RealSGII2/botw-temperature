package dev.realsgii2.botwtemperature.registry;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.registries.DeferredRegister;

public class PotionRegisterer {
	public static final int SHORT_DURATION = 9600; // 8 minutes
	public static final int LONG_DURATION = 28800; // 24 minutes

	public static final NamedPotion COLD_RESISTANCE_I_SHORT = new NamedPotion("cold_resistance_i_short",
			new EffectInstance(EffectRegisterer.COLD_RESISTANCE, SHORT_DURATION));

	public static final NamedPotion COLD_RESISTANCE_I_LONG = new NamedPotion("cold_resistance_i_long",
			new EffectInstance(EffectRegisterer.COLD_RESISTANCE, LONG_DURATION));

	public static final NamedPotion COLD_RESISTANCE_II_SHORT = new NamedPotion("cold_resistance_ii_short",
			new EffectInstance(EffectRegisterer.COLD_RESISTANCE, SHORT_DURATION, 1));

	public static final NamedPotion COLD_RESISTANCE_II_LONG = new NamedPotion("cold_resistance_ii_long",
			new EffectInstance(EffectRegisterer.COLD_RESISTANCE, LONG_DURATION, 1));

	public static final NamedPotion HEAT_RESISTANCE_I_SHORT = new NamedPotion("heat_resistance_i_short",
			new EffectInstance(EffectRegisterer.HEAT_RESISTANCE, SHORT_DURATION));

	public static final NamedPotion HEAT_RESISTANCE_I_LONG = new NamedPotion("heat_resistance_i_long",
			new EffectInstance(EffectRegisterer.HEAT_RESISTANCE, LONG_DURATION));

	public static final NamedPotion HEAT_RESISTANCE_II_SHORT = new NamedPotion("heat_resistance_ii_short",
			new EffectInstance(EffectRegisterer.HEAT_RESISTANCE, SHORT_DURATION, 1));

	public static final NamedPotion HEAT_RESISTANCE_II_LONG = new NamedPotion("heat_resistance_ii_long",
			new EffectInstance(EffectRegisterer.HEAT_RESISTANCE, LONG_DURATION, 1));

	public static final NamedPotion ICEBREAKER_I_SHORT = new NamedPotion("icebreaker_i_short",
			new EffectInstance(EffectRegisterer.ICEBREAKER, SHORT_DURATION));

	public static final NamedPotion ICEBREAKER_I_LONG = new NamedPotion("icebreaker_i_long",
			new EffectInstance(EffectRegisterer.ICEBREAKER, LONG_DURATION));

	public static final NamedPotion ICEBREAKER_II_SHORT = new NamedPotion("icebreaker_ii_short",
			new EffectInstance(EffectRegisterer.ICEBREAKER, SHORT_DURATION, 1));

	public static final NamedPotion ICEBREAKER_II_LONG = new NamedPotion("icebreaker_ii_long",
			new EffectInstance(EffectRegisterer.ICEBREAKER, LONG_DURATION, 1));

	private static NamedPotion[] TO_REGISTER = new NamedPotion[] {
			COLD_RESISTANCE_I_SHORT, COLD_RESISTANCE_I_LONG, COLD_RESISTANCE_II_SHORT,
			COLD_RESISTANCE_II_LONG,
			HEAT_RESISTANCE_I_SHORT, HEAT_RESISTANCE_I_LONG, HEAT_RESISTANCE_II_SHORT,
			HEAT_RESISTANCE_II_LONG,
			ICEBREAKER_I_SHORT, ICEBREAKER_I_LONG, ICEBREAKER_II_SHORT, ICEBREAKER_II_LONG
	};

	public static void register(DeferredRegister<Potion> registerer) {
		for (NamedPotion item : TO_REGISTER)
			registerer.register(item.name, () -> item);
	}

	private static class NamedPotion extends Potion {
		public final String name;

		public NamedPotion(String name, EffectInstance effect) {
			super(effect);
			this.name = name;
		}
	}
}
