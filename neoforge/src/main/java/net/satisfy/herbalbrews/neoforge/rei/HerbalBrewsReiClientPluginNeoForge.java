package net.satisfy.herbalbrews.neoforge.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.forge.REIPluginClient;
import net.satisfy.herbalbrews.core.compat.rei.HerbalBrewsReiClientPlugin;

@REIPluginClient
public class HerbalBrewsReiClientPluginNeoForge implements REIClientPlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        HerbalBrewsReiClientPlugin.registerCategories(registry);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        HerbalBrewsReiClientPlugin.registerDisplays(registry);
    }
}
