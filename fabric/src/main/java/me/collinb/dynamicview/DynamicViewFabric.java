package me.collinb.dynamicview;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class DynamicViewFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        DynamicView.init();
        ClientTickEvents.START_CLIENT_TICK.register(DynamicView::preTick);
    }
}
