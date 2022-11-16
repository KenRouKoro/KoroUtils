package cn.korostudio.sodiumsightdistanceunlocked.mixin;

import cn.korostudio.sodiumsightdistanceunlocked.client.SodiumSightDistanceUnlockedClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Final
    @Mutable
    @Shadow
    private SimpleOption<Integer> viewDistance;

    @Redirect(method = "<init>",at = @At(value = "FIELD",target = "Lnet/minecraft/client/option/GameOptions;viewDistance:Lnet/minecraft/client/option/SimpleOption;"))
    public void setGameOptions(GameOptions instance, SimpleOption<Integer> value1){
        SodiumSightDistanceUnlockedClient.setGameOptions(((GameOptions)(Object)this));
        viewDistance = new SimpleOption<>("options.renderDistance", SimpleOption.emptyTooltip(), (optionText, value) -> GameOptions.getGenericValueText(optionText, Text.translatable("options.chunks", value)), new SimpleOption.ValidatingIntSliderCallbacks(2, 1024), 512, value -> MinecraftClient.getInstance().worldRenderer.scheduleTerrainUpdate());
    }

}
