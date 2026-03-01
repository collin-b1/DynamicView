package me.collinb.dynamicview.mixin;

import me.collinb.dynamicview.DynamicView;
import me.collinb.dynamicview.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Unique
    private boolean dynamicView$wasSwimming = false;

    @Inject(method = "startRiding", at = @At("TAIL"))
    private void startRiding(Entity p_108667_, boolean p_108668_, boolean p_435382_, CallbackInfoReturnable<Boolean> cir) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).get();
        if (config.contexts.ridingEnabled) {
            DynamicView.setCameraType(config.contexts.ridingCamera);
        }
    }

    @Inject(method = "removeVehicle", at = @At("TAIL"))
    private void removeVehicle(CallbackInfo ci) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).get();
        if (config.contexts.ridingEnabled) {
            DynamicView.unsetCameraType();
        }
    }

    @Inject(method = "onSyncedDataUpdated", at = @At("TAIL"))
    private void updatePose(EntityDataAccessor<?> pKey, CallbackInfo ci) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).get();

        if (pKey.id() != EntityAccessor.DATA_POSE.id()) return;

        LocalPlayer player = (LocalPlayer) (Object) this;

        if (player.getPose() == Pose.SWIMMING) {
            if (player.isInWater() && config.contexts.swimmingEnabled) {
                DynamicView.setCameraType(config.contexts.swimmingCamera);
                dynamicView$wasSwimming = true;
            } else if (!player.isInWater() && config.contexts.crawlingEnabled) {
                DynamicView.setCameraType(config.contexts.crawlingCamera);
                dynamicView$wasSwimming = true;
            }
        } else if (player.getPose() == Pose.FALL_FLYING && config.contexts.flyingEnabled) {
            DynamicView.setCameraType(config.contexts.flyingCamera);
            dynamicView$wasSwimming = true;
        } else if (dynamicView$wasSwimming) {
            DynamicView.unsetCameraType();
            dynamicView$wasSwimming = false;
        }
    }
}
