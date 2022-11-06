package cn.korostudio.koroutilslib.processing;

import cn.hutool.core.collection.ConcurrentHashSet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Path;

@Slf4j
public class InitDependence {
    @Getter
    protected static ConcurrentHashSet<Path> dependencePaths = new ConcurrentHashSet<>();

    protected static void loadPath(String... paths) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        loadPath(InitDependence.class.getClassLoader(), paths);
    }

    protected static void loadPath(ClassLoader classLoader, String... paths) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = FabricLauncherBase.getLauncher().getTargetClassLoader().getClass();
        Method addUrl;
        try {
            addUrl = clazz.getMethod("addUrlFwd", URL.class);
        } catch (NoSuchMethodException e) {
            addUrl = clazz.getMethod("addUrl", URL.class);
        }
        addUrl.setAccessible(true);
        for (String path : paths) {
            addUrl.invoke(FabricLauncherBase.getLauncher().getTargetClassLoader(), classLoader.getResource(path));
        }
    }
}
