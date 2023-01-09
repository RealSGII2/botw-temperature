package dev.realsgii2.botwtemperature.capabilities;

import javax.annotation.Nullable;

import com.ibm.icu.impl.Pair;

import net.minecraft.world.biome.Biome;

public interface ITemperatureCapability {
    /**
     * @return Should the player be damaged?
     */
    public boolean shouldDamagePlayerCold();

    /**
     * @return Should the player be damaged?
     */
    public boolean shouldDamagePlayerHeat();

    /**
     * @return Should the player be set on fire?
     */
    public boolean shouldSetPlayerOnFire();

    /**
     * Gets the computed temperature based on the current biome's temperature
     * and the closest BiomeAndDistance's temperature
     * 
     * @return The current temperature
     */
    public double getCurrentTemperature();

    /**
     * Updates biome, closest biome, cold resistance, heat resistance, and flamebreaker status
     * based on the player.
     * 
     * @param playerEntity The player.
     */
    public void updatePropertiesBasedOnPlayer();

    /**
     * Sets the biome the player is currently in.
     */
    public void setCurrentBiome(Biome biome);

    /**
     * Sets the BiomeAndDistance that should be used when computing
     * temperature.
     * 
     * @param biomePair The BiomeAndDistance. Should be the closest one to the player within 8 blocks. Pass null if there isn't one.
     */
    public void setClosestBiomeAndDistance(@Nullable Pair<String, Double> biomePair);

    /**
     * Sets the level of cold resistance the player has. Should be inclusively between 0-2. 
     * 
     * @param protectionLevel The level of cold resistance the player has. Should be [0-2].
     */
    public void setColdResistance(int protectionLevel);
    
    /**
     * Sets the level of heat resistance the player has. Should be inclusively between 0-2. 
     * 
     * @param protectionLevel The level of heat resistance the player has. Should be [0-2].
     */
    public void setHeatResistance(int protectionLevel);
    
    // /**
    //  * Sets whether the player has the Flamebreaker effect or if they have Flamebreaker armour. 
    //  * 
    //  * @param hasFlameBreaker Whether the player has Flamebreaker armour.
    //  */
    // public void setHasFlameBreaker(boolean hasFlameBreaker);
}
