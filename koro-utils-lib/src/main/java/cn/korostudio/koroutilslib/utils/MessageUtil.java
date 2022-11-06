package cn.korostudio.koroutilslib.utils;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class MessageUtil {
    public static void Say(ServerPlayerEntity serverPlayerEntity, Text sendText, boolean overlay) {
        serverPlayerEntity.sendMessage(sendText, overlay);
    }
}
