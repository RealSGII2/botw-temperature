package dev.realsgii2.botwtemperature.common;

import org.apache.commons.lang3.tuple.Triple;

import com.ibm.icu.impl.Pair;

import dev.realsgii2.botwtemperature.Config;
import dev.realsgii2.botwtemperature.registry.ArmorRegisterer;
import dev.realsgii2.botwtemperature.registry.EffectRegisterer;
import dev.realsgii2.botwtemperature.registry.EnchantmentRegisterer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import static dev.realsgii2.botwtemperature.TemperatureMod.LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Temperature {
    private PlayerEntity player;

    private ArrayList<ArmorItem> coldProofArmors = new ArrayList<ArmorItem>() {
        {
            add(ArmorRegisterer.SNOWQUILL_HEADRESS);
            add(ArmorRegisterer.SNOWQUILL_TUNIC);
            add(ArmorRegisterer.SNOWQUILL_TROUSERS);
            add(ArmorRegisterer.SNOWQUILL_BOOTS);
        }
    };

    public Temperature(PlayerEntity player) {
        this.player = player;
    }

    /**
     * @return Should the player receive cold damage?
     */
    public boolean shouldDamagePlayerCold() {
        double currentTemperature = getCurrentTemperature();
        return (currentTemperature != -3 && currentTemperature < -1
                && Math.abs(currentTemperature) - 1 > getColdProtectionLevel());
    }

    /**
     * Do not use as a condition whether to render fire. Use
     * {@link #shouldShowFireIndicator()} instead.
     * 
     * @return Should the player receive heat damage?
     */
    public boolean shouldDamagePlayerHeat() {
        double currentTemperature = getCurrentTemperature();
        return (currentTemperature != 3 && currentTemperature > 1 && currentTemperature - 1 > getHeatProtectionLevel());
    }

    /**
     * @return Should the player be set on fire due to extreme heat?
     */
    public boolean shouldSetPlayerOnFire() {
        return (getCurrentTemperature() == 3 && !getHasFlameBreaker());
    }

    /**
     * @return Should the player receive freezing damage?
     */
    public boolean shouldFreezePlayer() {
        return (getCurrentTemperature() == -3 && !getHasIcebreaker());
    }

    /**
     * @return Should the fire indicator be shown?
     */
    public boolean shouldShowFireIndicator() {
        return (shouldSetPlayerOnFire() || player.isOnFire());
    }

    /**
     * Gets the current temperature as a number in between inclusively -2 and 2, or
     * exactly
     * -3 or 3.
     * 
     * @return The current temperature.
     */
    public double getCurrentTemperature() {
        if (player.level.dimension() == World.NETHER)
            return 3;

        Biome biome = player.level.getBiome(player.blockPosition());
        double temperature = getTemperatureOfBiome(biome);

        if (player.level.isRaining()) {
            if (player.level.getBiome(player.blockPosition()).getPrecipitation() == Biome.RainType.RAIN)
                temperature += Config.temperatureChangeIfRain();
            else
                temperature += Config.temperatureChangeIfSnow();
        }

        Pair<Biome, Double> closestBiomePair = getClosestDifferentBiomePair(biome);

        if (closestBiomePair != null) {
            double closestBiomeTemperature = getTemperatureOfBiome(closestBiomePair.first);
            Double blendAmount = 0.5 + 0.5 * (closestBiomePair.second / 32);
            temperature = Utils.blend(closestBiomeTemperature, temperature, blendAmount, 0, 1);
        }

        for (BlockPos blockPos : Utils.getNearbyPositions(player.blockPosition(), 16, 1)) {
            BlockState state = player.level.getBlockState(blockPos);
            Double magnitude = Math.abs(player.blockPosition().distSqr(blockPos));

            if (state.is(Blocks.CAMPFIRE) ||
                    state.is(Blocks.FIRE))
                temperature += (1 - magnitude / 16) * 2;
        }

        if (temperature < -2)
            temperature = -2;

        if (temperature > 2)
            temperature = 2;

        if (temperature <= -1 && player.isInWater())
            return -3;

        return temperature;
    }

    private Pair<Biome, Double> getClosestDifferentBiomePair(Biome excludeBiome) {
        Pair<Biome, Double> biomePair = null;

        for (BlockPos blockPos : Utils.getNearbyPositions(player.blockPosition(), 64, 2)) {
            // IChunk chunk = player.level.getChunk(blockPos.getX() >> 4, blockPos.getZ() >>
            // 4, ChunkStatus.BIOMES, false);
            // if (chunk == null)
            // continue;

            // Biome biome = chunk.getBiomes().getNoiseBiome(blockPos.getX(),
            // blockPos.getY(), blockPos.getZ());
            Biome biome = player.level.getBiome(blockPos);
            ResourceLocation biomeId = biome.getRegistryName();

            if (biomeId.toString().equals(excludeBiome.getRegistryName().toString()))
                continue;

            Double magnitude = player.blockPosition().distSqr(blockPos);

            if (biomePair == null || biomePair.second > magnitude)
                biomePair = Pair.of(biome, magnitude);
        }

        return biomePair;
    }

    private double getTemperatureOfBiome(Biome biome) {
        // double time = Math.pow(Math.sin(player.level.getDayTime() / (12000 /
        // Math.PI)), 0.6);
        double time = Math.sin(player.level.getDayTime() / (12000 / Math.PI));
        List<Triple<String, Double, Double>> modifiers = Config.biomeTemperatures();

        // double temperature = biome.getBaseTemperature();
        double temperature = 0.0;
        Triple<String, Double, Double> currentModifier = modifiers.stream()
                .filter(m -> m.getLeft().equals(biome.getRegistryName().toString())).findFirst().orElse(null);

        if (currentModifier != null) {
            // temperature += currentModifier.getMiddle();
            temperature += Utils.blend(currentModifier.getRight(), currentModifier.getMiddle(),
                    time, -1, 1);
        }

        return temperature;
    }

    /**
     * @return The player's cold protection level. Integer between 0-2.
     */
    public int getColdProtectionLevel() {
        int level = 0;

        for (ItemStack a : player.inventory.armor) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(a);

            if (enchantments.get(EnchantmentRegisterer.COLD_PROOF) != null)
                level += 1;
            else if (coldProofArmors.contains(a.getItem()))
                level += 1;
        }

        if (player.hasEffect(EffectRegisterer.COLD_RESISTANCE))
            level += player.getEffect(EffectRegisterer.COLD_RESISTANCE).getAmplifier() + 1;

        if (level > 2)
            level = 2;

        return level;

        // return coldProtectionLevel;
    }

    /**
     * @return The player's heat protection level. Integer between 0-2.
     */
    public int getHeatProtectionLevel() {
        int level = 0;

        for (ItemStack a : player.inventory.armor) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(a);

            if (enchantments.get(EnchantmentRegisterer.HEAT_PROOF) != null)
                level += 1;
        }

        if (player.hasEffect(EffectRegisterer.HEAT_RESISTANCE))
            level += player.getEffect(EffectRegisterer.HEAT_RESISTANCE).getAmplifier() + 1;

        if (level > 2)
            level = 2;

        return level;

        // return heatProtectionLevel;
    }

    /**
     * @return Whether the player is immune to fire.
     */
    public boolean getHasFlameBreaker() {
        int level = 0;

        for (ItemStack a : player.inventory.armor) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(a);

            if (enchantments.get(EnchantmentRegisterer.FLAMEBREAKER) != null)
                level += 1;
        }

        if (player.hasEffect(net.minecraft.potion.Effects.FIRE_RESISTANCE))
            level += 2;

        return level >= 2;
    }

    /**
     * @return Whether the player is immune to freezing damage.
     */
    public boolean getHasIcebreaker() {
        int level = 0;

        for (ItemStack a : player.inventory.armor) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(a);

            if (enchantments.get(EnchantmentRegisterer.ICEBREAKER) != null)
                level += 1;
        }

        if (player.hasEffect(EffectRegisterer.ICEBREAKER))
            level += 2;

        LOGGER.info(level >= 2);

        return level >= 2;
    }

    private static class Utils {
        // Credit:
        // https://github.com/Momo-Studios/Cold-Sweat/blob/1.16.5-FG/src/main/java/dev/momostudios/coldsweat/util/world/WorldHelper.java#L81
        public static List<BlockPos> getNearbyPositions(BlockPos pos, int samples, int interval) {
            List<BlockPos> posList = new ArrayList<>();
            int sampleRoot = (int) Math.sqrt(samples);

            for (int sx = 0; sx < sampleRoot; sx++) {
                for (int sz = 0; sz < sampleRoot; sz++) {
                    int length = interval * sampleRoot;
                    posList.add(pos.offset(sx * interval - (length / 2), 0, sz * interval - (length / 2)));
                }
            }

            return posList;
        }

        public static double blend(double blendFrom, double blendTo, double factor, double rangeMin, double rangeMax) {
            if (factor <= rangeMin)
                return blendFrom;
            if (factor >= rangeMax)
                return blendTo;
            return ((1 / (rangeMax - rangeMin)) * (factor - rangeMin)) * (blendTo - blendFrom) + blendFrom;
        }
    }
}
