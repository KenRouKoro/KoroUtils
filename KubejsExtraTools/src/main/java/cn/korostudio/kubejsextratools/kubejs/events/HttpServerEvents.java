package cn.korostudio.kubejsextratools.kubejs.events;

import cn.korostudio.kubejsextratools.kubejs.events.eventJS.PathEventJS;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.Extra;


public class HttpServerEvents {
    public static EventGroup GROUP = EventGroup.of("HttpServerEvents");
    public static EventHandler PATH = GROUP.server("path", () -> PathEventJS.class).extra(Extra.STRING);

}
