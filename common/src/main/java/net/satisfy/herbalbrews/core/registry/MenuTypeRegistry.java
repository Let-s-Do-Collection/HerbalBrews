package net.satisfy.herbalbrews.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.satisfy.herbalbrews.HerbalBrews;
import net.satisfy.herbalbrews.client.gui.handler.CauldronGuiHandler;
import net.satisfy.herbalbrews.client.gui.handler.TeaKettleGuiHandler;

import java.util.function.Supplier;


public class MenuTypeRegistry {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(HerbalBrews.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<TeaKettleGuiHandler>> TEA_KETTLE_SCREEN_HANDLER = create("tea_kettle_gui_handler", () -> new MenuType<>(TeaKettleGuiHandler::new, FeatureFlags.VANILLA_SET));
    public static final RegistrySupplier<MenuType<CauldronGuiHandler>> CAULDRON_SCREEN_HANDLER = create("cauldron_gui_handler", () -> new MenuType<>(CauldronGuiHandler::new, FeatureFlags.VANILLA_SET));

    private static <T extends MenuType<?>> RegistrySupplier<T> create(String name, Supplier<T> type) {
        return MENU_TYPES.register(name, type);
    }

    public static void init() {
        MENU_TYPES.register();
    }
}
