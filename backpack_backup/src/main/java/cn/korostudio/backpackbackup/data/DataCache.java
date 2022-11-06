package cn.korostudio.backpackbackup.data;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.ObjectUtil;
import cn.korostudio.koroutilslib.utils.NBTIOUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DataCache {
    @Setter
    @Getter
    protected static boolean dirty = true;
    @Getter
    protected static ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerData>> PlayerDataCache = new ConcurrentHashMap<>();

    public static void save() {
        //判断是否需要持久化
        if (!dirty) {
            return;
        }
        dirty = false;
        //序列化玩家背包集NBT
        ByteData byteData = new ByteData();
        PlayerDataCache.forEach((playerUUID, playerDataMap) -> {
            ConcurrentHashMap<String, PlayerDataByte> playerDataByteMap = new ConcurrentHashMap<>();
            playerDataMap.forEach((name, playerData) -> {
                PlayerDataByte playerDataByte = new PlayerDataByte();
                playerData.getMain().forEach(nbt -> {
                    try {
                        playerDataByte.main.add(NBTIOUtil.nbtToByteArray(nbt));
                    } catch (IOException e) {
                        log.error("转换NBT出错，可能有不支持序列化的NBT出现", e);
                    }
                });
                playerData.getArmor().forEach(nbt -> {
                    try {
                        playerDataByte.armor.add(NBTIOUtil.nbtToByteArray(nbt));
                    } catch (IOException e) {
                        log.error("转换NBT出错，可能有不支持序列化的NBT出现", e);
                    }
                });
                playerData.getOffHand().forEach(nbt -> {
                    try {
                        playerDataByte.offHand.add(NBTIOUtil.nbtToByteArray(nbt));
                    } catch (IOException e) {
                        log.error("转换NBT出错，可能有不支持序列化的NBT出现", e);
                    }
                });
                playerDataByteMap.put(name, playerDataByte);
            });
            byteData.getPlayerDataCache().put(playerUUID, playerDataByteMap);
        });
        //Java序列化
        byte[] saveByteArray = ObjectUtil.serialize(byteData);
        //持久化
        File file = FileUtil.touch(System.getProperty("user.dir") + "/config/BackpackBackup/backpacks_save.dat");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.writeFromStream(new ByteArrayInputStream(saveByteArray), true);
    }

    public static void load() {
        //判断是否存在文件
        if (!FileUtil.isFile(System.getProperty("user.dir") + "/config/BackpackBackup/backpacks_save.dat")) {
            return;
        }
        //读取文件
        File file = new File(System.getProperty("user.dir") + "/config/BackpackBackup/backpacks_save.dat");
        FileReader fileReader = new FileReader(file);
        byte[] saveByteArray = fileReader.readBytes();
        //Java反序列化
        ByteData byteData = ObjectUtil.deserialize(saveByteArray);
        //反序列化玩家NBT
        byteData.getPlayerDataCache().forEach((uuid, playerDataByteMap) -> {
            ConcurrentHashMap<String, PlayerData> playerDataMap;
            if (!PlayerDataCache.containsKey(uuid)) {
                playerDataMap = new ConcurrentHashMap<>();
            } else {
                playerDataMap = PlayerDataCache.get(uuid);
            }
            playerDataByteMap.forEach((name, playerDataByte) -> {
                PlayerData playerData = new PlayerData();

                playerDataByte.main.forEach(nbt -> {
                    try {
                        playerData.main.add(NBTIOUtil.ByteArrayToNbt(nbt));
                    } catch (IOException e) {
                        log.error("转换NBT出错，可能有不支持反序列化的NBT出现", e);
                    }
                });
                playerDataByte.armor.forEach(nbt -> {
                    try {
                        playerData.armor.add(NBTIOUtil.ByteArrayToNbt(nbt));
                    } catch (IOException e) {
                        log.error("转换NBT出错，可能有不支持反序列化的NBT出现", e);
                    }
                });
                playerDataByte.offHand.forEach(nbt -> {
                    try {
                        playerData.offHand.add(NBTIOUtil.ByteArrayToNbt(nbt));
                    } catch (IOException e) {
                        log.error("转换NBT出错，可能有不支持反序列化的NBT出现", e);
                    }
                });
                playerDataMap.put(name, playerData);

            });
            PlayerDataCache.put(uuid, playerDataMap);
        });


    }

    public static void autosaveTask() {
        log.info("正在自动保存");
        save();
    }
}
