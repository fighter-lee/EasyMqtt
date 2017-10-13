package top.fighter_lee.mqttlibs.connect;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by fighter_lee on 2017/9/28.
 */

public class Utils {

    public static boolean isNetWorkAvailable(Context context) {

        boolean ret = false;
        if (context == null) {
            return ret;
        }
        try {
            ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectManager == null) {
                return ret;
            }
            NetworkInfo[] infos = connectManager.getAllNetworkInfo();
            if (infos == null) {
                return ret;
            }
            for (int i = 0; i < infos.length && infos[i] != null; i++) {
                if (infos[i].isConnected() && infos[i].isAvailable()) {
                    ret = true;
                    break;
                }
            }
        } catch (Exception e) {

        }
        return ret;
    }

}
