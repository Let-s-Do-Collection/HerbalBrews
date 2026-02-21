package net.satisfy.herbalbrews.core.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.herbalbrews.core.blocks.entity.TeaKettleBlockEntity;
import net.satisfy.herbalbrews.core.registry.EntityTypeRegistry;
import net.satisfy.herbalbrews.core.registry.SoundEventRegistry;
import net.satisfy.herbalbrews.core.util.HerbalBrewsUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class TeaKettleBlock extends BaseEntityBlock {
    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.5, 0.8125), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.0625, 0.125, 0.4375, 0.1875, 0.5, 0.5625), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.3125, 0.5, 0.3125, 0.6875, 0.5625, 0.6875), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0.4375, 0.5625, 0.4375, 0.5625, 0.625, 0.5625), BooleanOp.OR);
        shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.5, 0.4375, 0.1875, 0.625, 0.5625), BooleanOp.OR);
        return shape;
    };

    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, HerbalBrewsUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty LIT = BooleanProperty.create("lit");
    public static final BooleanProperty COOKING = BooleanProperty.create("cooking");

    public TeaKettleBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(COOKING, false).setValue(LIT, false));
    }

    public static final MapCodec<TeaKettleBlock> CODEC = simpleCodec(TeaKettleBlock::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        BlockEntity entity = level.getBlockEntity(blockPos);
        if (entity instanceof MenuProvider factory) {
            player.openMenu(factory);
            return InteractionResult.SUCCESS;
        } else {
            return super.useWithoutItem(blockState, level, blockPos, player, blockHitResult);
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TeaKettleBlockEntity pot) {
                if (world instanceof ServerLevel) {
                    Containers.dropContents(world, pos, pot);
                }
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (world.isClientSide()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof TeaKettleBlockEntity be && be.doEffect) {
                double d = pos.getX() + 0.5;
                double e = pos.getY() + 0.5;
                double f = pos.getZ() + 0.5;
                world.playLocalSound(d, e, f, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                double radius = 0.75;
                int particleCount = 16;
                for (int i = 0; i < particleCount; i++) {
                    double angle = 2.0 * Math.PI * i / particleCount;
                    double offsetX = radius * Math.cos(angle);
                    double offsetZ = radius * Math.sin(angle);
                    double px = d + offsetX;
                    double pz = f + offsetZ;
                    world.addParticle(ParticleTypes.FLAME, px, e, pz, 0, 0.02, 0);
                }
                be.doEffect = false;
            }
        }

        if (state.getValue(COOKING) || state.getValue(LIT)) {
            double d = pos.getX() + 0.5;
            double e = pos.getY() + 8.0F + 5.0F;
            double f = pos.getZ() + 0.5;
            if (random.nextDouble() < 0.3) {
                world.playLocalSound(d, e, f, SoundEventRegistry.TEA_KETTLE_BOILING.get(), SoundSource.BLOCKS, 0.05F, 1.0F, false);
            }
            SimpleParticleType cozySmokeParticle = ParticleTypes.SMOKE;
            addParticle(world, cozySmokeParticle, pos.getX(), pos.getY() + 0.5, pos.getZ(), random, 0.02);
            addParticle(world, ParticleTypes.SMOKE, pos.getX(), pos.getY() + 2.2, pos.getZ(), random, 0.002);
        }
    }

    private void addParticle(Level world, ParticleOptions particleOptions, double x, double y, double z, RandomSource random, double velocityY) {
        world.addAlwaysVisibleParticle(
                particleOptions,
                x + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                y + random.nextDouble() + random.nextDouble(),
                z + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                0.0,
                velocityY,
                0.0
        );
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        boolean isLit = state.getValue(LIT);
        if (isLit && !entity.fireImmune() && entity instanceof LivingEntity livingEntity) {
            livingEntity.hurt(world.damageSources().hotFloor(), 1.f);
        }
        super.stepOn(world, pos, state, entity);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, COOKING, LIT);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, EntityTypeRegistry.TEA_KETTLE_BLOCK_ENTITY.get(), (world1, pos, state1, be) -> be.tick(world1, pos, state1));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TeaKettleBlockEntity(pos, state);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltip, TooltipFlag tooltipFlag) {
        tooltip.add(Component.translatable("tooltip.herbalbrews.canbeplaced").withStyle(ChatFormatting.GRAY));
    }
}
