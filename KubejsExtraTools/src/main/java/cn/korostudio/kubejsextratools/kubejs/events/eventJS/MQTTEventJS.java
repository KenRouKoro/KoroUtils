package cn.korostudio.kubejsextratools.kubejs.events.eventJS;

import dev.latvian.mods.kubejs.event.EventJS;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

@Getter
@Setter
public class MQTTEventJS extends EventJS {
    MqttDisconnectResponse disconnectResponse;
    String topic;
    MqttMessage message;
    IMqttToken token;
    boolean reconnect;
    String serverURI;
    int reasonCode;
    MqttProperties properties;
}
