package me.collinb.dynamicview.mixin;

import me.collinb.dynamicview.DynamicView;
import me.collinb.dynamicview.camera.CameraContext;
import me.collinb.dynamicview.config.ModConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    // Polling once per tick (instead of hooking mount/dismount and pose data
    // updates) catches every way a context can start or stop: failed mount
    // attempts, leaving water while still in the swimming pose, config changes
    // mid-context, respawning, and so on.
    @Inject(method = "tick", at = @At("TAIL"))
    private void dynamicView$updateContexts(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        ModConfig.Contexts contexts = ModConfig.get().contexts;
        boolean swimmingPose = player.getPose() == Pose.SWIMMING;

        DynamicView.setContextActive(CameraContext.RIDING,
                contexts.ridingEnabled && player.getVehicle() != null,
                contexts.ridingCamera);
        DynamicView.setContextActive(CameraContext.FLYING,
                contexts.flyingEnabled && player.getPose() == Pose.FALL_FLYING,
                contexts.flyingCamera);
        DynamicView.setContextActive(CameraContext.SWIMMING,
                contexts.swimmingEnabled && swimmingPose && player.isInWater(),
                contexts.swimmingCamera);
        DynamicView.setContextActive(CameraContext.CRAWLING,
                contexts.crawlingEnabled && swimmingPose && !player.isInWater(),
                contexts.crawlingCamera);
    }
}
