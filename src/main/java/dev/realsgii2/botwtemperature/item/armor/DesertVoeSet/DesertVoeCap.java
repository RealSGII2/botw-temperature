package dev.realsgii2.botwtemperature.item.armor.DesertVoeSet;

import dev.realsgii2.botwtemperature.CreativeTab;
import dev.realsgii2.botwtemperature.item.armor.ScaledArmorItem;
import net.minecraft.inventory.EquipmentSlotType;

public class DesertVoeCap extends ScaledArmorItem {
    private static final Properties PROPERTIES = new Properties().stacksTo(1).tab(CreativeTab.INSTANCE);
    private static final float SCALE = 0.5f;

    public DesertVoeCap() {
        super(SCALE, DesertVoeMaterial.INSTANCE, EquipmentSlotType.HEAD, PROPERTIES);
    }
}
