package cn.korostudio.koroutilslib.api.command;

import cn.korostudio.koroutilslib.KoroUtilsLib;
import com.mojang.brigadier.CommandDispatcher;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import java.util.HashSet;

@Slf4j
public class Command {
    @Getter
    protected static HashSet<RegisterCommand> commands = new HashSet<>();

    public static void register(String nodeName, RegisterCommand registerCommand) {
        log.debug("指令集" + nodeName + "注册");
        if (KoroUtilsLib.getCommandEN().getBool(nodeName, true)) {
            commands.add(registerCommand);
            CommandRegistrationCallback.EVENT.register((server, b, c) -> {
                registerCommand.register(server);
            });
        }
    }

    public interface RegisterCommand {
        void register(CommandDispatcher<ServerCommandSource> server);
    }
}
