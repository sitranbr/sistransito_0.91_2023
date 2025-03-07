package net.sistransito

import android.content.Context
import androidx.multidex.MultiDexApplication

class Application : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        // Qualquer inicialização global pode ser feita aqui
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }
}
