package ru.andreev_av.weather.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    private static ConnectionDetector mInstance;
    private Context mContext;

    private ConnectionDetector(Context context) {
        mContext = context;
    }

    public static ConnectionDetector getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ConnectionDetector(context);
        }
        return mInstance;
    }

    public boolean isNetworkAvailableAndConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}
