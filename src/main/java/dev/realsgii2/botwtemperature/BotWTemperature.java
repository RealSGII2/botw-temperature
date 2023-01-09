package dev.realsgii2.botwtemperature;

import org.apache.logging.log4j.Logger;

import dev.realsgii2.botwtemperature.effects.Effects;
import dev.realsgii2.botwtemperature.item.Items;
import dev.realsgii2.botwtemperature.network.ServerNetwork;
import dev.realsgii2.botwtemperature.potions.Potions;

import org.apache.logging.log4j.LogManager;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(BotWTemperature.MODID)
@Mod.EventBusSubscriber(modid = BotWTemperature.MODID, bus = Bus.MOD)
public class BotWTemperature {
    public static final String MODID = "botwtemperature";
    public static final Logger LOGGER = LogManager.getLogger("BotWTemperature");

    public BotWTemperature() {
        MinecraftForge.EVENT_BUS.register(this);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        Config.init();
        ServerNetwork.init();

        Effects.EFFECTS.register(bus);
        Potions.POTIONS.register(bus);
        Items.ITEMS.register(bus);
    }
}
