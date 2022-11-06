package cn.korostudio.koroutilslib;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.setting.Setting;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

@Slf4j
public class KoroUtilsLib implements ModInitializer {
    @Getter
    @Setter
    protected static MinecraftServer minecraftServer;
    @Getter
    protected static Setting commandEN = new Setting(FileUtil.touch(System.getProperty("user.dir") + "/config/CommandEn.setting"), CharsetUtil.CHARSET_UTF_8, true);
    @Getter
    @Setter
    protected static boolean cancelChat = false;

    @Override
    public void onInitialize() {

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            minecraftServer = server;
        });
        CronUtil.setMatchSecond(true);
        CronUtil.start();
        log.info("KoroUtilsLib Loaded!");
    }
}
