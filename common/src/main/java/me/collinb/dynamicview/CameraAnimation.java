package me.collinb.dynamicview;

public class CameraAnimation {
    public static float previousDistance = 0.0f;
    public static float currentDistance = 0.0f;
    public static float targetDistance = 4.0f;

    public static float enterSpeed = 0.2f;
    public static float exitSpeed = 0.6f;

    public static Runnable onAnimationComplete = null;

    public static void tick() {
        previousDistance = currentDistance;

        float speed = (targetDistance > currentDistance) ? enterSpeed : exitSpeed;
        currentDistance += (targetDistance - currentDistance) * speed;

        if (Math.abs(currentDistance - targetDistance) < 0.1f && onAnimationComplete != null) {
            onAnimationComplete.run();
            onAnimationComplete = null;
        }
    }
}
