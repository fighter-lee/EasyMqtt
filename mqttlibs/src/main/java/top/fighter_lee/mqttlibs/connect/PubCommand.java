package top.fighter_lee.mqttlibs.connect;


import top.fighter_lee.mqttlibs.mqttv3.IMqttActionListener;
import top.fighter_lee.mqttlibs.mqttv3.MqttException;

/**
 * Created by fighter_lee on 2017/6/30.
 */

public class PubCommand implements Command {

    private String mTopic;
    private int mQos;
    private String mMsg;
    private boolean mRetained;

    /**
     * 创建MQTT相关的主题
     *
     * @param topic
     * @return
     */
    public PubCommand setTopic(String topic) {
        this.mTopic = topic;
        return this;
    }

    /**
     * QoS=0：最多一次，有可能重复或丢失。

     * QoS=1：至少一次，有可能重复。MessageId=x --->PUBLISH--> Server收到后，存储Message，发布，删除，向Client回发PUBACK
     * Client收到PUBACK后，删除Message；如果未收到PUBACK，设置DUP++，重新发送，Server端重新发布，所以有可能重复发送消息。
     *
     * QoS=2：只有一次，确保消息只到达一次（用于比较严格的计费系统）。
     * @param qos
     * @return
     */
    public PubCommand setQos(int qos) {
        this.mQos = qos;
        return this;
    }

    /**
     * 设置消息
     *
     * @param message
     * @return
     */
    public PubCommand setMessage(String message) {
        this.mMsg = message;
        return this;
    }

    /**
     * 设置是否在服务器中保存消息体
     * @param retained
     * @return
     */
    public PubCommand setRetained(boolean retained){
        this.mRetained = retained;
        return this;
    }

    @Override
    public void execute(IMqttActionListener listener) throws MqttException {
        MqttManager.getInstance().getConnect().getClient()
                .publish(mTopic, mMsg.getBytes(), mQos, mRetained, null, listener);
    }
}
