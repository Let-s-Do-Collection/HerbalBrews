package net.satisfy.herbalbrews.core.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import org.jetbrains.annotations.NotNull;

public class HatItem extends ArmorItem {
    private final ResourceLocation hatTexture;

    public HatItem(ArmorMaterial armorMaterial, Type type, Properties properties, ResourceLocation hatTexture) {
        super(armorMaterial, type, properties);
        this.hatTexture = hatTexture;
    }


    public ResourceLocation getHatTexture()
    {
        return hatTexture;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return this.type.getSlot();
    }
}