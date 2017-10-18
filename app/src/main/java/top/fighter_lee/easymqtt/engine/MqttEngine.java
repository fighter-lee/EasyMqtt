package top.fighter_lee.easymqtt.engine;

import android.os.Build;
import android.text.TextUtils;

import com.adups.trace.Trace;

import top.fighter_lee.mqttlibs.connect.ConnectCommand;
import top.fighter_lee.mqttlibs.connect.DisconnectCommand;
import top.fighter_lee.mqttlibs.connect.MqttManager;
import top.fighter_lee.mqttlibs.connect.PubCommand;
import top.fighter_lee.mqttlibs.connect.SubCommand;
import top.fighter_lee.mqttlibs.connect.UnsubCommand;
import top.fighter_lee.mqttlibs.mqttv3.IMqttActionListener;
import top.fighter_lee.mqttlibs.mqttv3.IMqttToken;
import top.fighter_lee.mqttlibs.mqttv3.MqttException;

/**
 * Created by fighter_lee on 2017/10/17.
 */

public class MqttEngine {

    private static final String TAG = "MqttEngine";
    private static MqttEngine mqttEngine;

    private MqttEngine() {
    }

    public static MqttEngine getInstance() {
        if (null == mqttEngine)
            mqttEngine = new MqttEngine();
        return mqttEngine;
    }

    public void connect() throws MqttException {
        MqttManager.getInstance()
                .connect(new ConnectCommand()
                                .setClientId(getClientId())
                                .setServer("172.17.3.35")
                                .setPort(61613)
                                .setUserNameAndPassword("admin", "password")
                                .setKeepAlive(30)
                                .setTimeout(10)
                                .setCleanSession(false)
                        , new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Trace.d(TAG, "onSuccess() ");
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Trace.d(TAG, "onFailure() ");
                                Trace.e(TAG, exception);
                            }
                        });
    }

    public void disconnect() throws MqttException {
        MqttManager.getInstance().disConnect(new DisconnectCommand()
                .setQuiesceTimeout(10), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Trace.d(TAG, "onSuccess() ");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Trace.e(TAG, exception);
            }
        });
    }

    public void pub() throws MqttException {
        MqttManager.getInstance().pub(new PubCommand()
                .setMessage("哈哈哈，我来了")
                .setQos(1)
                .setTopic("/fighter-lee.top/mqttlibs")
                .setRetained(false), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Trace.d(TAG, "onSuccess() ");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Trace.e(TAG, exception);
            }
        });
    }

    public void sub() throws MqttException {
        MqttManager.getInstance().sub(new SubCommand()
                .setQos(1)
                .setTopic("/fighter-lee.top/mqttlibs"), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Trace.d(TAG, "onSuccess() ");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Trace.e(TAG, exception);
            }
        });
    }

    public void unsub() throws MqttException {
        MqttManager.getInstance().unSub(new UnsubCommand()
                .setTopic("/fighter-lee.top/mqttlibs"), new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Trace.d(TAG, "onSuccess() ");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Trace.e(TAG, exception);
            }
        });
    }

    private String getClientId() {
        Trace.d(TAG, "getClientId() " + Build.SERIAL);
        String clientId = Build.SERIAL;
        if (TextUtils.isEmpty(clientId)) {
            clientId = "222222";
        }
        return clientId;
    }
}
