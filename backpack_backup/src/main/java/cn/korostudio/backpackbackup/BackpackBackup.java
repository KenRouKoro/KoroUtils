package cn.korostudio.backpackbackup;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.setting.Setting;
import cn.korostudio.backpackbackup.command.BackupCommand;
import cn.korostudio.backpackbackup.data.DataCache;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

@Slf4j
public class BackpackBackup implements ModInitializer {
    @Getter
    protected static Setting BackpackBackupSetting = new Setting(FileUtil.touch(System.getProperty("user.dir") + "/config/BackpackBackup.setting"), CharsetUtil.CHARSET_UTF_8, true);
    @Override
    public void onInitialize() {
        BackupCommand.register();
        log.info("正在读取已保存的背包备份数据");
        DataCache.load();
        log.info("读取完毕");
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            log.info("正在保存玩家背包备份数据");
            DataCache.save();
        });
        CronUtil.schedule("*/5 * * * *", (Task) DataCache::autosaveTask);

    }
}
