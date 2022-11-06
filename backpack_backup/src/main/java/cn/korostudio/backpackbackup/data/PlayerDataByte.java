package cn.korostudio.backpackbackup.data;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class PlayerDataByte implements Serializable {
     protected ArrayList<byte[]>main = new ArrayList<>();
     protected ArrayList<byte[]>armor = new ArrayList<>();
     protected ArrayList<byte[]>offHand = new ArrayList<>();
}
