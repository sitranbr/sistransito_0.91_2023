package net.sistransito

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import android.widget.LinearLayout
import com.rey.material.widget.ProgressView
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import net.sistransito.mobile.appconstants.AppConstants
import net.sistransito.mobile.appobject.AppObject
import net.sistransito.mobile.login.ActivityLogin
import net.sistransito.mobile.main.InstallTask
import net.sistransito.mobile.main.MainUserActivity

class MainActivity : AppCompatActivity() {

    private lateinit var linearLayout: LinearLayout
    private lateinit var mProgressBar: ProgressView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializedView()
        startViewAnimation()

        InstallTask(
            activity = this,
            context = this,
            updateProgress = { progress -> updateProgress(progress) },
            onComplete = { installWorkComplete() }
        )
    }

    private fun initializedView() {
        mProgressBar = findViewById(R.id.progressBar)
    }

    private fun startViewAnimation() {
        linearLayout = findViewById(R.id.linear_layout)
        linearLayout.startAnimation(AppObject.getSlideAndScaleAnimation(this))
    }

    private fun updateProgress(progress: Float) {
        mProgressBar.progress = progress
    }

    private fun installWorkComplete() {
        lifecycleScope.launch {
            delay(3000)
            if (AppObject.getTinyDB(this@MainActivity).getBoolean(AppConstants.isLogin, false)) {
                mProgressBar.stop()
                finish()
                startActivity(Intent(this@MainActivity, MainUserActivity::class.java))
            } else {
                mProgressBar.stop()
                finish()
                startActivity(Intent(this@MainActivity, ActivityLogin::class.java))
            }
        }
    }
}
