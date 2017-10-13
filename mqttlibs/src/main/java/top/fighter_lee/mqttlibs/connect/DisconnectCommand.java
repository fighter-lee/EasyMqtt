package top.fighter_lee.mqttlibs.connect;


import top.fighter_lee.mqttlibs.mqttv3.IMqttActionListener;
import top.fighter_lee.mqttlibs.mqttv3.MqttException;

/**
 * Created by fighter_lee on 2017/6/30.
 */

public class DisconnectCommand implements Command {

    public DisconnectCommand setQuiesceTimeout(long quiesceTimeout) {
        this.quiesceTimeout = quiesceTimeout;
        return this;
    }

    private long quiesceTimeout;


    @Override
    public void execute(IMqttActionListener listener) throws MqttException {
        MqttManager.getInstance().getConnect().getClient()
                .disconnect(quiesceTimeout);
    }
}
