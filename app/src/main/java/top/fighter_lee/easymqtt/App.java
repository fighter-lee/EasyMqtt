package top.fighter_lee.easymqtt;

import android.app.Application;

import com.adups.trace.Trace;

import top.fighter_lee.mqttlibs.connect.MqttManager;

/**
 * Created by fighter_lee on 2017/10/18.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Trace.setLevel(Trace.DEBUG);
        Trace.write_file(false);
        MqttManager.getInstance().setContext(getApplicationContext());
    }
}
