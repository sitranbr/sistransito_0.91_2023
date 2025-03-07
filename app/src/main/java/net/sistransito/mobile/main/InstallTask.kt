package net.sistransito.mobile.main

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.sistransito.mobile.appobject.AppObject
import net.sqlcipher.database.SQLiteDatabase

class InstallTask(
    private val activity: LifecycleOwner,
    private val context: Context,
    private val updateProgress: (Float) -> Unit,
    private val onComplete: () -> Unit
) {

    init {
        activity.lifecycleScope.launch {
            activity.repeatOnLifecycle(Lifecycle.State.STARTED) {
                executeTask()
            }
        }
    }

    private suspend fun executeTask() {
        updateProgress(0f)

        try {
            withContext(Dispatchers.IO) {
                SQLiteDatabase.loadLibs(context)
            }
            updateProgress(80f)
            AppObject.newInstance(context.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        updateProgress(100f)
        onComplete()
    }
}
