package top.fighter_lee.mqttlibs.connect;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.adups.trace.Trace;

import java.io.InputStream;

import top.fighter_lee.mqttlibs.mqtt_service.MqttAndroidClient;
import top.fighter_lee.mqttlibs.mqtt_service.MqttTraceHandler;
import top.fighter_lee.mqttlibs.mqttv3.IMqttActionListener;
import top.fighter_lee.mqttlibs.mqttv3.MqttCallback;
import top.fighter_lee.mqttlibs.mqttv3.MqttConnectOptions;
import top.fighter_lee.mqttlibs.mqttv3.MqttException;
import top.fighter_lee.mqttlibs.mqttv3.MqttSecurityException;

/**
 * Created by fighter_lee on 2017/6/30.
 */

public class ConnectCommand implements Command {

    private static final String TAG = "ConnectCommand";
    private String mServer;
    private int mPort;
    private String lastWillMsg;
    private String lastWillTopic;
    private int mTimeout;
    private int mKeepAlive;
    private String mUserName;
    private String mUserPassword;
    private boolean mCleanSession;
    private String mClientId;
    private String mSslKeyPath;
    private String mSslKeyPassword;
    private MqttAndroidClient client;
    private int lastWillQos;
    private boolean lastWillRetained;
    private MqttCallback mMessageListener;
    private boolean traceEnabled = false;
    private MqttTraceHandler mTraceCallback;
    private static ConnectCommand connectCommand;

    public static ConnectCommand getInstance() {
        if (connectCommand == null)
            synchronized (ConnectCommand.class) {
                if (connectCommand == null)
                    connectCommand = new ConnectCommand();
            }
        return connectCommand;
    }

    public ConnectCommand setClientId(String clientId) {
        this.mClientId = clientId;
        return this;
    }

    public ConnectCommand setServer(String server) {
        this.mServer = server;
        return this;
    }

    public ConnectCommand setPort(int port) {
        this.mPort = port;
        return this;
    }

    public ConnectCommand setLastWill(String msg, String topic, int qos, boolean retained) {
        this.lastWillMsg = msg;
        this.lastWillTopic = topic;
        this.lastWillQos = qos;
        this.lastWillRetained = retained;
        return this;
    }

    /**
     * 连接超时时间(单位：秒)
     *
     * @param timeout
     * @return
     */
    public ConnectCommand setTimeout(int timeout) {
        this.mTimeout = timeout;
        return this;
    }

    /**
     * 心跳时间（单位秒）
     *
     * @return
     */
    public ConnectCommand setKeepAlive(int keepAlive) {
        this.mKeepAlive = keepAlive;
        return this;
    }

    /**
     * 1) client 连接到 broker 时设置 clean session flag 为 false， client 与 broker
     * 间建立了一个长连接(Durable connnecton); 当 client 断开与 broker 的连接时，
     * 任何 subscriptions 还有剩余的 QoS 为 1 或 2 的 messages，
     * 那么会保存这些 subscriptions/messages 直到再次连接。
     * <p>
     * 2) 如果 clean session flag 为 true，就相当于 'session start', 当 disconnect 时,
     * 会移除这个 client 所有的 subscriptions.
     *
     * @param cleanSession
     * @return
     */
    public ConnectCommand setCleanSession(boolean cleanSession) {
        this.mCleanSession = cleanSession;
        return this;
    }

    /**
     * 设置用户名和密码
     *
     * @return
     */
    public ConnectCommand setUserNameAndPassword(String userName, String password) {
        this.mUserName = userName;
        this.mUserPassword = password;
        return this;
    }

    public MqttAndroidClient getClient() {
        return client;
    }

    /**
     * ssl连接
     *
     * @param sslKeyPath
     * @param sslKeyPassword
     * @return
     */
    public ConnectCommand setSsl(String sslKeyPath, String sslKeyPassword) {
        this.mSslKeyPath = sslKeyPath;
        this.mSslKeyPassword = sslKeyPassword;
        return this;
    }


    /**
     * 是否显示相关日志
     *
     * @return
     */
    public ConnectCommand setTraceEnabled(boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
        return this;
    }

    /**
     * 日志回调（需要setTraceEnabled（true））
     *
     * @param traceCallback
     * @return
     */
    public ConnectCommand setTraceCallback(MqttTraceHandler traceCallback) {
        this.mTraceCallback = traceCallback;
        return this;
    }

    public void setMessageListener(MqttCallback listener) {
        this.mMessageListener = listener;
    }

    public void connect() {

    }

    private String getUri() {
        String uri;
        if (TextUtils.isEmpty(mSslKeyPath)) {
            uri = "tcp://";
        } else {
            uri = "ssl://";
        }

        uri = uri + mServer + ":" + mPort;
        return uri;
    }

    private MqttAndroidClient createClient(Context context, String serverURI, String clientId) {
        MqttAndroidClient client = new MqttAndroidClient(context, serverURI, clientId);
        return client;
    }

    @Override
    public void execute(IMqttActionListener listener) throws MqttException {

        MqttConnectOptions conOpt = new MqttConnectOptions();

        String uri = getUri();

        if (null == client) {
            client = createClient(MqttManager.sCx, uri, mClientId);
        }

        if (!TextUtils.isEmpty(mSslKeyPath)) {
            try {
                InputStream key = this.getClass().getResourceAsStream(mSslKeyPath);
                conOpt.setSocketFactory(client.getSSLSocketFactory(key,
                        mSslKeyPassword));

            } catch (MqttSecurityException e) {
                Log.e(this.getClass().getCanonicalName(),
                        "MqttException Occured: ", e);
            }
        }

        conOpt.setCleanSession(mCleanSession);
        conOpt.setConnectionTimeout(mTimeout);
        conOpt.setKeepAliveInterval(mKeepAlive);
        if (!TextUtils.isEmpty(mUserName)) {
            conOpt.setUserName(mUserName);
        }
        if (!TextUtils.isEmpty(mUserPassword)) {
            conOpt.setPassword(mUserPassword.toCharArray());
        }

        boolean doConnect = true;

        if ((!TextUtils.isEmpty(lastWillMsg))
                || (!TextUtils.isEmpty(lastWillTopic))) {
            try {
                conOpt.setWill(lastWillTopic, lastWillMsg.getBytes(), lastWillQos, lastWillRetained);
            } catch (Exception e) {
                Trace.d(TAG, "connect() Exception Occured：" + e.toString());
                doConnect = false;
            }
        }

        client.setCallback(mMessageListener);

        if (traceEnabled) {
            client.setTraceCallback(mTraceCallback);
        }

        if (doConnect) {
            try {
                client.connect(conOpt, null, listener);
            } catch (MqttException e) {
                Trace.d(TAG, "connect() MqttException Occured:" + e.toString());
            }
        }
    }
}
