package dev.realsgii2.botwtemperature.potions;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.realsgii2.botwtemperature.BotWTemperature.MODID;

import dev.realsgii2.botwtemperature.effects.Effects;

public class Potions {
        public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES,
                        MODID);

        public static final RegistryObject<Potion> COLD_RESISTANCE_POTION = POTIONS.register("cold_resistance",
                        () -> new Potion(new EffectInstance(Effects.COLD_RESISTANCE_EFFECT.get(), 9600)));

        public static final RegistryObject<Potion> COLD_RESISTANCE_POTION_LONG = POTIONS.register(
                        "cold_resistance_long",
                        () -> new Potion(new EffectInstance(Effects.COLD_RESISTANCE_EFFECT.get(), 9600 * 3)));

        public static final RegistryObject<Potion> COLD_RESISTANCE_POTION_II = POTIONS.register("cold_resistance_ii",
                        () -> new Potion(new EffectInstance(Effects.COLD_RESISTANCE_EFFECT.get(), 9600, 1)));

        public static final RegistryObject<Potion> COLD_RESISTANCE_POTION_II_LONG = POTIONS.register(
                        "cold_resistance_ii_long",
                        () -> new Potion(new EffectInstance(Effects.COLD_RESISTANCE_EFFECT.get(), 9600 * 3, 1)));

        public static final RegistryObject<Potion> HEAT_RESISTANCE_POTION = POTIONS.register("heat_resistance",
                        () -> new Potion(new EffectInstance(Effects.HEAT_RESISTANCE_EFFECT.get(), 9600)));

        public static final RegistryObject<Potion> HEAT_RESISTANCE_POTION_LONG = POTIONS.register(
                        "heat_resistance_long",
                        () -> new Potion(new EffectInstance(Effects.HEAT_RESISTANCE_EFFECT.get(), 9600 * 3)));

        public static final RegistryObject<Potion> HEAT_RESISTANCE_POTION_II = POTIONS.register(
                        "heat_resistance_ii",
                        () -> new Potion(new EffectInstance(Effects.HEAT_RESISTANCE_EFFECT.get(), 9600, 1)));

        public static final RegistryObject<Potion> HEAT_RESISTANCE_POTION_II_LONG = POTIONS.register(
                        "heat_resistance_ii_long",
                        () -> new Potion(new EffectInstance(Effects.HEAT_RESISTANCE_EFFECT.get(), 9600 * 3, 1)));

        public static final RegistryObject<Potion> ICEBREAKER_POTION = POTIONS.register("icebreaker",
                        () -> new Potion(new EffectInstance(Effects.ICEBREAKER_EFFECT.get(), 9600)));

        public static final RegistryObject<Potion> ICEBREAKER_POTION_LONG = POTIONS.register(
                        "icebreaker_long",
                        () -> new Potion(new EffectInstance(Effects.ICEBREAKER_EFFECT.get(), 9600 * 3)));

        public static final RegistryObject<Potion> ICEBREAKER_POTION_II = POTIONS.register(
                        "icebreaker_ii",
                        () -> new Potion(new EffectInstance(Effects.ICEBREAKER_EFFECT.get(), 9600, 1)));

        public static final RegistryObject<Potion> ICEBREAKER_POTION_II_LONG = POTIONS.register(
                        "icebreaker_ii_long",
                        () -> new Potion(new EffectInstance(Effects.ICEBREAKER_EFFECT.get(), 9600 * 3, 1)));
}
