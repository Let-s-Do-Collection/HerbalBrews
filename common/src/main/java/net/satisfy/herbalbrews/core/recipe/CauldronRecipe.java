package net.satisfy.herbalbrews.core.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.satisfy.herbalbrews.core.registry.RecipeTypeRegistry;
import net.satisfy.herbalbrews.core.util.HerbalBrewsUtil;
import org.jetbrains.annotations.NotNull;

public class CauldronRecipe implements Recipe<Container> {

    private final NonNullList<net.minecraft.world.item.crafting.Ingredient> inputs;
    private final ItemStack output;

    public CauldronRecipe(NonNullList<net.minecraft.world.item.crafting.Ingredient> inputs, ItemStack output) {
        this.inputs = inputs;
        this.output = output;
    }

    @Override
    public boolean matches(Container inventory, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(Container recipeInput, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull NonNullList<net.minecraft.world.item.crafting.Ingredient> getIngredients() {
        return this.inputs;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.output.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeTypeRegistry.CAULDRON_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeTypeRegistry.CAULDRON_RECIPE_TYPE.get();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
    public static class Serializer implements RecipeSerializer<CauldronRecipe> {
        @Override
        public MapCodec<CauldronRecipe> codec() {
            return null;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CauldronRecipe> streamCodec() {
            return null;
        }

        // TODO fixme
        /*
        @Override
        public @NotNull CauldronRecipe fromJson(ResourceLocation id, JsonObject json) {
            final var ingredients = HerbalBrewsUtil.deserializeIngredients(GsonHelper.getAsJsonArray(json, "ingredients"));
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients for Brewing Cauldron");
            } else if (ingredients.size() > 3) {
                throw new JsonParseException("Too many ingredients for Brewing Cauldron");
            } else {
                return new CauldronRecipe(id, ingredients, net.minecraft.world.item.crafting.ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result")));
            }
        }

        @Override
        public @NotNull CauldronRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            final var ingredients  = NonNullList.withSize(buf.readVarInt(), net.minecraft.world.item.crafting.Ingredient.EMPTY);
            ingredients.replaceAll(ignored -> net.minecraft.world.item.crafting.Ingredient.fromNetwork(buf));
            return new CauldronRecipe(id, ingredients, buf.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CauldronRecipe recipe) {
            buf.writeVarInt(recipe.inputs.size());
            for (net.minecraft.world.item.crafting.Ingredient ingredient : recipe.inputs) {
                ingredient.toNetwork(buf);
            }

            buf.writeItem(recipe.output);
        }*/
    }
}
