package net.satisfy.herbalbrews.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.satisfy.herbalbrews.HerbalBrews;
import net.satisfy.herbalbrews.client.HerbalbrewsClient;

@Mod.EventBusSubscriber(modid = HerbalBrews.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HerbalbrewsClientForge {
    @SubscribeEvent
    public static void onClientSetup(RegisterEvent event) {
        HerbalbrewsClient.preInitClient();
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        HerbalbrewsClient.onInitializeClient();
    }
}
