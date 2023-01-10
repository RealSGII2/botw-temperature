package dev.realsgii2.botwtemperature;

import dev.realsgii2.botwtemperature.registry.Registerer;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(TemperatureMod.MODID)
@Mod.EventBusSubscriber(modid = TemperatureMod.MODID, bus = Bus.MOD)
public class TemperatureMod {
    public static final String MODID = "botwtemperature";
    public static final Logger LOGGER = LogManager.getLogger("BotWTemperature");

    public TemperatureMod() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        Config.init();
        Registerer.register(bus);
    }
}
