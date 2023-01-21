package datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

import static dev.realsgii2.botwtemperature.TemperatureMod.MODID;

import dev.realsgii2.botwtemperature.registry.LootModifierRegisterer;
import dev.realsgii2.botwtemperature.registry.lootmodifiers.DesertVoeArmorModifier;
import dev.realsgii2.botwtemperature.registry.lootmodifiers.FlamebreakerArmorModifier;
import dev.realsgii2.botwtemperature.registry.lootmodifiers.IcebreakerArmorModifier;
import dev.realsgii2.botwtemperature.registry.lootmodifiers.SnowquillArmorModifier;

public class LootModifierProvider extends GlobalLootModifierProvider {
    public LootModifierProvider(DataGenerator generator) {
        super(generator, MODID);
    }

    @Override
    protected void start() {
        addDesertVoeChestItem("village/village_desert_house", 0.35f);
        addDesertVoeChestItem("desert_pyramid", 0.65f);
        
        addSnowquillChestItem("village/village_taiga_house", 0.15f);
        addSnowquillChestItem("village/village_snowy_house", 0.35f);
        addSnowquillChestItem("igloo_chest", 0.65f);
        
        addFlamebreakerChestItem("village/village_armorer", 0.025f);
        addFlamebreakerChestItem("bastion_bridge", 0.35f);
        addFlamebreakerChestItem("bastion_hoglin_stable", 0.35f);
        addFlamebreakerChestItem("bastion_other", 0.25f);
        addFlamebreakerChestItem("bastion_treasure", 0.65f);
        addFlamebreakerChestItem("ruined_portal", 0.35f);

        addIcebreakerChestItem("village/village_armorer", 0.0125f);
        addIcebreakerChestItem("village/village_snowy_house", 0.0125f);
        addIcebreakerChestItem("igloo_chest", 0.15f);
        addIcebreakerChestItem("bastion_bridge", 0.15f);
        addIcebreakerChestItem("bastion_hoglin_stable", 0.15f);
        addIcebreakerChestItem("bastion_other", 0.1f);
        addIcebreakerChestItem("bastion_treasure", 0.25f);
    }

    private void addDesertVoeChestItem(String lootTableName, float chance) {
        add("desert_voe_armor/" + lootTableName, LootModifierRegisterer.DESERT_VOE_ARMOR.get(),
                new DesertVoeArmorModifier(new ILootCondition[] {
                        LootTableIdCondition.builder(new ResourceLocation("chests/" + lootTableName)).build(),
                        RandomChance.randomChance(chance).build()
                }));
    }

    private void addSnowquillChestItem(String lootTableName, float chance) {
        add("snowquill_armor/" + lootTableName, LootModifierRegisterer.SNOWQUILL_ARMOR.get(),
                new SnowquillArmorModifier(new ILootCondition[] {
                        LootTableIdCondition.builder(new ResourceLocation("chests/" + lootTableName)).build(),
                        RandomChance.randomChance(chance).build()
                }));
    }

    private void addFlamebreakerChestItem(String lootTableName, float chance) {
        add("flamebreaker_armor/" + lootTableName, LootModifierRegisterer.FLAMEBREAKER_ARMOR.get(),
                new FlamebreakerArmorModifier(new ILootCondition[] {
                        LootTableIdCondition.builder(new ResourceLocation("chests/" + lootTableName)).build(),
                        RandomChance.randomChance(chance).build()
                }));
    }

    private void addIcebreakerChestItem(String lootTableName, float chance) {
        add("ice_breaker_armor/" + lootTableName, LootModifierRegisterer.ICEBREAKER_ARMOR.get(),
                new IcebreakerArmorModifier(new ILootCondition[] {
                        LootTableIdCondition.builder(new ResourceLocation("chests/" + lootTableName)).build(),
                        RandomChance.randomChance(chance).build()
                }));
    }
}
