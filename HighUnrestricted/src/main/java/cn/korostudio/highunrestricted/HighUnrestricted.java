package cn.korostudio.highunrestricted;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.setting.Setting;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.api.ModInitializer;


@Slf4j
public class HighUnrestricted implements ModInitializer {
    @Getter
    protected static Setting settingFile ;
    protected static String DefSetting = new String("""
            #主世界最高高度
            overworld-max-height = 384
            #下届最高高度
            nether-max-height = 256
            #末地最高高度
            end-max-height = 256
            """);
    static{
        if(!FileUtil.isFile(System.getProperty("user.dir") + "/config/HighUnrestricted.setting")){
            FileWriter fileWriter = new FileWriter(FileUtil.touch(System.getProperty("user.dir") + "/config/HighUnrestricted.setting"));
            fileWriter.write(DefSetting);
        }
        settingFile = new Setting(System.getProperty("user.dir") + "/config/HighUnrestricted.setting");

    }

    @Override
    public void onInitialize() {
        log.info("High Unrestricted Loaded!");
    }
}
