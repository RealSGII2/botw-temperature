package dev.realsgii2.botwtemperature.capabilities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;

public class ClientTemperatureCapability extends TemperatureCapability {
    public ClientTemperatureCapability(PlayerEntity player)
    {
        super(player);
    }
}
