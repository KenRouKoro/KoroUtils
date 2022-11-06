package cn.korostudio.koroutilslib.mixin;

import cn.korostudio.koroutilslib.KoroUtilsLib;
import cn.korostudio.koroutilslib.api.event.PlayerChatEvent;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class PlayerChatMixin {

    @Shadow
    public ServerPlayerEntity player;

    @Inject(method = "onChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;submit(Ljava/lang/Runnable;)Ljava/util/concurrent/CompletableFuture;"), cancellable = true)
    public void message(ChatMessageC2SPacket packet, CallbackInfo ci) {
        String string = StringUtils.normalizeSpace(packet.chatMessage());
        PlayerChatEvent.EVENT.invoker().interact(player, string);
        if (KoroUtilsLib.isCancelChat()) ci.cancel();
    }


}
