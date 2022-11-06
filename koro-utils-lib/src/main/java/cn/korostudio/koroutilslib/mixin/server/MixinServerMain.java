package cn.korostudio.koroutilslib.mixin.server;

import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MixinServerMain {
    @Inject(method = "main", at = @At("HEAD"))
    private static void mixinMain(String[] args, CallbackInfo ci) {
        //InitDependence.init();
    }
}
