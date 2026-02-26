package me.collinb.dynamicview;

import me.collinb.dynamicview.config.ModConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

@Environment(EnvType.CLIENT)
public class DynamicViewFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModConfig.init();
        DynamicView.init();
        ClientTickEvents.START_CLIENT_TICK.register(DynamicView::preTick);
    }
}
