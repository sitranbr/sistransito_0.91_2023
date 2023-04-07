package net.sistransito.mobile.appobject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.sistransito.Application;
import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.ait.lister.AitLister;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.TinyDB;
import net.sistransito.mobile.http.CustomHttpClient;
import net.sistransito.mobile.plate.lister.PlateLister;
import net.sistransito.mobile.rrd.lister.RrdLister;
import net.sistransito.mobile.tav.lister.TavLister;
import net.sistransito.mobile.tca.lister.TcaLister;
import net.sistransito.R;

import java.util.HashMap;
import java.util.Map;

public class AppObject {
    private static Vibrator vibrator;
    private static MediaPlayer mPlayer = null;
    private static CustomHttpClient customHttpClient;
    private static Animation slideAndScaleAnimation = null;
    private static Typeface tfNormal, tfCurrent, tfTitle;
    private static TinyDB tinyDB;

    public static Intent getPlateListIntent(Context context) {
        if (plateListIntent == null)
            plateListIntent = new Intent(context, PlateLister.class);
        return plateListIntent;
    }

    public static Intent getAitListerIntent(Context context) {
        if (aitListerIntent == null)
            aitListerIntent = new Intent(context, AitLister.class);

        return aitListerIntent;
    }

    public static  Intent getRrdListerIntent(Context context) {
        if (rrdListerIntent == null)
            rrdListerIntent = new Intent(context, RrdLister.class);
        return rrdListerIntent;
    }

    public  static Intent getTcaListerIntent(Context context) {
        if (tcaListerIntent == null)
            tcaListerIntent = new Intent(context, TcaLister.class);
        return tcaListerIntent;
    }

    public static Intent getTavListerIntent(Context context) {
        if (tavListerIntent == null)
            tavListerIntent = new Intent(context, TavLister.class);
        return tavListerIntent;
    }

    private static Intent plateListIntent, aitListerIntent, rrdListerIntent, tcaListerIntent, tavListerIntent;


    public static TinyDB getTinyDB(Context c) {
        if (tinyDB == null)
            tinyDB = new TinyDB(c);
        return tinyDB;
    }

    public static void newInstance(Context context) {
        getFont(context);
        slideAndScaleAnimation = AnimationUtils.loadAnimation(context,
                R.anim.slide_and_scale);

        customHttpClient = new CustomHttpClient();
        mPlayer = MediaPlayer.create(context, R.raw.right_answer);
        vibrator = (Vibrator) context
                .getSystemService(Service.VIBRATOR_SERVICE);
        tinyDB = new TinyDB(context);

        plateListIntent = new Intent(Application.getAppContext(), PlateLister.class);
        aitListerIntent = new Intent(Application.getAppContext(), AitLister.class);
        rrdListerIntent = new Intent(Application.getAppContext(), RrdLister.class);
        tcaListerIntent = new Intent(Application.getAppContext(), TcaLister.class);
        tavListerIntent = new Intent(Application.getAppContext(), TavLister.class);
    }

    public static Typeface getTf_normal(Context context) {
        if (tfNormal == null)
            tfNormal = Typeface.createFromAsset(context.getAssets(),
                    "font/MuseoSans-300.ttf");
        return tfNormal;
    }

    public static Typeface getCurrentFont(Context context) {
        if (tfCurrent == null) {
            getFont(context);
        }
        return tfCurrent;
    }

    public static void getFont1(Context context) {

        String font = (DatabaseCreator.getSettingDatabaseAdapter(context))
                .getFont();

        if ((font.equals(AppConstants.FONT_1))) {
            tfCurrent = Typeface.createFromAsset(context.getAssets(),
                    "font/Roboto-Regular.ttf");
        } else if ((font.equals(AppConstants.FONT_2))) {
            tfCurrent = Typeface.createFromAsset(context.getAssets(),
                    "font/Roboto-Regular.ttf");
        } else if ((font.equals(AppConstants.FONT_3))) {
            tfCurrent = Typeface.createFromAsset(context.getAssets(),
                    "font/Roboto-Regular.ttf");
        } else {
            tfCurrent = Typeface.DEFAULT;
        }
    }

    public static void getFont(Context context) {
        String font = DatabaseCreator.getSettingDatabaseAdapter(context).getFont();
        Map<String, String> fontMap = new HashMap<>();
        fontMap.put(AppConstants.FONT_1, "font/Roboto-Regular.ttf");
        fontMap.put(AppConstants.FONT_2, "font/Roboto-Regular.ttf");
        fontMap.put(AppConstants.FONT_3, "font/Roboto-Regular.ttf");

        String fontPath = fontMap.getOrDefault(font, null);

        if (fontPath != null) {
            tfCurrent = Typeface.createFromAsset(context.getAssets(), fontPath);
        } else {
            tfCurrent = Typeface.DEFAULT;
        }
    }

    public static Typeface getFontTitle(Context context) {

        tfTitle = Typeface.createFromAsset(context.getAssets(),
                "font/Roboto-Regular.ttf");
        return tfTitle;

    }

    public static void setFont(Context context, String font) {
        (DatabaseCreator.getSettingDatabaseAdapter(context)).setFont(font);
        getFont(context);
    }

    public static Animation getSlideAndScaleAnimation(Context context) {
        if (slideAndScaleAnimation == null) {
            slideAndScaleAnimation = AnimationUtils.loadAnimation(context,
                    R.anim.slide_and_scale);
            return slideAndScaleAnimation;
        }
        return slideAndScaleAnimation;

    }

    public static CustomHttpClient getHttpClient() {
        if (customHttpClient == null) {
            customHttpClient = new CustomHttpClient();
        }
        return customHttpClient;
    }

    public static void startWarning(Context context) {
        if (mPlayer == null) {
            mPlayer = MediaPlayer.create(context, R.raw.right_answer);
        }
        mPlayer.start();
    }

    public static void startVibrate(Context context) {
        vibrator = (Vibrator) context
                .getSystemService(Service.VIBRATOR_SERVICE);
        vibrator.vibrate(3000);
    }
}
