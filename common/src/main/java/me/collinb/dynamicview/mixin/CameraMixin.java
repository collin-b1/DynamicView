package me.collinb.dynamicview.mixin;

import me.collinb.dynamicview.CameraAnimation;
import me.collinb.dynamicview.DynamicView;
import me.collinb.dynamicview.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.collinb.dynamicview.DynamicView.getMC;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Inject(method = "getMaxZoom", at = @At("HEAD"), cancellable = true)
    private void useSmoothZooming(float pMaxZoom, CallbackInfoReturnable<Float> cir) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).get();
        if (DynamicView.isCameraDynamic() && config.animationEnabled) {
            float partial = getMC().getDeltaTracker().getGameTimeDeltaPartialTick(true);
            float smoothDistance = Mth.lerp(
                    partial,
                    CameraAnimation.INSTANCE.previousDistance,
                    CameraAnimation.INSTANCE.currentDistance
            );
            cir.setReturnValue(smoothDistance);
        }
    }
}
