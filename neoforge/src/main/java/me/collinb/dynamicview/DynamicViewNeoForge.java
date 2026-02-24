package me.collinb.dynamicview;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
@EventBusSubscriber(value = Dist.CLIENT)
public class DynamicViewNeoForge {

    public DynamicViewNeoForge(IEventBus eventBus) {
        DynamicView.init();
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void preTick(ClientTickEvent.Pre event) {
        DynamicView.preTick(Minecraft.getInstance());
    }
}
