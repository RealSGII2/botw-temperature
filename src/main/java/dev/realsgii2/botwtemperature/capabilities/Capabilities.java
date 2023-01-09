package dev.realsgii2.botwtemperature.capabilities;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static dev.realsgii2.botwtemperature.BotWTemperature.MODID;

import javax.annotation.Nullable;
import javax.naming.OperationNotSupportedException;

@Mod.EventBusSubscriber(modid = MODID, bus = Bus.MOD)
public final class Capabilities {
    private Capabilities() {
    };

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            registerCapability(TemperatureCapability.class);
        });
    }

    private static <T> void registerCapability(Class<T> classOf) {
        CapabilityManager.INSTANCE.register(classOf, new Capability.IStorage<T>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<T> capability, T instance, Direction side) {
                return null;
            }

            @Override
            public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
            }
        }, () -> {
            throw new OperationNotSupportedException();
        });
    }

    @CapabilityInject(TemperatureCapability.class)
    public static Capability<TemperatureCapability> temperatureCapability = null;
}
