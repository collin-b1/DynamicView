package me.collinb.dynamicview.mixin;

import me.collinb.dynamicview.CameraAnimation;
import me.collinb.dynamicview.DynamicView;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.collinb.dynamicview.Constants.MC;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Inject(method = "getMaxZoom", at = @At("HEAD"), cancellable = true)
    private void useSmoothZooming(float pMaxZoom, CallbackInfoReturnable<Float> cir) {
        if (DynamicView.isCameraDynamic()) {
            float partial = MC.getTimer().getGameTimeDeltaPartialTick(true);
            float smoothDistance = Mth.lerp(
                    partial,
                    CameraAnimation.previousDistance,
                    CameraAnimation.currentDistance
            );
            cir.setReturnValue(smoothDistance);
        }
    }
}
