package cn.korostudio.kubejsextratools.kubejs.events;

import cn.korostudio.kubejsextratools.kubejs.events.eventJS.MQTTEventJS;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.Extra;

public class MQTTEvents {
    public static EventGroup GROUP = EventGroup.of("MQTTEvents");
    public static EventHandler EVENT = GROUP.server("event", () -> MQTTEventJS.class).extra(Extra.STRING);
}
