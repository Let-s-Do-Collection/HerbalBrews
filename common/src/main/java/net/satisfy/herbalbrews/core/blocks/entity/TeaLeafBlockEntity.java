package net.satisfy.herbalbrews.core.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.satisfy.herbalbrews.core.blocks.TeaLeafBlock;
import net.satisfy.herbalbrews.core.registry.EntityTypeRegistry;
import net.satisfy.herbalbrews.core.registry.ObjectRegistry;
import net.satisfy.herbalbrews.platform.PlatformHelper;

public class TeaLeafBlockEntity extends BlockEntity implements BlockEntityTicker<TeaLeafBlockEntity> {
    private int timer = 0;

    public TeaLeafBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.TEA_LEAF_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        timer = tag.getInt("Timer");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Timer", timer);
    }

    private Block getEndBlock(Block startBlock) {
        if (startBlock == ObjectRegistry.GREEN_TEA_LEAF_BLOCK.get()) {
            return ObjectRegistry.DRIED_OUT_GREEN_TEA_LEAF_BLOCK.get();
        } else if (startBlock == ObjectRegistry.MIXED_TEA_LEAF_BLOCK.get()) {
            return ObjectRegistry.OOLONG_TEA_LEAF_BLOCK.get();
        } else if (startBlock == ObjectRegistry.DRIED_GREEN_TEA_LEAF_BLOCK.get()) {
            return ObjectRegistry.BLACK_TEA_LEAF_BLOCK.get();
        }
        return Blocks.AIR;
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, TeaLeafBlockEntity blockEntity) {
        if (level == null || level.isClientSide) {
            return;
        }

        int dryingDuration = PlatformHelper.getDryingDuration();
        timer++;

        if (timer >= dryingDuration) {
            BlockState state = level.getBlockState(blockPos);
            if(!state.hasProperty(TeaLeafBlock.DRYING))
                return;

            int dryingStage = state.getValue(TeaLeafBlock.DRYING);
            if (dryingStage < 4) {
                level.levelEvent(2001, blockPos, Block.getId(state));
                level.playSound(null, blockPos, SoundEvents.GRASS_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.setBlockAndUpdate(blockPos, state.setValue(TeaLeafBlock.DRYING, dryingStage + 1));
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(state));
                timer = 0;
            } else {
                Block endBlock = getEndBlock(state.getBlock());
                if (endBlock != Blocks.AIR) {
                    level.setBlockAndUpdate(blockPos, endBlock.defaultBlockState());
                }
            }
        }
    }
}
