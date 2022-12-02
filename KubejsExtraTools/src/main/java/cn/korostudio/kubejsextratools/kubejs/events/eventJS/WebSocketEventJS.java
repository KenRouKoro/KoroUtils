package cn.korostudio.kubejsextratools.kubejs.events.eventJS;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import dev.latvian.mods.kubejs.event.EventJS;
import lombok.Getter;
import org.java_websocket.handshake.ServerHandshake;
@Getter
public class WebSocketEventJS extends EventJS {
    protected String message;
    protected ServerHandshake serverHandshake;
    protected int code;
    protected String reason;
    protected boolean remote;
    public WebSocketEventJS(String message,ServerHandshake serverHandshake,int code,String reason,boolean remote){
        this.message = message;
        this.serverHandshake = serverHandshake;
        this.code = code;
        this.reason = reason;
        this.remote = remote;
    }
}
