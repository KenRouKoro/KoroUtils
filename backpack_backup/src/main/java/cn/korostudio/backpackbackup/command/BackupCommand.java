package cn.korostudio.backpackbackup.command;


import cn.hutool.core.thread.ThreadUtil;
import cn.korostudio.backpackbackup.BackpackBackup;
import cn.korostudio.backpackbackup.data.DataCache;
import cn.korostudio.backpackbackup.data.PlayerData;
import cn.korostudio.koroutilslib.api.command.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.server.command.CommandManager.argument;
@Slf4j
public class BackupCommand {
    public static void register() {
        Command.register("BackpackBackup",server -> {
            server.register(literal(BackpackBackup.getBackpackBackupSetting().getStr("header","backup")).then(literal(BackpackBackup.getBackpackBackupSetting().getStr("backup","backup")).then(argument("name", MessageArgumentType.message()).executes(BackupCommand::backup))));
            server.register(literal(BackpackBackup.getBackpackBackupSetting().getStr("header","backup")).then(literal(BackpackBackup.getBackpackBackupSetting().getStr("restore","restore")).then(argument("name", MessageArgumentType.message()).executes(BackupCommand::restore))));
            server.register(literal(BackpackBackup.getBackpackBackupSetting().getStr("header","backup")).then(literal(BackpackBackup.getBackpackBackupSetting().getStr("remove","remove")).then(argument("name", MessageArgumentType.message()).executes(BackupCommand::remove))));
            server.register(literal(BackpackBackup.getBackpackBackupSetting().getStr("header","backup")).then(literal(BackpackBackup.getBackpackBackupSetting().getStr("list","list")).executes(BackupCommand::backupList)));
        });
    }

