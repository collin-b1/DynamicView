package me.collinb.dynamicview.camera;

import me.collinb.dynamicview.config.ModConfig;
import net.minecraft.util.Mth;

/**
 * Animates the third-person camera distance as a fraction of the vanilla zoom,
 * where 0 places the camera at the player and 1 is the full distance.
 */
public final class CameraAnimation {
    public static final CameraAnimation INSTANCE = new CameraAnimation();

    private static final float SNAP_THRESHOLD = 0.01f;

    private float previousProgress = 1.0f;
    private float currentProgress = 1.0f;
    private float targetProgress = 1.0f;

    private Runnable onComplete;

    private CameraAnimation() {
    }

    /** Zooms out from the player to the full third-person distance. */
    public void startEnter() {
        previousProgress = 0.0f;
        currentProgress = 0.0f;
        targetProgress = 1.0f;
        onComplete = null;
    }

    /**
     * Re-targets the full distance from wherever the camera currently is,
     * cancelling a pending exit and its completion callback.
     */
    public void resumeEnter() {
        targetProgress = 1.0f;
        onComplete = null;
    }

    /** Zooms in towards the player, then runs {@code onComplete}. */
    public void startExit(Runnable onComplete) {
        targetProgress = 0.0f;
        this.onComplete = onComplete;
    }

    /** Stops any animation, discarding a pending completion callback. */
    public void reset() {
        previousProgress = 1.0f;
        currentProgress = 1.0f;
        targetProgress = 1.0f;
        onComplete = null;
    }

    public void tick() {
        previousProgress = currentProgress;
        if (currentProgress == targetProgress) {
            return;
        }
        ModConfig config = ModConfig.get();
        if (!config.animationEnabled) {
            finish();
            return;
        }
        int speed = (targetProgress > currentProgress) ? config.animationEnterSpeed : config.animationExitSpeed;
        currentProgress += (targetProgress - currentProgress) * (speed / 100.0f);
        if (Math.abs(currentProgress - targetProgress) < SNAP_THRESHOLD) {
            finish();
        }
    }

    private void finish() {
        currentProgress = targetProgress;
        if (onComplete != null) {
            Runnable callback = onComplete;
            onComplete = null;
            callback.run();
        }
    }

    public boolean isAnimating() {
        return currentProgress != targetProgress;
    }

    public float getProgress(float partialTick) {
        return Mth.lerp(partialTick, previousProgress, currentProgress);
    }
}
