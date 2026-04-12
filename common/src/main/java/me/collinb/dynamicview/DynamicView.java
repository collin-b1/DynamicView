package me.collinb.dynamicview;

import me.collinb.dynamicview.camera.CameraAnimation;
import me.collinb.dynamicview.config.ModConfig;
import me.collinb.dynamicview.platform.Services;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;

public class DynamicView {
    private static final ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).get();

    private static CameraType previousCameraType;

    public static void setCameraType(CameraType cameraType) {
        if (getMC().options.getCameraType() != cameraType) {
            previousCameraType = getMC().options.getCameraType();
            if (cameraType != null) {
                getMC().options.setCameraType(cameraType);
            }
            CameraAnimation.INSTANCE.currentDistance = 0.0f;
            CameraAnimation.INSTANCE.targetDistance = 4.0f;
        }
    }

    public static void unsetCameraType() {
        if (isCameraDynamic()) {
            if (previousCameraType != getMC().options.getCameraType()) {
                CameraAnimation.INSTANCE.targetDistance = 0.0f;
                if (config.animationEnabled) {
                    CameraAnimation.INSTANCE.onAnimationComplete = () -> {
                        getMC().options.setCameraType(previousCameraType);
                        previousCameraType = null;
                    };
                } else {
                    getMC().options.setCameraType(previousCameraType);
                    previousCameraType = null;
                }
            }
        }
    }

    public static void init() {
        Constants.LOG.debug("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
    }

    public static void preTick(Minecraft mc) {
        CameraAnimation.INSTANCE.tick();
    }

    public static Minecraft getMC() {
        return Minecraft.getInstance();
    }

    public static boolean isCameraDynamic() {
        return previousCameraType != null;
    }
}
