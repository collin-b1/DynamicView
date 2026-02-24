package me.collinb.dynamicview;

import me.collinb.dynamicview.platform.Services;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class DynamicView {

    private static CameraType previousCameraType;

    public static void setCameraType(CameraType cameraType) {
        if (!isCameraDynamic()) {
            previousCameraType = Constants.MC.options.getCameraType();
            Constants.LOG.info("Setting previousCameraType={}", Constants.MC.options.getCameraType());
        }
        Constants.MC.options.setCameraType(cameraType);
        CameraAnimation.currentDistance = 0.0f;
        CameraAnimation.targetDistance = 4.0f;
        Constants.LOG.info("Setting cameraType={}", cameraType);
    }

    public static void unsetCameraType() {
        if (isCameraDynamic()) {
            CameraAnimation.targetDistance = 0.0f;
            CameraAnimation.onAnimationComplete = () -> {
                Constants.MC.options.setCameraType(previousCameraType);
                Constants.LOG.info("Resetting cameraType={}", previousCameraType);
                previousCameraType = null;
            };
        }
    }

    public static void init() {
        Constants.LOG.info("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND));
    }

    public static void preTick(Minecraft mc) {
        CameraAnimation.tick();
    }

    public static boolean isCameraDynamic() {
        return previousCameraType != null;
    }
}
