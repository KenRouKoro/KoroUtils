package cn.korostudio.kubejsextratools.kubejs.events;

import cn.hutool.http.server.HttpServerRequest;
import cn.hutool.http.server.HttpServerResponse;
import dev.latvian.mods.kubejs.event.EventJS;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PathEventJS extends EventJS {
    protected HttpServerRequest request;
    protected HttpServerResponse response;

    public PathEventJS(HttpServerRequest request,HttpServerResponse response){
        this.request = request;
        this.response = response;
    }
}
