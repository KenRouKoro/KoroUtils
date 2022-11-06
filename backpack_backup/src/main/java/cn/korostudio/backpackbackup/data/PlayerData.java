package cn.korostudio.backpackbackup.data;

import lombok.Data;
import net.minecraft.nbt.NbtCompound;

import java.util.concurrent.CopyOnWriteArrayList;

@Data
public class PlayerData {
    protected CopyOnWriteArrayList<NbtCompound> main = new CopyOnWriteArrayList<>();
    protected CopyOnWriteArrayList<NbtCompound> armor = new CopyOnWriteArrayList<>();
    protected CopyOnWriteArrayList<NbtCompound> offHand = new CopyOnWriteArrayList<>();
}
