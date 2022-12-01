package cn.korostudio.kubejsextratools.kubejs.bindings;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.server.SimpleServer;
import cn.hutool.http.server.action.Action;
import cn.korostudio.kubejsextratools.kubejs.data.HttpServer;
import cn.korostudio.kubejsextratools.kubejs.events.HttpServerEvents;
import cn.korostudio.kubejsextratools.kubejs.events.PathEventJS;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.Function;
import dev.latvian.mods.rhino.FunctionObject;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.Arrays;
@Slf4j
public class HttpServerBindings {
    public static HttpServer httpServer = null;
    public static Thread serverThread = null;
    public static HttpServer createServer(int port){
        if (httpServer!=null){
            log.warn("已存在现有的HttpServer");
            return httpServer;
        }
        SimpleServer server = HttpUtil.createServer(port);
        httpServer = new HttpServer();
        httpServer.setServer(server);
        return httpServer;
    }

    public static void register(String path){
        httpServer.getServer().addAction(path,(request, response)->{
            HttpServerEvents.PATH.post(path,new PathEventJS(request,response));
        });
    }

    public static void write(PathEventJS pathEventJS,String write,String type){
        pathEventJS.getResponse().write(write,type);
    }

    public static boolean hasServer(){
        return httpServer!=null;
    }

    public static void start(){
        if(serverThread!=null){
            return;
        }
        serverThread = new Thread(()->{
            httpServer.getServer().start();
        });
        serverThread.start();
    }
    public static void stop(){
        ThreadUtil.execute(()->{
            httpServer.getServer().getRawServer().stop(1024);
            httpServer = null;
            serverThread = null;
        });

    }
    public static void setRoot(String root){
        httpServer.getServer().setRoot(root);
    }
    public static String getRuntimeLocation(){
        return System.getProperty("user.dir");
    }
}
