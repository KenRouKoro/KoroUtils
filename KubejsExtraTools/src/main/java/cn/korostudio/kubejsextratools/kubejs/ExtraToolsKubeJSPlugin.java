package cn.korostudio.kubejsextratools.kubejs;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.server.action.Action;
import cn.korostudio.kubejsextratools.kubejs.bindings.HttpClientBindings;
import cn.korostudio.kubejsextratools.kubejs.bindings.HttpServerBindings;
import cn.korostudio.kubejsextratools.kubejs.bindings.MQTTBindings;
import cn.korostudio.kubejsextratools.kubejs.bindings.WebSocketBindings;
import cn.korostudio.kubejsextratools.kubejs.events.HttpServerEvents;
import cn.korostudio.kubejsextratools.kubejs.events.MQTTEvents;
import cn.korostudio.kubejsextratools.kubejs.events.WebSocketEvents;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import dev.latvian.mods.rhino.util.wrap.TypeWrapperFactory;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;

import java.nio.charset.Charset;

public class ExtraToolsKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.allow("cn.korostudio");
        filter.allow("cn.hutool.core");
        filter.allow("cn.hutool.http");
        filter.allow("cn.hutool.http.server.action.Action");
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("ThreadUtil",ThreadUtil.class);
        event.add("HttpClient", HttpClientBindings.class);
        event.add("HttpServer", HttpServerBindings.class);
        event.add("Action", Action.class);
        event.add("ContentType", ContentType.class);
        event.add("HtmlUtil", HtmlUtil.class);
        event.add("ReUtil", ReUtil.class);
        event.add("FileWriter", FileWriter.class);
        event.add("FileReader", FileReader.class);
        event.add("Assert", Assert.class);
        event.add("Charset", Charset.class);
        event.add("WebSocket", WebSocketBindings.class);
        event.add("MQTT", MQTTBindings.class);
    }

    @Override
    public void registerEvents() {
        HttpServerEvents.GROUP.register();
        WebSocketEvents.GROUP.register();
        MQTTEvents.GROUP.register();
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
    }
}
