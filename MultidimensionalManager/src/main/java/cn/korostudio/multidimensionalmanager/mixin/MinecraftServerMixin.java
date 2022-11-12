package cn.korostudio.multidimensionalmanager.mixin;

import cn.korostudio.multidimensionalmanager.mixin.interfaces.MinecraftServerFieldGetter;
import com.google.common.collect.Maps;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin implements MinecraftServerFieldGetter {

    WorldGenerationProgressListener worldGenerationProgressListenerField;

    @Mutable
    @Final
    @Shadow
    protected LevelStorage.Session session;
    @Mutable
    @Final
    @Shadow
    private  Executor workerExecutor;
    @Mutable
    @Final
    @Shadow
    private final Map<RegistryKey<World>, ServerWorld> worlds = new ConcurrentHashMap<>();


    @Inject(method = "createWorlds",at = @At(value = "HEAD"))
    private void getWorldGenerationProgressListenerField(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci){
        worldGenerationProgressListenerField = worldGenerationProgressListener;
    }

    @Override
    public Executor getWorkerExecutor() {
        return workerExecutor;
    }

    @Override
    public LevelStorage.Session getSession() {
        return session;
    }

    @Override
    public WorldGenerationProgressListener getWorldGenerationProgressListener() {
        return worldGenerationProgressListenerField;
    }

    @Override
    public Map<RegistryKey<World>, ServerWorld> getWorldsMap() {
        return worlds;
    }
}
