package dev.realsgii2.botwtemperature.network;

import com.ibm.icu.impl.Pair;

import dev.realsgii2.botwtemperature.capabilities.TemperatureCapability;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public final class SyncTemperatureCapabilityNetworkMessage {
    public final String currentBiome;
    public final Pair<String, Double> biomePair;
    public final int coldProtectionLevel;
    public final int heatProtectionLevel;
    public final boolean hasFlameBreaker;

    public SyncTemperatureCapabilityNetworkMessage(TemperatureCapability temperatureCapability) {
        this(
                temperatureCapability.getCurrentBiome().getRegistryName().toString(),
                temperatureCapability.getClosestBiomeAndDistance(),
                temperatureCapability.getColdProtectionLevel(),
                temperatureCapability.getHeatProtectionLevel(),
                temperatureCapability.getHasFlameBreaker());
    }

    public SyncTemperatureCapabilityNetworkMessage(String currentBiome, Pair<String, Double> biomePair,
            int coldProtectionLevel, int heatProtectionLevel, boolean hasFlameBreaker) {
        this.currentBiome = currentBiome;
        this.biomePair = biomePair;
        this.coldProtectionLevel = coldProtectionLevel;
        this.heatProtectionLevel = heatProtectionLevel;
        this.hasFlameBreaker = hasFlameBreaker;
    }

    public static SyncTemperatureCapabilityNetworkMessage read(PacketBuffer buffer) {
        String biomeName = buffer.readByteArray().toString();
        String[] encodedBiomePair = buffer.readByteArray().toString().split("@");

        return new SyncTemperatureCapabilityNetworkMessage(
                biomeName,
                Pair.of(encodedBiomePair[0], Double.parseDouble(encodedBiomePair[1])),
                buffer.readInt(),
                buffer.readInt(),
                buffer.readBoolean());
    }

    public void write(PacketBuffer buffer) {
        buffer
                .writeByteArray(currentBiome.getBytes())
                .writeByteArray((biomePair.first + '@' + biomePair.second).getBytes())
                .writeInt(coldProtectionLevel)
                .writeInt(heatProtectionLevel)
                .writeBoolean(hasFlameBreaker);
    }

    public void copyTo(TemperatureCapability temperatureCapability)
    {
        temperatureCapability.setCurrentBiome(getCurrentBiomeAsBiome());
        temperatureCapability.setClosestBiomeAndDistance(biomePair);
        temperatureCapability.setColdResistance(coldProtectionLevel);
        temperatureCapability.setHeatResistance(heatProtectionLevel);
        // temperatureCapability.setHasFlameBreaker(hasFlameBreaker);
    }

    public Biome getCurrentBiomeAsBiome() {
        return ForgeRegistries.BIOMES.getValue(new ResourceLocation(currentBiome));
    }
}
