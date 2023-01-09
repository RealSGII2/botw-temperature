package dev.realsgii2.botwtemperature.item;

import net.minecraft.item.ArmorItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.realsgii2.botwtemperature.BotWTemperature.MODID;

import dev.realsgii2.botwtemperature.item.armor.ScaledArmorItem;
import dev.realsgii2.botwtemperature.item.armor.DesertVoeSet.*;
import dev.realsgii2.botwtemperature.item.armor.SnowquilSet.SnowquilBoots;
import dev.realsgii2.botwtemperature.item.armor.SnowquilSet.SnowquilCap;
import dev.realsgii2.botwtemperature.item.armor.SnowquilSet.SnowquilLeggings;
import dev.realsgii2.botwtemperature.item.armor.SnowquilSet.SnowquilTunic;

public final class Items {
    public static final DeferredRegister<net.minecraft.item.Item> ITEMS = DeferredRegister
            .create(ForgeRegistries.ITEMS, MODID);

    // Desert Voe Set
    public static final RegistryObject<ScaledArmorItem> DESERT_VOE_CAP = ITEMS.register("desert_voe_cap", () -> new DesertVoeCap());
    public static final RegistryObject<ScaledArmorItem> DESERT_VOE_TUNIC = ITEMS.register("desert_voe_tunic", () -> new DesertVoeTunic());
    public static final RegistryObject<ScaledArmorItem> DESERT_VOE_LEGGINGS = ITEMS.register("desert_voe_leggings", () -> new DesertVoeLeggings());
    public static final RegistryObject<ScaledArmorItem> DESERT_VOE_BOOTS = ITEMS.register("desert_voe_boots", () -> new DesertVoeBoots());

    // Snowquil Set
    public static final RegistryObject<ScaledArmorItem> SNOWQUIL_CAP = ITEMS.register("snowquil_cap", () -> new SnowquilCap());
    public static final RegistryObject<ScaledArmorItem> SNOWQUIL_TUNIC = ITEMS.register("snowquil_tunic", () -> new SnowquilTunic());
    public static final RegistryObject<ScaledArmorItem> SNOWQUIL_LEGGINGS = ITEMS.register("snowquil_leggings", () -> new SnowquilLeggings());
    public static final RegistryObject<ScaledArmorItem> SNOWQUIL_BOOTS = ITEMS.register("snowquil_boots", () -> new SnowquilBoots());
}
