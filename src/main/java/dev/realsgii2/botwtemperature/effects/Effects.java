package dev.realsgii2.botwtemperature.effects;

import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.realsgii2.botwtemperature.BotWTemperature.MODID;

public class Effects {
        public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, MODID);

        public static final RegistryObject<Effect> COLD_RESISTANCE_EFFECT = EFFECTS.register("cold_resistance",
                        ColdResistanceEffect::new);

        public static final RegistryObject<Effect> HEAT_RESISTANCE_EFFECT = EFFECTS.register("heat_resistance",
                        HeatResistanceEffect::new);

        public static final RegistryObject<Effect> ICEBREAKER_EFFECT = EFFECTS.register("icebreaker",
                        IcebreakerEffect::new);
}
