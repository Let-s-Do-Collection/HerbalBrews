package net.satisfy.herbalbrews.core.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Clearable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.herbalbrews.core.items.DrinkBlockItem;
import net.satisfy.herbalbrews.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class JugBlockEntity extends BlockEntity implements Clearable {
    private final List<ItemStack> drinks = new ArrayList<>();

    public JugBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.JUG_BLOCK_ENTITY.get(), pos, state);
    }

    public void addDrink(ItemStack drink) {
        if (drinks.size() < 3 && drink.getItem() instanceof DrinkBlockItem) {
            drinks.add(drink.copy());
            setChanged();
            if (level != null) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }

    public List<ItemStack> getDrinks() {
        return drinks;
    }

    public void clearDrinks() {
        drinks.clear();
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void clearContent() {
        drinks.clear();
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        ListTag drinkList = new ListTag();
        for (ItemStack drink : drinks) {
            drinkList.add(drink.save(provider, new CompoundTag()));
        }
        compoundTag.put("Drinks", drinkList);
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        drinks.clear();
        if (compoundTag.contains("Drinks", 9)) {
            ListTag drinkList = compoundTag.getList("Drinks", 10);
            for (int i = 0; i < drinkList.size(); i++) {
                CompoundTag drinkTag = drinkList.getCompound(i);
                ItemStack drink = ItemStack.parseOptional(provider, drinkTag);
                drinks.add(drink);
            }
        }
    }

    public void applyEffects(LivingEntity user, int durationTicks) {
        for (ItemStack drink : drinks) {
            if (drink.getItem() instanceof DrinkBlockItem) {
                PotionContents data = drink.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
                if (data.hasEffects()) {
                    data.forEachEffect(mobEffectInstance -> {
                        mobEffectInstance.duration = durationTicks;
                        user.addEffect(mobEffectInstance);
                    });
                }
            }
        }
    }
}
