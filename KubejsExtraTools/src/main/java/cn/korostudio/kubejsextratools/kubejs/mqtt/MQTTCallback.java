package cn.korostudio.kubejsextratools.kubejs.mqtt;

import cn.korostudio.kubejsextratools.kubejs.events.MQTTEvents;
import cn.korostudio.kubejsextratools.kubejs.events.eventJS.MQTTEventJS;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

public class MQTTCallback implements MqttCallback {
    @Override
    public void disconnected(MqttDisconnectResponse disconnectResponse) {
        MQTTEventJS eventJS = new MQTTEventJS();
        eventJS.setDisconnectResponse(disconnectResponse);
        MQTTEvents.EVENT.post("disconnected",eventJS);
    }

    @Override
    public void mqttErrorOccurred(MqttException exception) {
        MQTTEventJS eventJS = new MQTTEventJS();
        MQTTEvents.EVENT.post("mqttErrorOccurred",eventJS);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        MQTTEventJS eventJS = new MQTTEventJS();
        eventJS.setTopic(topic);
        eventJS.setMessage(message);
        MQTTEvents.EVENT.post("messageArrived",eventJS);
    }

    @Override
    public void deliveryComplete(IMqttToken token) {
        MQTTEventJS eventJS = new MQTTEventJS();
        eventJS.setToken(token);
        MQTTEvents.EVENT.post("deliveryComplete",eventJS);
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        MQTTEventJS eventJS = new MQTTEventJS();
        eventJS.setReconnect(reconnect);
        eventJS.setServerURI(serverURI);
        MQTTEvents.EVENT.post("connectComplete",eventJS);
    }

    @Override
    public void authPacketArrived(int reasonCode, MqttProperties properties) {
        MQTTEventJS eventJS = new MQTTEventJS();
        eventJS.setReasonCode(reasonCode);
        eventJS.setProperties(properties);
        MQTTEvents.EVENT.post("authPacketArrived",eventJS);
    }
}
