package cn.korostudio.backpackbackup.data;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class ByteData implements Serializable {
    protected ConcurrentHashMap<String, ConcurrentHashMap<String, PlayerDataByte>> PlayerDataCache = new ConcurrentHashMap<>();
}
