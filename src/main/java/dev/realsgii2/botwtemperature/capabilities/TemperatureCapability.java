package dev.realsgii2.botwtemperature.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Triple;

import com.ibm.icu.impl.Pair;

import dev.realsgii2.botwtemperature.Config;
import dev.realsgii2.botwtemperature.effects.Effects;
import dev.realsgii2.botwtemperature.enchantments.Enchantments;
import dev.realsgii2.botwtemperature.item.Items;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.Tag;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.realsgii2.botwtemperature.TemperatureMod.LOGGER;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemperatureCapability implements ICapabilityProvider, ITemperatureCapability {
    protected Biome currentBiome;
    protected Pair<String, Double> biomePair;
    protected int coldProtectionLevel = 0;
    protected int heatProtectionLevel = 0;
    protected int flameBreakerLevel = 0;
    protected PlayerEntity player;

    protected ArrayList<ArmorItem> coldProofArmors = new ArrayList<ArmorItem>() {
        {
            add(Items.SNOWQUIL_CAP.get());
            add(Items.SNOWQUIL_TUNIC.get());
            add(Items.SNOWQUIL_LEGGINGS.get());
            add(Items.SNOWQUIL_BOOTS.get());
        }
    };

    public TemperatureCapability(PlayerEntity player) {
        this.player = player;
    }

    public boolean shouldDamagePlayerCold() {
        double currentTemperature = getCurrentTemperature();
        return (currentTemperature != -3 && currentTemperature < -1
                && Math.abs(currentTemperature) - 1 > getColdProtectionLevel());
    }

    public boolean shouldDamagePlayerHeat() {
        double currentTemperature = getCurrentTemperature();
        return (currentTemperature != 3 && currentTemperature > 1 && currentTemperature - 1 > getHeatProtectionLevel());
    }

    public boolean shouldSetPlayerOnFire() {
        return (getCurrentTemperature() == 3 && !getHasFlameBreaker());
    }

    public boolean shouldFreezePlayer() {
        return (getCurrentTemperature() == -3 && !getHasIcebreaker());
    }

    public boolean shouldShowFireIndicator()
    {
        return (shouldSetPlayerOnFire() || player.isOnFire());
    }

    public double getCurrentTemperature() {
        if (player.level.dimension() == World.NETHER)
            return 3;

        Biome biome = player.level.getBiome(player.blockPosition());
        double temperature = getTemperatureOfBiome(biome);

        if (player.level.getBiome(player.blockPosition()).getPrecipitation() == Biome.RainType.RAIN
                && player.level.isRaining()) {
            temperature += Config.temperatureChangeIfRain();
        }

        Pair<Biome, Double> closestBiomePair = getClosestDifferentBiomePair(biome);

        if (closestBiomePair != null) {
            double closestBiomeTemperature = getTemperatureOfBiome(closestBiomePair.first);
            Double blendAmount = 0.5 + 0.5 * (closestBiomePair.second / 32);
            temperature = blend(closestBiomeTemperature, temperature, blendAmount, 0, 1);
        }

        for (BlockPos blockPos : getNearbyPositions(player.blockPosition(), 16, 1)) {
            BlockState state = player.level.getBlockState(blockPos);
            Double magnitude = Math.abs(player.blockPosition().distSqr(blockPos));

            if (
                state.is(Blocks.CAMPFIRE) ||
                state.is(Blocks.FIRE)
            )
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

    // Credit:
    // https://github.com/Momo-Studios/Cold-Sweat/blob/1.16.5-FG/src/main/java/dev/momostudios/coldsweat/util/world/WorldHelper.java#L81
    private static List<BlockPos> getNearbyPositions(BlockPos pos, int samples, int interval) {
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

    private Pair<Biome, Double> getClosestDifferentBiomePair(Biome excludeBiome) {
        Pair<Biome, Double> biomePair = null;

        for (BlockPos blockPos : getNearbyPositions(player.blockPosition(), 64, 2)) {
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
            temperature += currentModifier.getMiddle();
            temperature += blend(currentModifier.getRight(), currentModifier.getMiddle(),
                    time, -1, 1);
        }

        return temperature;
    }

    public Biome getCurrentBiome() {
        return currentBiome;
    }

    public Pair<String, Double> getClosestBiomeAndDistance() {
        return biomePair;
    }

    public int getColdProtectionLevel() {
        int level = 0;

        for (ItemStack a : player.inventory.armor) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(a);

            if (enchantments.get(Enchantments.COLD_PROOF_ENCHANTMENT) != null)
                level += 1;
            else if (coldProofArmors.contains(a.getItem()))
                level += 1;
        }

        if (player.hasEffect(Effects.COLD_RESISTANCE_EFFECT.get()))
            level += player.getEffect(Effects.COLD_RESISTANCE_EFFECT.get()).getAmplifier() + 1;

        if (level > 2)
            level = 2;

        return level;

        // return coldProtectionLevel;
    }

    public int getHeatProtectionLevel() {
        int level = 0;

        for (ItemStack a : player.inventory.armor) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(a);

            if (enchantments.get(Enchantments.HEAT_PROOF_ENCHANTMENT) != null)
                level += 1;
        }

        if (player.hasEffect(Effects.HEAT_RESISTANCE_EFFECT.get()))
            level += player.getEffect(Effects.HEAT_RESISTANCE_EFFECT.get()).getAmplifier() + 1;

        if (level > 2)
            level = 2;

        return level;

        // return heatProtectionLevel;
    }

    public boolean getHasFlameBreaker() {
        int level = 0;

        for (ItemStack a : player.inventory.armor) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(a);

            if (enchantments.get(Enchantments.FLAMEBREAKER_ENCHANTMENT) != null)
                level += 1;
        }

        if (player.hasEffect(net.minecraft.potion.Effects.FIRE_RESISTANCE))
            level += 2;

        return level >= 2;

        // return flameBreakerLevel >= 2;
    }

    public boolean getHasIcebreaker() {
        int level = 0;

        for (ItemStack a : player.inventory.armor) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(a);

            if (enchantments.get(Enchantments.ICEBREAKER_ENCHANTMENT) != null)
                level += 1;
        }

        if (player.hasEffect(Effects.ICEBREAKER_EFFECT.get()))
            level += 2;

        LOGGER.info(level >= 2);

        return level >= 2;

        // return flameBreakerLevel >= 2;
    }

    public void updatePropertiesBasedOnPlayer() {
        LOGGER.error(
                "updatePropertiesBasedOnPlayer called on something other than the server. It is a server only method.");
    }

    public void setCurrentBiome(Biome biome) {
        currentBiome = biome;
    }

    public void setClosestBiomeAndDistance(@Nullable Pair<String, Double> biomePair) {
        this.biomePair = biomePair;
    }

    public void setColdResistance(int protectionLevel) {
        coldProtectionLevel = protectionLevel;
    }

    public void setHeatResistance(int protectionLevel) {
        heatProtectionLevel = protectionLevel;
    }

    // public void setHasFlameBreaker(boolean hasFlameBreaker) {
    // this.hasFlameBreaker = hasFlameBreaker;
    // }

    private final LazyOptional<TemperatureCapability> self = LazyOptional.of(() -> this);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction side) {
        return capability == Capabilities.temperatureCapability ? self.cast() : LazyOptional.empty();
    }

    public static TemperatureCapability of(ICapabilityProvider capabilityProvider) {
        return capabilityProvider.getCapability(Capabilities.temperatureCapability).orElse(null);
    }

    private static double blend(double blendFrom, double blendTo, double factor, double rangeMin, double rangeMax) {
        if (factor <= rangeMin)
            return blendFrom;
        if (factor >= rangeMax)
            return blendTo;
        return ((1 / (rangeMax - rangeMin)) * (factor - rangeMin)) * (blendTo - blendFrom) + blendFrom;
    }
}
