package top.fighter_lee.mqttlibs.connect;


import top.fighter_lee.mqttlibs.mqttv3.IMqttActionListener;
import top.fighter_lee.mqttlibs.mqttv3.MqttException;

/**
 * Created by fighter_lee on 2017/6/30.
 */

public interface Command {

    void execute(IMqttActionListener listener) throws MqttException;

}
