package me.collinb.dynamicview;

import me.collinb.dynamicview.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class CameraAnimation {
    public static CameraAnimation INSTANCE = new CameraAnimation();

    private final ModConfig config;

    public float previousDistance = 0.0f;
    public float currentDistance = 0.0f;
    public float targetDistance = 4.0f;

    public Runnable onAnimationComplete = null;

    public CameraAnimation() {
        this.config = AutoConfig.getConfigHolder(ModConfig.class).get();
    }

    public void tick() {
        if (!config.animationEnabled) {
            if (currentDistance != targetDistance) {
                currentDistance = targetDistance;
                previousDistance = currentDistance;
                if (onAnimationComplete != null) {
                    onAnimationComplete.run();
                    onAnimationComplete = null;
                }
            }
            return;
        }
        previousDistance = currentDistance;

        float speed = (targetDistance > currentDistance) ? config.animationEnterEasing : config.animationExitEasing;
        currentDistance += (targetDistance - currentDistance) * speed;

        if (Math.abs(currentDistance - targetDistance) < 0.1f && onAnimationComplete != null) {
            onAnimationComplete.run();
            onAnimationComplete = null;
        }
    }
}
