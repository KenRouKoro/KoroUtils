package cn.korostudio.sodiumsightdistanceunlocked.mixin;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.setting.Setting;
import de.johni0702.minecraft.bobby.Bobby;
import de.johni0702.minecraft.bobby.BobbyConfig;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.Control;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.MinecraftOptionsStorage;
import net.minecraft.client.option.GameOptions;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Field;

@Mixin(value = SodiumGameOptionPages.class,remap = false,priority = 1500)//高优先级
public class SodiumGameOptionPagesMixin {


    @Inject(method = "lambda$general$0",at = @At("HEAD"), cancellable = true)//constant  = @Constant(intValue = 32))
    private static void changeSodiumSetting(OptionImpl option, CallbackInfoReturnable<Control> cir) {
        cir.setReturnValue(new SliderControl(option, 2, 1024, 1, ControlValueFormatter.translateVariable("options.chunks")));
    }
    @Inject(method = "lambda$general$1",at = @At("HEAD"))
    private static void setBobby(GameOptions options, Integer value, CallbackInfo ci){
        try {
            Class.forName("de.johni0702.minecraft.bobby.Bobby");
        } catch (ClassNotFoundException e) {
            return;
        }
        Setting setting = new Setting(System.getProperty("user.dir") + "/config/bobby.conf");
        setting.set("max-render-distance",value.toString());
        StringBuilder stringBuilder = new StringBuilder();
        setting.forEach((key,v)->{
            stringBuilder.append(key).append('=').append(v).append('\n');
        });
        FileWriter fileWriter = new FileWriter(System.getProperty("user.dir") + "/config/bobby.conf");
        fileWriter.write(stringBuilder.toString());
    }

    /*
    @ModifyArg(method = "general",at =@At(value = "INVOKE",target = "Lcom/google/common/collect/ImmutableList;copyOf(Ljava/util/Collection;)Lcom/google/common/collect/ImmutableList;"))
    private static Collection<?> setTestValue(Collection<? > elements){
        List<OptionGroup> groups = (List<OptionGroup>) elements;

        groups.set(0,OptionGroup.createBuilder()
                        .add(OptionImpl.createBuilder(int.class, vanillaOpts)
                                .setName(Text.translatable("options.renderDistance"))
                                .setTooltip(Text.translatable("sodium.options.view_distance.tooltip"))
                                .setControl(option -> new SliderControl(option, 2, 1024, 1, ControlValueFormatter.translateVariable("options.chunks")))
                                .setBinding((options, value) -> options.getViewDistance().setValue(value), options -> options.getViewDistance().getValue())
                                .setImpact(OptionImpact.HIGH)
                                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                                .build())
                        .add(OptionImpl.createBuilder(int.class, vanillaOpts)
                                .setName(Text.translatable("options.simulationDistance"))
                                .setTooltip(Text.translatable("sodium.options.simulation_distance.tooltip"))
                                .setControl(option -> new SliderControl(option, 5, 1024, 1, ControlValueFormatter.translateVariable("options.chunks")))
                                .setBinding((options, value) -> options.getSimulationDistance().setValue(value), options -> options.getSimulationDistance().getValue())
                                .setImpact(OptionImpact.HIGH)
                                .setFlags(OptionFlag.REQUIRES_RENDERER_RELOAD)
                                .build())
                        .add(OptionImpl.createBuilder(int.class, vanillaOpts)
                                .setName(Text.translatable("options.gamma"))
                                .setTooltip(Text.translatable("sodium.options.brightness.tooltip"))
                                .setControl(opt -> new SliderControl(opt, 0, 100, 1, ControlValueFormatter.brightness()))
                                .setBinding((opts, value) -> opts.getGamma().setValue(value * 0.01D), (opts) -> (int) (opts.getGamma().getValue() / 0.01D))
                                .build())
                        .build()
                );

        return ImmutableList.copyOf(elements) ;
    }*/

}
