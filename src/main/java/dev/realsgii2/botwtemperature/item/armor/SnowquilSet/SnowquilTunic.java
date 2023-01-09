package dev.realsgii2.botwtemperature.item.armor.SnowquilSet;

import dev.realsgii2.botwtemperature.CreativeTab;
import dev.realsgii2.botwtemperature.item.armor.ScaledArmorItem;
import net.minecraft.inventory.EquipmentSlotType;

public class SnowquilTunic extends ScaledArmorItem {
    private static final Properties PROPERTIES = new Properties().stacksTo(1).tab(CreativeTab.INSTANCE);
    private static final float SCALE = 0.35f;

    public SnowquilTunic() {
        super(SCALE, SnowquilMaterial.INSTANCE, EquipmentSlotType.CHEST, PROPERTIES);
    }
}
