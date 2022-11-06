package cn.korostudio.koroutilslib.mixin;

import cn.korostudio.koroutilslib.api.event.PlayerTeleportEvent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerTeleportMixin {
    @Inject(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;setCameraEntity(Lnet/minecraft/entity/Entity;)V"))
    public void onTeleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
        PlayerTeleportEvent.EVENT.invoker().interact(((ServerPlayerEntity) (Object) this), targetWorld, x, y, z, yaw, pitch);
    }
}
