package net.sistransito.mobile.appobject

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import net.sistransito.R
import net.sistransito.mobile.ait.lister.AitLister
import net.sistransito.mobile.appconstants.AppConstants
import net.sistransito.mobile.database.DatabaseCreator
import net.sistransito.mobile.database.TinyDB
import net.sistransito.mobile.http.CustomHttpClient
import net.sistransito.mobile.plate.lister.PlateLister
import net.sistransito.mobile.rrd.lister.RrdLister
import net.sistransito.mobile.tav.lister.TavLister
import net.sistransito.mobile.tca.lister.TcaLister

object AppObject {
    private var vibrator: Vibrator? = null
    private var mPlayer: MediaPlayer? = null
    private var customHttpClient: CustomHttpClient? = null
    private var slideAndScaleAnimation: Animation? = null
    private var tfNormal: Typeface? = null
    private var tfCurrent: Typeface? = null
    private var tfTitle: Typeface? = null
    private var tinyDB: TinyDB? = null

    private var plateListIntent: Intent? = null
    private var aitListerIntent: Intent? = null
    private var rrdListerIntent: Intent? = null
    private var tcaListerIntent: Intent? = null
    private var tavListerIntent: Intent? = null

    @JvmStatic
    fun getPlateListIntent(context: Context): Intent {
        if (plateListIntent == null) {
            plateListIntent = Intent(context, PlateLister::class.java)
        }
        return plateListIntent!!
    }

    @JvmStatic
    fun getAitListerIntent(context: Context): Intent {
        if (aitListerIntent == null) {
            aitListerIntent = Intent(context, AitLister::class.java)
        }
        return aitListerIntent!!
    }

    @JvmStatic
    fun getRrdListerIntent(context: Context): Intent {
        if (rrdListerIntent == null) {
            rrdListerIntent = Intent(context, RrdLister::class.java)
        }
        return rrdListerIntent!!
    }

    @JvmStatic
    fun getTcaListerIntent(context: Context): Intent {
        if (tcaListerIntent == null) {
            tcaListerIntent = Intent(context, TcaLister::class.java)
        }
        return tcaListerIntent!!
    }

    @JvmStatic
    fun getTavListerIntent(context: Context): Intent {
        if (tavListerIntent == null) {
            tavListerIntent = Intent(context, TavLister::class.java)
        }
        return tavListerIntent!!
    }

    @JvmStatic
    fun getTinyDB(context: Context): TinyDB {
        if (tinyDB == null) {
            tinyDB = TinyDB(context.applicationContext)
        }
        return tinyDB!!
    }

    @JvmStatic
    fun newInstance(context: Context) {
        getFont(context)
        slideAndScaleAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_and_scale)
        customHttpClient = CustomHttpClient()
        mPlayer = MediaPlayer.create(context, R.raw.right_answer)
        vibrator = getVibrator(context)

        plateListIntent = Intent(context.applicationContext, PlateLister::class.java)
        aitListerIntent = Intent(context.applicationContext, AitLister::class.java)
        rrdListerIntent = Intent(context.applicationContext, RrdLister::class.java)
        tcaListerIntent = Intent(context.applicationContext, TcaLister::class.java)
        tavListerIntent = Intent(context.applicationContext, TavLister::class.java)
    }

    private fun getVibrator(context: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }

    @JvmStatic
    fun getTfNormal(context: Context): Typeface {
        if (tfNormal == null) {
            tfNormal = Typeface.createFromAsset(context.assets, "font/MuseoSans-300.ttf")
        }
        return tfNormal!!
    }

    @JvmStatic
    fun getCurrentFont(context: Context): Typeface {
        if (tfCurrent == null) {
            getFont(context)
        }
        return tfCurrent!!
    }

    private fun getFont(context: Context) {
        val font = DatabaseCreator.getSettingDatabaseAdapter(context).getFont()
        val fontMap = mapOf(
            AppConstants.FONT_1 to "font/Roboto-Regular.ttf",
            AppConstants.FONT_2 to "font/Roboto-Regular.ttf",
            AppConstants.FONT_3 to "font/Roboto-Regular.ttf"
        )
        val fontPath = fontMap[font]
        tfCurrent = if (fontPath != null) {
            Typeface.createFromAsset(context.assets, fontPath)
        } else {
            Typeface.DEFAULT
        }
    }

    @JvmStatic
    fun getFontTitle(context: Context): Typeface {
        if (tfTitle == null) {
            tfTitle = Typeface.createFromAsset(context.assets, "font/Roboto-Regular.ttf")
        }
        return tfTitle!!
    }

    @JvmStatic
    fun setFont(context: Context, font: String) {
        DatabaseCreator.getSettingDatabaseAdapter(context).setFont(font)
        getFont(context)
    }

    @JvmStatic
    fun getSlideAndScaleAnimation(context: Context): Animation {
        if (slideAndScaleAnimation == null) {
            slideAndScaleAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_and_scale)
        }
        return slideAndScaleAnimation!!
    }

    @JvmStatic
    fun getHttpClient(): CustomHttpClient {
        if (customHttpClient == null) {
            customHttpClient = CustomHttpClient()
        }
        return customHttpClient!!
    }

    @JvmStatic
    fun startWarning(context: Context) {
        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(context, R.raw.right_answer)
        }
        mPlayer?.start()
    }

    @JvmStatic
    fun startVibrate(context: Context) {
        if (vibrator == null) {
            vibrator = getVibrator(context)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator?.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator?.vibrate(3000)
        }
    }
}
