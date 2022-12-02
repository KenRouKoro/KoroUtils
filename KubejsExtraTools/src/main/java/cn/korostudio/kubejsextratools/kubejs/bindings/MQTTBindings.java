package cn.korostudio.kubejsextratools.kubejs.bindings;

import cn.korostudio.kubejsextratools.kubejs.mqtt.MQTTCallback;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;

@Slf4j
public class MQTTBindings {

    protected static MqttClient mqttClient = null;
    protected static MemoryPersistence persistence = null;
    protected static MqttConnectionOptions connOpts;

    public static void creatConnect(String broker,String clientId) {
        if(mqttClient!=null){
            return;
        }
        persistence = new MemoryPersistence();
        try {
            mqttClient = new MqttClient(broker, clientId, persistence);
            connOpts = new MqttConnectionOptions();
            mqttClient.setCallback(new MQTTCallback());
        } catch (MqttException e) {
            log.error("clientId为空或长度大于65535个字符!", e);
        }
    }
    public static void setServerURIs(String[] urIs){
        if (connOpts == null){
            return;
        }
        connOpts.setServerURIs(urIs);
    }
    public static void setCleanSession(boolean b){
        if (connOpts == null){
            return;
        }
        connOpts.setCleanStart(b);
    }
    public static void setPassword(String password){
        if (connOpts == null){
            return;
        }
        connOpts.setPassword(password.getBytes());
    }
    public static void setUsername(String username){
        if (connOpts == null){
            return;
        }
        connOpts.setUserName(username);
    }
    public static void setAutomaticReconnect(boolean b){
        if (connOpts == null){
            return;
        }
        connOpts.setAutomaticReconnect(b);
    }
    public static void setMaxInflight(int maxInflight){
        if (connOpts == null){
            return;
        }
        connOpts.setReceiveMaximum(maxInflight);
    }
    public static void setKeepalive(int keepalive){
        if (connOpts == null){
            return;
        }
        connOpts.setKeepAliveInterval(keepalive);
    }
    public static void setWillPayloadAndTopic(int qos,String topic,String payload,boolean retain){
        if (connOpts == null){
            return;
        }
        MqttMessage willMessage = new MqttMessage(payload.getBytes(), qos, retain, null);
        connOpts.setWill(topic, willMessage);
    }
    public static void connect(){
        if (mqttClient==null){
            return;
        }
        try {
            mqttClient.connect(connOpts);
        } catch (MqttException e) {
            log.error("MQTT 连接失败!",e);
        }
    }
    public static void disconnect(){
        if (mqttClient==null){
            return;
        }
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            log.error("MQTT 关闭异常!",e);
        }
    }
}
