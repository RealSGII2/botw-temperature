package dev.realsgii2.botwtemperature.registry.lootmodifiers;

import java.util.List;

import javax.annotation.Nonnull;

import com.google.gson.JsonObject;

import dev.realsgii2.botwtemperature.registry.ArmorRegisterer;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import static dev.realsgii2.botwtemperature.TemperatureMod.LOGGER;

public class DesertVoeArmorModifier extends LootModifier {
    public static final ItemStack[] items = new ItemStack[] {
            new ItemStack(ArmorRegisterer.DESERT_VOE_HEADBAND, 1),
            new ItemStack(ArmorRegisterer.DESERT_VOE_SPAULDER, 1),
            new ItemStack(ArmorRegisterer.DESERT_VOE_TROUSERS, 1),
            new ItemStack(ArmorRegisterer.DESERT_VOE_BOOTS, 1)
    };

    public DesertVoeArmorModifier(ILootCondition[] lootConditions) {
        super(lootConditions);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        LOGGER.info("doApply called");

        generatedLoot.add(items[context.getRandom().nextInt(items.length)]);

        return generatedLoot;
    }

    public static final class Serializer extends GlobalLootModifierSerializer<DesertVoeArmorModifier> {
        @Override
        public DesertVoeArmorModifier read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new DesertVoeArmorModifier(conditionsIn);
        }

        @Override
        public JsonObject write(DesertVoeArmorModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
