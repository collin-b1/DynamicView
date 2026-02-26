package me.collinb.dynamicview;

import me.collinb.dynamicview.platform.Services;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;

public class DynamicView {

    private static CameraType previousCameraType;

    public static void setCameraType(CameraType cameraType) {
        if (!isCameraDynamic() && Constants.MC.options.getCameraType() != cameraType) {
            previousCameraType = Constants.MC.options.getCameraType();
            Constants.LOG.debug("Setting previousCameraType={}", Constants.MC.options.getCameraType());
            Constants.MC.options.setCameraType(cameraType);
            CameraAnimation.currentDistance = 0.0f;
            CameraAnimation.targetDistance = 4.0f;
            Constants.LOG.debug("Setting cameraType={}", cameraType);
        }
    }

    public static void unsetCameraType() {
        if (isCameraDynamic()) {
            if (previousCameraType != Constants.MC.options.getCameraType()) {
                CameraAnimation.targetDistance = 0.0f;
                CameraAnimation.onAnimationComplete = () -> {
                    Constants.MC.options.setCameraType(previousCameraType);
                    Constants.LOG.debug("Resetting cameraType={}", previousCameraType);
                    previousCameraType = null;
                };
            } else {
                previousCameraType = null;
            }
        }
    }

    public static void init() {
        Constants.LOG.debug("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
    }

    public static void preTick(Minecraft mc) {
        CameraAnimation.tick();
    }

    public static boolean isCameraDynamic() {
        return previousCameraType != null;
    }
}