    public static int backup(CommandContext<ServerCommandSource> server){
        try {
            ServerPlayerEntity player = server.getSource().getPlayer();
            String nameStr = MessageArgumentType.getMessage(server, "name").getString();

            if(player==null){
                log.error("请勿在控制台执行该指令");
                return 1;
            }
            ConcurrentHashMap<String,PlayerData>playerDataMap;
            if(!DataCache.getPlayerDataCache().containsKey(player.getUuidAsString())){
                playerDataMap = new ConcurrentHashMap<>();
                DataCache.getPlayerDataCache().put(player.getUuidAsString(),playerDataMap);
            }else {
                playerDataMap = DataCache.getPlayerDataCache().get(player.getUuidAsString());
            }

            if((!playerDataMap.containsKey(nameStr))&&playerDataMap.size()>BackpackBackup.getBackpackBackupSetting().getInt("max-backup",5)){
                player.sendMessage(Text.literal("§a已达到设定储存上限."));
                return 1;
            }
            ThreadUtil.execute(()->{
                //深拷贝玩家背包NBT
                DefaultedList<ItemStack> main = player.getInventory().main;
                DefaultedList<ItemStack> armor = player.getInventory().armor;
                DefaultedList<ItemStack> offHand = player.getInventory().offHand;

                PlayerData playerData = new PlayerData();

                for (ItemStack itemStack : main) {
                    playerData.getMain().add(itemStack.writeNbt(new NbtCompound()));
                }
                for (ItemStack itemStack : armor) {
                    playerData.getArmor().add(itemStack.writeNbt(new NbtCompound()));
                }
                for (ItemStack itemStack : offHand) {
                    playerData.getOffHand().add(itemStack.writeNbt(new NbtCompound()));
                }

                playerDataMap.put(nameStr,playerData);

                if(BackpackBackup.getBackpackBackupSetting().getBool("delete-used-backup",true)){
                    player.getInventory().main.clear();
                    player.getInventory().armor.clear();
                    player.getInventory().offHand.clear();
                }

                player.sendMessage(Text.literal("§a背包 §b"+nameStr+" §a备份完毕."));
            });

        }catch (Exception e) {
            log.error("备份指令出错",e);
        }

        return 1;
    }
    public static int backupList(CommandContext<ServerCommandSource> server){
        try {
            ServerPlayerEntity player = server.getSource().getPlayer();

            if(player==null){
                log.error("请勿在控制台执行该指令");
                return 1;
            }
            if(!DataCache.getPlayerDataCache().containsKey(player.getUuidAsString())){
                player.sendMessage(Text.literal("§a您没有任何背包备份."));
                return 1;
            }

            ConcurrentHashMap<String,PlayerData> playerDataMap = DataCache.getPlayerDataCache().get(player.getUuidAsString());

            if(playerDataMap.size()==0){
                player.sendMessage(Text.literal("§a您没有任何背包备份."));
                return 1;
            }
            MutableText textCache = Text.literal("====当前所有背包备份====\n");
            for(String key:playerDataMap.keySet()){
                textCache.append(Text.literal("恢复").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/"+BackpackBackup.getBackpackBackupSetting().getStr("header","backup")+" "+BackpackBackup.getBackpackBackupSetting().getStr("backup","backup")+" "+key)).withColor(TextColor.parse("#00FFFF")).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("恢复背包备份 "+key)))));
                textCache.append(Text.literal("  "));
                textCache.append(Text.literal("删除").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,"/"+BackpackBackup.getBackpackBackupSetting().getStr("header","backup")+" "+BackpackBackup.getBackpackBackupSetting().getStr("remove","remove")+" "+key)).withColor(TextColor.parse("#FF0000")).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,Text.literal("删除背包备份 "+key)))));
                textCache.append(Text.literal("  "+key+"\n"));
            }
            textCache.append(Text.literal("===================="));
            player.sendMessage(textCache);

        }catch (Exception e) {
            log.error("列表指令出错.",e);
        }
        return 1;
    }
    public static int remove(CommandContext<ServerCommandSource> server){
        try {
            ServerPlayerEntity player = server.getSource().getPlayer();
            String nameStr = MessageArgumentType.getMessage(server, "name").getString();

            if(player==null){
                log.error("请勿在控制台执行该指令");
                return 1;
            }
            if(!DataCache.getPlayerDataCache().containsKey(player.getUuidAsString())){
                player.sendMessage(Text.literal("§a您还没有任何背包备份."));
                return 1;
            }

            ConcurrentHashMap<String,PlayerData> playerDataMap = DataCache.getPlayerDataCache().get(player.getUuidAsString());

            if(playerDataMap.size()==0){
                player.sendMessage(Text.literal("§a您还没有任何背包备份."));
                return 1;
            }

            if(!playerDataMap.containsKey(nameStr)){
                player.sendMessage(Text.literal("§a您并没有名为 §b"+nameStr+" §a的背包备份可供删除."));
                return 1;
            }

            playerDataMap.remove(nameStr);

            player.sendMessage(Text.literal("§a背包备份 §b"+nameStr+" §a已删除."));

        }catch (Exception e) {
            log.error("删除指令出错.",e);
        }
        return 1;
    }
    public static int restore(CommandContext<ServerCommandSource> server){
        try {
            ServerPlayerEntity player = server.getSource().getPlayer();
            String nameStr = MessageArgumentType.getMessage(server, "name").getString();
            if(player==null){
                log.error("请勿在控制台执行该指令.");
                return 1;
            }
            if(!DataCache.getPlayerDataCache().containsKey(player.getUuidAsString())){
                player.sendMessage(Text.literal("§a您还没有任何背包备份."));
                return 1;
            }

            ConcurrentHashMap<String,PlayerData> playerDataMap = DataCache.getPlayerDataCache().get(player.getUuidAsString());

            if(!playerDataMap.containsKey(nameStr)){
                player.sendMessage(Text.literal("§a您并没有名为 §b"+nameStr+" §a的背包备份."));
                return 1;
            }

            ThreadUtil.execute(()->{
                PlayerData playerData = playerDataMap.get(nameStr);

                DefaultedList<ItemStack> main = player.getInventory().main;
                DefaultedList<ItemStack> armor = player.getInventory().armor;
                DefaultedList<ItemStack> offHand = player.getInventory().offHand;

                for (int i = 0; i < playerData.getMain().size(); i++) {
                    main.set(i, ItemStack.fromNbt(playerData.getMain().get(i)));
                }
                for (int i = 0; i < playerData.getArmor().size(); i++) {
                    armor.set(i, ItemStack.fromNbt(playerData.getArmor().get(i)));
                }
                for (int i = 0; i < playerData.getOffHand().size(); i++) {
                    offHand.set(i, ItemStack.fromNbt(playerData.getOffHand().get(i)));
                }

                if(BackpackBackup.getBackpackBackupSetting().getBool("delete-used-backup",true)){
                    DataCache.getPlayerDataCache().get(player.getUuidAsString()).remove(nameStr);
                }

                player.sendMessage(Text.literal("§a背包 §b"+nameStr+" §a恢复完毕."));
            });
        }catch (Exception e) {
            log.error("恢复指令出错.",e);
        }
        return 1;
    }
}
