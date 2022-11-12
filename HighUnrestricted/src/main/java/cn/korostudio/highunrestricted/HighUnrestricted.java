package cn.korostudio.highunrestricted;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.setting.Setting;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.ModInitializer;
@Slf4j
public class HighUnrestricted implements ModInitializer {
    @Getter
    protected static Setting settingFile = new Setting(FileUtil.touch(System.getProperty("user.dir") + "/config/HighUnrestricted.setting"), CharsetUtil.CHARSET_UTF_8, true);
    @Override
    public void onInitialize() {
        log.info("High Unrestricted Loaded!");
    }
}
