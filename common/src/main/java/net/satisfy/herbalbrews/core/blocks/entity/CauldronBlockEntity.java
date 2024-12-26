package net.satisfy.herbalbrews.core.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.herbalbrews.client.gui.handler.CauldronGuiHandler;
import net.satisfy.herbalbrews.core.registry.EntityTypeRegistry;
import net.satisfy.herbalbrews.core.registry.RecipeTypeRegistry;
import net.satisfy.herbalbrews.core.world.ImplementedInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CauldronBlockEntity extends BlockEntity implements ImplementedInventory, BlockEntityTicker<CauldronBlockEntity>, MenuProvider {
    private NonNullList<ItemStack> inventory;
    public static final int CAPACITY = 6;
    private static final int BOTTLE_INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 5;
    private int brewingTime = 0;
    private int totalBrewingTime = 30;

    private static final int[] SLOTS_FOR_SIDE = new int[]{0};
    private static final int[] SLOTS_FOR_UP = new int[]{1, 2, 3, 4};
    private static final int[] SLOTS_FOR_DOWN = new int[]{5};

    private final ContainerData propertyDelegate = new ContainerData() {

        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> CauldronBlockEntity.this.brewingTime;
                case 1 -> CauldronBlockEntity.this.totalBrewingTime;
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0 -> CauldronBlockEntity.this.brewingTime = value;
                case 1 -> CauldronBlockEntity.this.totalBrewingTime = value;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public CauldronBlockEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.CAULDRON_BLOCK_ENTITY.get(), pos, state);
        this.inventory = NonNullList.withSize(CAPACITY, ItemStack.EMPTY);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.inventory = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.inventory);
        this.brewingTime = nbt.getShort("BrewingTime");
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, this.inventory);
        nbt.putShort("BrewingTime", (short) this.brewingTime);
    }

    @Override
    public void tick(Level world, BlockPos pos, BlockState state, CauldronBlockEntity blockEntity) {
        if (world.isClientSide) return;
        boolean dirty = false;
        Recipe<?> recipe = world.getRecipeManager().getRecipeFor(RecipeTypeRegistry.CAULDRON_RECIPE_TYPE.get(), this, world).orElse(null);
        assert level != null;
        RegistryAccess access = level.registryAccess();
        if (canCraft(recipe, access)) {
            this.brewingTime++;

            if (this.brewingTime >= this.totalBrewingTime) {
                this.brewingTime = 0;
                craft(recipe, access);
                dirty = true;
            }
        } else {
            this.brewingTime = 0;
        }
        if (dirty) {
            setChanged();
        }

    }

    private boolean canCraft(Recipe<?> recipe, RegistryAccess access) {
        if (recipe == null || recipe.getResultItem(access).isEmpty()) {
            return false;
        } else if (areInputsEmpty()) {
            return false;
        } else if (this.getItem(BOTTLE_INPUT_SLOT).isEmpty()) {
            return false;
        } else {
            final Item item = this.getItem(BOTTLE_INPUT_SLOT).getItem();
            if (item != Items.GLASS_BOTTLE.asItem()) {
                return false;
            }
            return this.getItem(OUTPUT_SLOT).isEmpty();
        }
    }

    private boolean areInputsEmpty() {
        int emptyStacks = 0;
        for (int i = 1; i < 5; i++) {
            if (this.getItem(i).isEmpty()) emptyStacks++;
        }
        return emptyStacks == 4;
    }

    private void craft(Recipe<?> recipe, RegistryAccess access) {
        if (!canCraft(recipe, access)) {
            return;
        }
        final ItemStack recipeOutput = recipe.getResultItem(access);
        final ItemStack outputSlotStack = this.getItem(OUTPUT_SLOT);

        if (outputSlotStack.isEmpty()) {
            ItemStack output = recipeOutput.copy();
            setItem(OUTPUT_SLOT, output);
        }

        final ItemStack bottle = this.getItem(BOTTLE_INPUT_SLOT);
        if (bottle.getCount() > 1) {
            removeItem(BOTTLE_INPUT_SLOT, 1);
        } else if (bottle.getCount() == 1) {
            setItem(BOTTLE_INPUT_SLOT, ItemStack.EMPTY);
        }

        for (Ingredient entry : recipe.getIngredients()) {
            for (int i = 1; i <= 4; i++) {
                if (entry.test(this.getItem(i))) {
                    ItemStack stack = this.getItem(i);
                    ItemStack remainder = getRemainderItem(stack);
                    stack.shrink(1);
                    if (stack.isEmpty() && !remainder.isEmpty()) {
                        setItem(i, remainder);
                    } else {
                        setItem(i, stack);
                    }
                    break;
                }
            }
        }
    }

    private ItemStack getRemainderItem(ItemStack stack) {
        if (stack.getItem().hasCraftingRemainingItem()) {
            return new ItemStack(Objects.requireNonNull(stack.getItem().getCraftingRemainingItem()));
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int @NotNull [] getSlotsForFace(Direction side) {
        if (side.equals(Direction.UP)) {
            return SLOTS_FOR_UP;
        } else if (side.equals(Direction.DOWN)) {
            return SLOTS_FOR_DOWN;
        } else {
            return SLOTS_FOR_SIDE;
        }
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        final ItemStack stackInSlot = this.inventory.get(slot);
        boolean dirty = !stack.isEmpty() && ItemStack.isSameItem(stack, stackInSlot) && ItemStack.matches(stack, stackInSlot);
        this.inventory.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (slot == BOTTLE_INPUT_SLOT || (slot >= 2 && slot <= 5)) {
            if (!dirty) {
                this.totalBrewingTime = 600;
                this.brewingTime = 0;
                setChanged();
            }
        }
    }

    @Override
    public boolean stillValid(Player player) {
        assert this.level != null;
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double) this.worldPosition.getX() + 0.5, (double) this.worldPosition.getY() + 0.5, (double) this.worldPosition.getZ() + 0.5) <= 64.0;
        }
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new CauldronGuiHandler(syncId, inv, this, this.propertyDelegate);
    }
}
