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
    private static ConfigHolder<ModConfig> holder;

    public static void init() {
        holder = AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);
    }

    public static ModConfig get() {
        return holder.get();
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

    // Percentage of the remaining distance covered each tick; 100 is instant.
    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int animationEnterSpeed = 20;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int animationExitSpeed = 60;

    @Override
    public void validatePostLoad() {
        animationEnterSpeed = clampSpeed(animationEnterSpeed);
        animationExitSpeed = clampSpeed(animationExitSpeed);
    }

    private static int clampSpeed(int speed) {
        return Math.max(1, Math.min(100, speed));
    }
}
