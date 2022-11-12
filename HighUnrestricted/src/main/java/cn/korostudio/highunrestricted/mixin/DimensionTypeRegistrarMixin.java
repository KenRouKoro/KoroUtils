package cn.korostudio.highunrestricted.mixin;


import cn.korostudio.highunrestricted.HighUnrestricted;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypeRegistrar;
import net.minecraft.world.dimension.DimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.OptionalLong;

/*
        BuiltinRegistries.add(registry, DimensionTypes.OVERWORLD, new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true, false, -64, 384, 384, BlockTags.INFINIBURN_OVERWORLD, DimensionTypes.OVERWORLD_ID, 0.0f, new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 0)));
        BuiltinRegistries.add(registry, DimensionTypes.THE_NETHER, new DimensionType(OptionalLong.of(18000L), false, true, true, false, 8.0, false, true, 0, 256, 128, BlockTags.INFINIBURN_NETHER, DimensionTypes.THE_NETHER_ID, 0.1f, new DimensionType.MonsterSettings(true, false, ConstantIntProvider.create(11), 15)));
        BuiltinRegistries.add(registry, DimensionTypes.THE_END, new DimensionType(OptionalLong.of(6000L), false, false, false, false, 1.0, false, false, 0, 256, 256, BlockTags.INFINIBURN_END, DimensionTypes.THE_END_ID, 0.0f, new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 0)));
 */
@Mixin(DimensionTypeRegistrar.class)
public class DimensionTypeRegistrarMixin {
    @Inject(method = "initAndGetDefault",at = @At(value = "HEAD"),cancellable = true)
    private static void initAndGetDefaultFix(Registry<DimensionType> registry, CallbackInfoReturnable<RegistryEntry<DimensionType>> cir){
        BuiltinRegistries.add(registry, DimensionTypes.OVERWORLD, new DimensionType(OptionalLong.empty(), true, false, false, true, 1.0, true, false, -64, HighUnrestricted.getSettingFile().getInt("overworld-max-height",384), HighUnrestricted.getSettingFile().getInt("overworld-max-hight",384), BlockTags.INFINIBURN_OVERWORLD, DimensionTypes.OVERWORLD_ID, 0.0f, new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 0)));
        BuiltinRegistries.add(registry, DimensionTypes.THE_NETHER, new DimensionType(OptionalLong.of(18000L), false, true, true, false, 8.0, false, true, 0, HighUnrestricted.getSettingFile().getInt("nether-max-height",256), HighUnrestricted.getSettingFile().getInt("nether-max-hight",256), BlockTags.INFINIBURN_NETHER, DimensionTypes.THE_NETHER_ID, 0.1f, new DimensionType.MonsterSettings(true, false, ConstantIntProvider.create(11), 15)));
        BuiltinRegistries.add(registry, DimensionTypes.THE_END, new DimensionType(OptionalLong.of(6000L), false, false, false, false, 1.0, false, false, 0, HighUnrestricted.getSettingFile().getInt("end-max-height",256), HighUnrestricted.getSettingFile().getInt("end-max-hight",256), BlockTags.INFINIBURN_END, DimensionTypes.THE_END_ID, 0.0f, new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 0)));
        cir.setReturnValue(BuiltinRegistries.add(registry, DimensionTypes.OVERWORLD_CAVES, new DimensionType(OptionalLong.empty(), true, true, false, true, 1.0, true, false, -64, HighUnrestricted.getSettingFile().getInt("overworld-max-height",384), HighUnrestricted.getSettingFile().getInt("overworld-max-hight",384), BlockTags.INFINIBURN_OVERWORLD, DimensionTypes.OVERWORLD_ID, 0.0f, new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 0))));

        cir.cancel();
    }

}
