package me.collinb.dynamicview.config;

import me.collinb.dynamicview.Constants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;

@Config(name = Constants.MOD_ID)
public class ModConfig implements ConfigData {
    public static void init() {
        ConfigHolder<ModConfig> holder = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
    }

    @ConfigEntry.Gui.CollapsibleObject
    EnabledContexts enabledContexts = new EnabledContexts();
    public static class EnabledContexts {
        boolean swimming = true;
        boolean crawling = true;
        boolean flying = true;
        boolean riding = true;
    }

    boolean animationEnabled = true;
    double easing = 0.2;
}
