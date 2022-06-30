package com.sopan.social_login;

import android.app.Application;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        /*if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }*/

        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(this));

        /*final String hashKey = new String(Base64.encode(md.digest(), 0));
        Log.i("AppLog", "key:" + hashKey + "=");*/
        AppEventsLogger.activateApp(this);

        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

}
