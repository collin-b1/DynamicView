package me.collinb.dynamicview;

import me.collinb.dynamicview.camera.CameraAnimation;
import me.collinb.dynamicview.camera.CameraContext;
import me.collinb.dynamicview.config.ModConfig;
import me.collinb.dynamicview.platform.Services;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;

import java.util.EnumMap;
import java.util.Iterator;

public class DynamicView {
    private static final EnumMap<CameraContext, CameraType> activeContexts = new EnumMap<>(CameraContext.class);

    /** Non-null while the mod controls the camera; holds the perspective to restore. */
    private static CameraType previousCameraType;
    /** The perspective the mod last applied, used to detect manual (F5) changes. */
    private static CameraType appliedCameraType;

    public static void init() {
        Constants.LOG.debug("Hello from Common init on {}! we are currently in a {} environment!", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
    }

    public static void preTick(Minecraft mc) {
        if (previousCameraType != null && mc.options.getCameraType() != appliedCameraType) {
            // The user changed perspective manually; respect it and stop managing
            // the camera until the active contexts change again.
            relinquishCamera();
        }
        CameraAnimation.INSTANCE.tick();
    }

    /**
     * Reports whether a context currently applies. Safe to call every tick;
     * the camera is only touched when a context starts or stops.
     */
    public static void setContextActive(CameraContext context, boolean active, CameraType camera) {
        if (active) {
            if (activeContexts.put(context, camera) != camera) {
                applyContexts();
            }
        } else if (activeContexts.remove(context) != null) {
            applyContexts();
        }
    }

    private static void applyContexts() {
        // EnumMap iterates in declaration order, so the first entry is the
        // highest-priority active context.
        Iterator<CameraType> it = activeContexts.values().iterator();
        if (it.hasNext()) {
            applyCamera(it.next());
        } else {
            restoreCamera();
        }
    }

    private static void applyCamera(CameraType desired) {
        Options options = getMC().options;
        CameraType current = options.getCameraType();
        if (previousCameraType == null) {
            if (current == desired) {
                // The player is already in the desired perspective; leave it alone.
                return;
            }
            previousCameraType = current;
        }
        if (current != desired) {
            options.setCameraType(desired);
            appliedCameraType = desired;
        }
        if (!ModConfig.get().animationEnabled || desired == CameraType.FIRST_PERSON) {
            CameraAnimation.INSTANCE.reset();
        } else if (current == CameraType.FIRST_PERSON) {
            CameraAnimation.INSTANCE.startEnter();
        } else {
            // Already in third person, possibly mid-exit; cancel any pending restore.
            CameraAnimation.INSTANCE.resumeEnter();
        }
    }

    private static void restoreCamera() {
        if (previousCameraType == null) {
            return;
        }
        Options options = getMC().options;
        CameraType current = options.getCameraType();
        CameraType restoreTo = previousCameraType;
        boolean animated = ModConfig.get().animationEnabled;
        if (current == restoreTo) {
            relinquishCamera();
        } else if (animated && restoreTo == CameraType.FIRST_PERSON && current != CameraType.FIRST_PERSON) {
            // Zoom all the way in before switching back to first person.
            CameraAnimation.INSTANCE.startExit(() -> {
                options.setCameraType(restoreTo);
                previousCameraType = null;
                appliedCameraType = null;
            });
        } else {
            options.setCameraType(restoreTo);
            previousCameraType = null;
            appliedCameraType = null;
            if (animated && current == CameraType.FIRST_PERSON && restoreTo != CameraType.FIRST_PERSON) {
                CameraAnimation.INSTANCE.startEnter();
            } else {
                CameraAnimation.INSTANCE.reset();
            }
        }
    }

    private static void relinquishCamera() {
        previousCameraType = null;
        appliedCameraType = null;
        CameraAnimation.INSTANCE.reset();
    }

    public static Minecraft getMC() {
        return Minecraft.getInstance();
    }
}
