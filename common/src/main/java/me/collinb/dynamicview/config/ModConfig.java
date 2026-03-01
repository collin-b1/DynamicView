package me.collinb.dynamicview.config;

import me.collinb.dynamicview.Constants;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.minecraft.client.CameraType;

@Config(name = Constants.MOD_ID)
public class ModConfig implements ConfigData {
    public static void init() {
        ConfigHolder<ModConfig> holder = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
    }

    @ConfigEntry.Gui.CollapsibleObject
    public Contexts contexts = new Contexts();

    public static class Contexts {
        public boolean swimmingEnabled = true;
        @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
        public CameraType swimmingCamera = CameraType.THIRD_PERSON_BACK;

        public boolean crawlingEnabled = true;
        @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
        public CameraType crawlingCamera = CameraType.FIRST_PERSON;

        public boolean flyingEnabled = true;
        @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
        public CameraType flyingCamera = CameraType.THIRD_PERSON_BACK;

        public boolean ridingEnabled = true;
        @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
        public CameraType ridingCamera = CameraType.THIRD_PERSON_BACK;

    }

    public boolean animationEnabled = true;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 10)
    public float animationEnterEasing = 0.2f;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 10)
    public float animationExitEasing = 0.6f;
}
