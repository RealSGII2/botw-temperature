package dev.realsgii2.botwtemperature.item.armor;

import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;

import static dev.realsgii2.botwtemperature.BotWTemperature.MODID;

public abstract class BaseArmorMaterial implements IArmorMaterial {
    private String name;

    private int[] durability;
    private int durabilityFactor;

    private int[] defense;

    private int enchantability;

    private SoundEvent equipSound;

    private Ingredient repairMaterial;

    private float toughness;

    private float knockbackResistance;

    public BaseArmorMaterial(
            String name,
            int[] durability,
            int durabilityFactor,
            int[] defense,
            int enchantability,
            SoundEvent equipSound,
            Ingredient repairMaterial,
            float toughness,
            float knockbackResistance) {
        this.name = name;
        this.durability = durability;
        this.durabilityFactor = durabilityFactor;
        this.defense = defense;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.repairMaterial = repairMaterial;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlotType slot) {
        return durability[slot.getIndex()] * durabilityFactor;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlotType slot) {
        return defense[slot.getIndex()];
    }

    @Override
    public int getEnchantmentValue() {
        return enchantability;
    }

    @Override
    public SoundEvent getEquipSound() {
        return equipSound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairMaterial;
    }

    @Override
    public String getName() {
        return MODID + ':' + name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }
}
