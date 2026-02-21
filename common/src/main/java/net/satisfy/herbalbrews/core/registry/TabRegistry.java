package net.satisfy.herbalbrews.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.herbalbrews.HerbalBrews;

@SuppressWarnings("unused")
public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> HERBALBREWS_TABS = DeferredRegister.create(HerbalBrews.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> HERBALBREWS_TAB = HERBALBREWS_TABS.register("herbalbrews", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .icon(() -> new ItemStack(ObjectRegistry.TEA_KETTLE.get()))
            .title(Component.translatable("creative_tab.herbalbrews"))
            .displayItems((parameters, out) -> {
                out.accept(ObjectRegistry.STOVE.get());
                out.accept(ObjectRegistry.TEA_LEAF_CRATE.get());
                out.accept(ObjectRegistry.GREEN_TEA_LEAF_BLOCK.get());
                out.accept(ObjectRegistry.DRIED_GREEN_TEA_LEAF_BLOCK.get());
                out.accept(ObjectRegistry.BLACK_TEA_LEAF_BLOCK.get());
                out.accept(ObjectRegistry.MIXED_TEA_LEAF_BLOCK.get());
                out.accept(ObjectRegistry.OOLONG_TEA_LEAF_BLOCK.get());
                out.accept(ObjectRegistry.WITCH_HAT.get());
                out.accept(ObjectRegistry.TOP_HAT.get());
                out.accept(ObjectRegistry.WILD_COFFEE_PLANT.get());
                out.accept(ObjectRegistry.WILD_YERBA_MATE_PLANT.get());
                out.accept(ObjectRegistry.WILD_ROOIBOS_PLANT.get());
                out.accept(ObjectRegistry.HIBISCUS.get());
                out.accept(ObjectRegistry.LAVENDER.get());
                out.accept(ObjectRegistry.TEA_BLOSSOM.get());
                out.accept(ObjectRegistry.GREEN_TEA_LEAF.get());
                out.accept(ObjectRegistry.YERBA_MATE_LEAF.get());
                out.accept(ObjectRegistry.ROOIBOS_LEAF.get());
                out.accept(ObjectRegistry.COFFEE_BEANS.get());
                out.accept(ObjectRegistry.DRIED_GREEN_TEA.get());
                out.accept(ObjectRegistry.DRIED_BLACK_TEA.get());
                out.accept(ObjectRegistry.DRIED_OOLONG_TEA.get());
                out.accept(ObjectRegistry.LAVENDER_BLOSSOM.get());
                out.accept(ObjectRegistry.TEA_KETTLE.get());
                out.accept(ObjectRegistry.COPPER_TEA_KETTLE.get());
                out.accept(ObjectRegistry.CAULDRON.get());
                out.accept(ObjectRegistry.JUG.get());
                out.accept(ObjectRegistry.GREEN_TEA.get());
                out.accept(ObjectRegistry.BLACK_TEA.get());
                out.accept(ObjectRegistry.HIBISCUS_TEA.get());
                out.accept(ObjectRegistry.LAVENDER_TEA.get());
                out.accept(ObjectRegistry.COFFEE.get());
                out.accept(ObjectRegistry.MILK_COFFEE.get());
                out.accept(ObjectRegistry.ROOIBOS_TEA.get());
                out.accept(ObjectRegistry.OOLONG_TEA.get());
                out.accept(ObjectRegistry.YERBA_MATE_TEA.get());
                out.accept(ObjectRegistry.FLASK.get());
                out.accept(ObjectRegistry.HERBAL_INFUSION.get());
                out.accept(ObjectRegistry.HERBALBREWS_BANNER.get());
            })
            .build());

    public static void init() {
        HERBALBREWS_TABS.register();
    }
}
