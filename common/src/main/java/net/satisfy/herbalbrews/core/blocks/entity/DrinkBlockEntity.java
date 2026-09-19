package net.satisfy.herbalbrews.core.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.herbalbrews.core.registry.EntityTypeRegistry;

public class DrinkBlockEntity extends BlockEntity implements Clearable {
    
    private CompoundTag storedNbt;

    public DrinkBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.DRINK_BLOCK_ENTITY.get(), pos, state);
    }

    public void setStoredNbt(CompoundTag tag) {
        this.storedNbt = tag;
    }

    public CompoundTag getStoredNbt() {
        return storedNbt;
    }

    @Override
    public void clearContent() {
        storedNbt = null;
    }

    @Override
    protected void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag, provider);
        if (compoundTag.contains("StoredNbt")) {
            storedNbt = compoundTag.getCompound("StoredNbt");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag, provider);
        if (storedNbt != null) {
            compoundTag.put("StoredNbt", storedNbt);
        }
    }
}
