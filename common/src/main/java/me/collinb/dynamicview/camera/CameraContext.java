package me.collinb.dynamicview.camera;

/**
 * A gameplay situation that can take control of the camera. Declaration order
 * is priority order: when several contexts are active at once, the earliest
 * declared one decides the perspective.
 */
public enum CameraContext {
    RIDING,
    FLYING,
    SWIMMING,
    CRAWLING
}
