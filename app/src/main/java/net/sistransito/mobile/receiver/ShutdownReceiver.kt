package net.sistransito.mobile.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import net.sistransito.mobile.appconstants.AppConstants
import net.sistransito.mobile.appobject.AppObject

class ShutdownReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val appContext = context.applicationContext
        Log.d("ShutdownReceiver", "onReceive called with action: ${intent.action}")
        AppObject.getTinyDB(appContext).putBoolean(AppConstants.isLogin, false)
    }
}
