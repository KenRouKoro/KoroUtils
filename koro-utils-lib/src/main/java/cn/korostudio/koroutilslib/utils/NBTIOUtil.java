package cn.korostudio.koroutilslib.utils;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class NBTIOUtil {
    public static byte[] nbtToByteArray(NbtCompound nbt) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        NbtIo.writeCompressed(nbt, byteArrayOutputStream);
        byte[] buf = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return buf;
    }

    public static NbtCompound ByteArrayToNbt(byte[] bytes) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        return NbtIo.readCompressed(byteArrayInputStream);
    }
}
