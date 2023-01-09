package dev.realsgii2.botwtemperature.capabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class RemoteTemperatureCapability extends TemperatureCapability {
    public RemoteTemperatureCapability(PlayerEntity player)
    {
        super(player);
        World world = player.level;
        this.currentBiome = world.getBiome(player.blockPosition());
    }
}
