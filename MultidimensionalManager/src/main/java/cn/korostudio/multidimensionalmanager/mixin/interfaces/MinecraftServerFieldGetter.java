package cn.korostudio.multidimensionalmanager.mixin.interfaces;

import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.level.storage.LevelStorage;

import java.util.Map;
import java.util.concurrent.Executor;

public interface MinecraftServerFieldGetter {
    default Executor getWorkerExecutor(){
        return null;
    }
    default LevelStorage.Session getSession(){
        return null;
    }
    default WorldGenerationProgressListener getWorldGenerationProgressListener(){
        return null;
    }
    default Map<RegistryKey<World>, ServerWorld> getWorldsMap(){
        return null;
    }
}
