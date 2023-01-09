package dev.realsgii2.botwtemperature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraftforge.common.ForgeConfigSpec.Builder;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public final class Config {
    private Config() {
    }

    private static ConfigValue<List<? extends List<?>>> biomeTemperatures;

    private static ConfigValue<Double> temperatureChangeIfRain;

    public static List<Triple<String, Double, Double>> biomeTemperatures() {
        List<? extends List<?>> rawTemperatures = biomeTemperatures.get();
        return rawTemperatures.stream().map(t -> Triple.of((String) t.get(0), (Double) t.get(1), (Double) t.get(2)))
                .collect(Collectors.toList());
    }

    public static Double temperatureChangeIfRain() {
        return temperatureChangeIfRain.get();
    }

    public static void init() {
        Builder commonConfig = new Builder();
        biomeTemperatures = commonConfig
                .comment("The biomes with specific temperatures.",
                        "Format: [biomeNameOrTag, dayTemperature, nightTemperature][]",
                        "  - biomeNameOrTag: A biome name or tag (with namespace) to identify a biome",

                        "  - day/nightTemperature: A number between inclusively -2 and 3, determining the temperature during said time",
                        "    - -2: Coldest  (2 points of cold resistance required)",
                        "    - -1: Cold     (1 point of cold resistance required)",
                        "    -  0: Default  (no resistance required)",
                        "    -  1: Hot      (1 point of heat resistance required)",
                        "    -  2: Hottest  (2 points of heat resistance required)",
                        "    -  3: Volcanic (fire resistance required)",
                        "Example: [",
                        "  [\"minecraft:taiga\", -1, -2],",
                        "  [\"minecraft:desert\", 1, -1],",
                        "  [\"minecraft:basalt_deltas\", 3, 3],",
                        "]",
                        "Default: []")
                // .define("biomeTemperatures", java.util.Collections.emptyList());
                .defineList("biomeTemperatures", Arrays.asList(),
                        it -> it instanceof List && ((List<?>) it).get(0) instanceof String
                                && ((List<?>) it).get(1) instanceof Double && ((List<?>) it).get(2) instanceof Double);

        temperatureChangeIfRain = commonConfig
                .comment("Added to the current temperature if it's raining.",
                        "Format: double",
                        "Example: -0.75",
                        "Default: -0.5")
                .define("temperatureChangeIfRain", -0.5);

        // commonConfig.pop();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, commonConfig.build());
    }
}
