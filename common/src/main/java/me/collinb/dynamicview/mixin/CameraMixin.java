package me.collinb.dynamicview.mixin;

import me.collinb.dynamicview.camera.CameraAnimation;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Inject(method = "getMaxZoom", at = @At("TAIL"), cancellable = true)
    private void useSmoothZooming(float pMaxZoom, CallbackInfoReturnable<Float> cir) {
        CameraAnimation animation = CameraAnimation.INSTANCE;
        if (!animation.isAnimating()) {
            return;
        }
        float partialTick = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
        // Scale the vanilla (collision-clamped) distance so the animation
        // respects walls and any other mod that changes the zoom distance.
        cir.setReturnValue(animation.getProgress(partialTick) * cir.getReturnValue());
    }
}
