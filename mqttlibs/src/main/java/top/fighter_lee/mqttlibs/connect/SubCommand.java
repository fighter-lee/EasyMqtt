package top.fighter_lee.mqttlibs.connect;


import top.fighter_lee.mqttlibs.mqttv3.IMqttActionListener;
import top.fighter_lee.mqttlibs.mqttv3.MqttException;

/**
 * Created by fighter_lee on 2017/6/30.
 */

public class SubCommand implements Command {

    private String mTopic;
    private int mQos;

    public SubCommand setTopic(String topic) {
        this.mTopic = topic;
        return this;
    }

    public SubCommand setQos(int qos) {
        this.mQos = qos;
        return this;
    }

    @Override
    public void execute(IMqttActionListener listener) throws MqttException {
        MqttManager.getInstance().getConnect().getClient()
                .subscribe(mTopic, mQos, null, listener);
    }
}
