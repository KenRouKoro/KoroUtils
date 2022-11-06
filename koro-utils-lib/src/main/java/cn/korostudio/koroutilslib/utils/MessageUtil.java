package cn.korostudio.koroutilslib.utils;

import net.minecraft.network.message.MessageType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.util.UUID;

public class MessageUtil {
    public static void Say(ServerPlayerEntity serverPlayerEntity, Text sendText,boolean overlay){
        serverPlayerEntity.sendMessage(sendText,overlay);
    }
}
