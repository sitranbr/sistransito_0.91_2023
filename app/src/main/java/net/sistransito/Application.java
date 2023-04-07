package net.sistransito;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

//It is used to define the application's global context.
public class Application extends MultiDexApplication {

    private static Context context;

    //Initializes the application context
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    //It is used to attach the context of the current activity to the application's global context
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    //Returns the application context
    public static Context getAppContext() {
        return context;
    }

}
