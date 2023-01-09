package dev.realsgii2.botwtemperature.item.armor.DesertVoeSet;

import dev.realsgii2.botwtemperature.item.armor.BaseArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;

public class DesertVoeMaterial extends BaseArmorMaterial {
    private static final String NAME = "desert_voe";

    private static final int[] DURABILITY = new int[] { 1, 2, 3, 2 };
    private static final int DURABILITY_FACTOR = 1;

    private static final int[] DEFENSE = new int[] { 13, 15, 16, 11 };

    private static final int ENCHANTABILITY = 0;

    private static final SoundEvent EQUIP_SOUND = null;

    private static final Ingredient REPAIR_MATERIAL = null;

    private static final float TOUGHNESS = 0;

    private static final float KNOCKBACK_RESISTANCE = 0;

    public DesertVoeMaterial() {
        super(NAME, DURABILITY, DURABILITY_FACTOR, DEFENSE, ENCHANTABILITY, EQUIP_SOUND, REPAIR_MATERIAL, TOUGHNESS,
                KNOCKBACK_RESISTANCE);
    }

    public static final DesertVoeMaterial INSTANCE = new DesertVoeMaterial();
}
