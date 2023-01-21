package dev.realsgii2.botwtemperature.registry;

import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static dev.realsgii2.botwtemperature.TemperatureMod.MODID;

import dev.realsgii2.botwtemperature.registry.lootmodifiers.*;

public class LootModifierRegisterer {
        public static final DeferredRegister<GlobalLootModifierSerializer<?>> REGISTER = DeferredRegister
                        .create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MODID);

        public static final RegistryObject<DesertVoeArmorModifier.Serializer> DESERT_VOE_ARMOR = REGISTER
                        .register("desert_voe_armor", DesertVoeArmorModifier.Serializer::new);

        public static final RegistryObject<SnowquillArmorModifier.Serializer> SNOWQUILL_ARMOR = REGISTER
                        .register("snowquill_armor", SnowquillArmorModifier.Serializer::new);

        public static final RegistryObject<FlamebreakerArmorModifier.Serializer> FLAMEBREAKER_ARMOR = REGISTER
                        .register("flamebreaker_armor", FlamebreakerArmorModifier.Serializer::new);

        public static final RegistryObject<IcebreakerArmorModifier.Serializer> ICEBREAKER_ARMOR = REGISTER
                        .register("icebreaker_armor", IcebreakerArmorModifier.Serializer::new);
}
