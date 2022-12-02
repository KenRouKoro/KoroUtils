package cn.korostudio.kubejsextratools.kubejs.events;

import cn.korostudio.kubejsextratools.kubejs.events.eventJS.PathEventJS;
import cn.korostudio.kubejsextratools.kubejs.events.eventJS.WebSocketEventJS;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.Extra;

public class WebSocketEvents {
    public static EventGroup GROUP = EventGroup.of("WebSocketEvents");
    public static EventHandler EVENT = GROUP.server("event", () -> WebSocketEventJS.class).extra(Extra.STRING);

}
