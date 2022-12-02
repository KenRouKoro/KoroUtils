package cn.korostudio.kubejsextratools.kubejs.bindings;

import cn.korostudio.kubejsextratools.kubejs.events.WebSocketEvents;
import cn.korostudio.kubejsextratools.kubejs.events.eventJS.WebSocketEventJS;
import cn.korostudio.kubejsextratools.kubejs.ws.WSClient;
import dev.latvian.mods.rhino.util.HideFromJS;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
@Slf4j
public class WebSocketBindings {

    protected static WSClient wsClient =null;
    protected static ServerHandshake serverHandshake;

    public static void creatConnect(String url){
        try {
            if (wsClient!=null){
                return;
            }
            URI uri = new URI(url);
            wsClient = new WSClient(uri);
        } catch (Exception e) {
            log.error("新建WebSocket连接失败!url："+url,e);
        }
    }
    public static void connect(){
        if(wsClient==null){
            log.warn("");
            return;
        }
        try {
            wsClient.connect();
        }catch (Exception e){
            log.error("启动WebSocket连接失败!",e);
        }
    }
    public synchronized static void sendMessage(String message){
        if (wsClient==null||wsClient.isClosed()){
            log.error("发送信息失败!WebSocket连接已关闭或失效!");
            return;
        }
        wsClient.send(message);
    }
    public static void close(){
        wsClient.close();
    }
    @HideFromJS
    public static void wsClose(int code, String reason, boolean remote){
        WebSocketEvents.EVENT.post("onClose",new WebSocketEventJS(null,serverHandshake,code,reason,remote));
        wsClient = null;
    }
    @HideFromJS
    public static void wsError(Exception e){
        WebSocketEvents.EVENT.post("onError",new WebSocketEventJS(null,serverHandshake,0,null,false));
        log.error("WebSocket错误!",e);
        wsClient = null;
    }
    @HideFromJS
    public static void wsOnMessage(String message){
        WebSocketEvents.EVENT.post("onMessage",new WebSocketEventJS(message,serverHandshake,0,null,false));
    }
    @HideFromJS
    public static void wsOnOpen(ServerHandshake handshakedata){
        serverHandshake = handshakedata;
        WebSocketEvents.EVENT.post("onOpen",new WebSocketEventJS(null,serverHandshake,0,null,false));
    }
}
