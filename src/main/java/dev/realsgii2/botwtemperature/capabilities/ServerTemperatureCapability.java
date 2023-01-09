package dev.realsgii2.botwtemperature.capabilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;

public class ServerTemperatureCapability extends TemperatureCapability {
    public ServerTemperatureCapability(PlayerEntity player)
    {
        super(player);
        // ServerWorld world = (ServerWorld)player.level;
        // this.currentBiome = world.getBiome(player.blockPosition());
    }
}
