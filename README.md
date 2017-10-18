### 如何使用
Androidstudio添加如下依赖：

	compile 'top.fighter-lee:mqttlibs:1.0.1'

####搭建服务器
> 如果有自己的mqtt服务器的话，请跳过此步骤。

1. 请[点击](http://activemq.apache.org/apollo/download.html),下载Apollo服务器，安装。
2. 命令行进入安装目录bin目录下。

	    D:
    	cd D:\develop\tools\apache-apollo-1.7.1\bin

3. 输入apollo create XXX（xxx为创建的服务器实例名称，例：apollo create mybroker），之后会在bin目录下创建名称为XXX的文件夹。XXX文件夹下etc\apollo.xml文件下是配置服务器信息的文件。etc\users.properties文件包含连接MQTT服务器时用到的用户名和密码，默认为admin=password，即账号为admin，密码为password，可自行更改。
4. 进入XXX/bin目录，输入apollo-broker.cmd run开启服务器，看到如下界面代表搭建完成
![](https://i.imgur.com/Dxq75Mt.png)
5. 在浏览器输入http://127.0.0.1:61680/，查看是否安装成功。
	![](https://i.imgur.com/ZfYkZU0.png)

#### 客户端编码

##### 连接

**先介绍API吧**。

ConnectCommand为连接操作类，可以设置相应属性。

1. setClientId()

	设置客户身份唯一标识

2. setServer()

	设置建立连接的域名或者服务器ip

3. setPort

	设置端口号

4. setUserNameAndPassword

	设置连接认证的用户名和密码

5. setKeepAlive

	设置保持长连接ping的频率，单位为秒，建议100

6. setTimeout

	设置操作超时时间。

7. setCleanSession

	设置cleansession，若为true，当 disconnect 时,会移除这个 client 所有的 subscriptions.

8. setSsl

	建立ssl长连接，若没有设置的话，默认为tcp长连接。

9. setLastWill

	设置遗愿消息，即当设备断开连接时会主动pub的消息。

10. setTraceEnabled

	是否打印日志，默认false

11. setTraceCallback

	监听日志回调，需要setTraceEnabled（true）



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

连接成功后在页面上显示如图：

![](https://i.imgur.com/96bbHu3.png)

需要**注意**几点：

1. clientId不同设备请设置不同的值。
2. server,若使用的是Apollo服务器，则为IPv4 地址(ipconfig/all获取)
3. port ,见下图：

	![](https://i.imgur.com/fc8zFPx.png)
4. setUserNameAndPassword，若使用的是Apollo服务器，则默认的用户名是admin，密码是：password。

####发送消息

**API**

1. setMessage

	设置消息内容

2. setQos

	设置qos，决定消息到达次数。

3. setTopic

	设置消息主题

4. setRetained

	服务器是否保存消息

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

#### 订阅消息主题

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

#### 取消订阅消息主题

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

#### 接收消息

	MqttManager.getInstance().registerMessageListener(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                Trace.e(TAG, cause);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Trace.d(TAG, "messageArrived() topic:"+topic);
                Trace.d(TAG, "messageArrived() message:"+message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });