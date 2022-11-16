package cn.korostudio.sodiumsightdistanceunlocked.mixin;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Final
    @Shadow
    @Mutable
    private static  float field_32762 = 1024f;
}
