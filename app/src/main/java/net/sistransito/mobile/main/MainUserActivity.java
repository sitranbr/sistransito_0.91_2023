package net.sistransito.mobile.main;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.gc.materialdesign.widgets.ProgressDialog;

import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import com.mikepenz.materialdrawer.widget.AccountHeaderView;
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.appobject.AppObject;
import net.sistransito.mobile.ait.AitActivity;
import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.ait.table.InfractionTable;
import net.sistransito.mobile.aitcompany.AitCompanyActivity;
import net.sistransito.mobile.aitcompany.pjData;
import net.sistransito.mobile.cnh.SearchCnhFragment;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.sync.ImageFragment;
import net.sistransito.mobile.equipments.AitEquipmentEntryJsonTask;
import net.sistransito.mobile.http.WebClient;
import net.sistransito.mobile.login.LoginData;
import net.sistransito.mobile.number.CheckNumber;
import net.sistransito.mobile.plate.search.ConsultPlateFragment;
import net.sistransito.mobile.plate.lister.LogListFragment;
import net.sistransito.mobile.profile.ProfileFragment;
import net.sistransito.mobile.rrd.RrdActivity;
import net.sistransito.mobile.rrd.RrdData;
import net.sistransito.mobile.setting.SettingFragment;
import net.sistransito.mobile.tav.TavActivity;
import net.sistransito.mobile.tav.TavData;
import net.sistransito.mobile.tca.TcaActivity;
import net.sistransito.mobile.tca.TcaData;
import net.sistransito.mobile.util.DialogMaterial;
import net.sistransito.mobile.util.User;
import net.sistransito.R;

import kotlin.jvm.functions.Function3;

public class MainUserActivity extends AppCompatActivity {
    //save our header or resul
    private Context context;
    private SQLiteDatabase database;
    private AccountHeaderView headerResult = null;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout root;

    private SharedPreferences preferences;
    //profile
    private IProfile profile;
    private User user;
    private LoginData loginData;

    private DialogMaterial dialogMaterial;
    private MaterialDrawerSliderView slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        slider = findViewById(R.id.slider);
        setImageLoader();
        startMenu(savedInstanceState);
        checkFirstTimeAppSetup();
        getPermission();

        /*try {
            Log.d("Senha em cash: ", dataLogin.getPassword() + " IDlogin: " + dataLogin.getIDLogin());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    /*@Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }*/

    private void setImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(500)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                // default
                .diskCacheSize(50 * 1024 * 1024).diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(defaultOptions)
                // default
                .build();
        ImageLoader.getInstance().init(config);

    }

    private void startMenu(Bundle savedInstanceState) {
        user = AppObject.getTinyDB(this).getObject(AppConstants.user, User.class);
        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        this.setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);



        toolbar.setNavigationIcon(R.drawable.ham_burger_menu);
        root = findViewById(R.id.root);
        actionBarDrawerToggle = new  ActionBarDrawerToggle(
                this,
                root,
                toolbar,
                com.mikepenz.materialdrawer.R.string.material_drawer_open,
                com.mikepenz.materialdrawer.R.string.material_drawer_close
        );

        root.addDrawerListener(actionBarDrawerToggle);

        //create account
        headerResult = new AccountHeaderView(this);
        headerResult.attachToSliderView(slider);
        headerResult.addProfile(getProfileDrawerItem(user.getCompanyName(),user.getCompanyName(),null),0);
        if(savedInstanceState != null){
            headerResult.saveInstanceState(savedInstanceState);
        }
        headerResult.setHeaderBackground(new ImageHolder(R.drawable.header));


        ImageLoader.getInstance().loadImage(user.getProfileImage(),
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        headerResult.removeProfile(0);
                        headerResult.addProfile(getProfileDrawerItem(user.getCompanyName(),user.getCompanyName(),loadedImage),0);
                    }
                });



        //    result.addItem(new DividerDrawerItem());
        slider.getItemAdapter().add(getPrimaryDrawerItem(R.string.renavan_search,R.mipmap.ic_i_car_p,0L));
        slider.getItemAdapter().add(getPrimaryDrawerItem(R.string.search_cnh,R.mipmap.ic_i_id_p,1L));

        slider.getItemAdapter().add(getPrimaryDrawerItem(R.string.document_history,R.mipmap.ic_log_n,2L));
        slider.getItemAdapter().add(new DividerDrawerItem());
        slider.getItemAdapter().add(getPrimaryDrawerItem(R.string.infraction_table,R.mipmap.ic_infraction,3L));
        slider.getItemAdapter().add(new DividerDrawerItem());
        slider.getItemAdapter().add(getPrimaryDrawerItem(R.string.setting,R.mipmap.ic_settting_nav,4L));
        slider.getItemAdapter().add(getPrimaryDrawerItem(R.string.profile,R.mipmap.ic_profile,5L));
        slider.getItemAdapter().add(getPrimaryDrawerItem(R.string.app_help,R.mipmap.ic_help_nav,6L));
        slider.getItemAdapter().add(new DividerDrawerItem());
        slider.getItemAdapter().add(getPrimaryDrawerItem(R.string.logout,R.mipmap.ic_logout,7L));


        // specify a click listener
        slider.setOnDrawerItemClickListener((view, iDrawerItem, integer) -> {


            onItemClick(iDrawerItem);

            return true;
        });



        slider.setSelection(0,true);
        //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ConsultPlateFragment.newInstance()).commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.btn_overflow_autuar) {
            AitData data = new AitData();
            data.setDataisNull(true);
            Intent intent = new Intent(this, AitActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(AitData.getAitID(), data);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (id == R.id.btn_overflow_autuar_pj) {
            pjData datapj = new pjData();
            datapj.setDataisPJNull(true);
            Intent intentPJ = new Intent(this, AitCompanyActivity.class);
            Bundle bundlePJ = new Bundle();
            bundlePJ.putSerializable(pjData.getIDAuto(), datapj);
            intentPJ.putExtras(bundlePJ);
            startActivity(intentPJ);
        } else if (id == R.id.btn_overflow_rrd_avulso) {
            RrdData dados = new RrdData();
            dados.setRrdType(getResources().getString(R.string.loose));
            Intent intents = new Intent(this, RrdActivity.class);
            Bundle bundles = new Bundle();
            bundles.putSerializable(RrdData.getRRDId(), dados);
            intents.putExtras(bundles);
            startActivity(intents);
        } else if (id == R.id.btn_overflow_tav_avulso) {
            TavData dadaTav = new TavData();
            dadaTav.setTavType(getResources().getString(R.string.loose));
            Intent intentTav = new Intent(this, TavActivity.class);
            Bundle bundleTav = new Bundle();
            bundleTav.putSerializable(TavData.getTavId(), dadaTav);
            intentTav.putExtras(bundleTav);
            startActivity(intentTav);
        } else if (id == R.id.btn_overflow_tca_avulso) {
            TcaData dadaTca = new TcaData();
            dadaTca.setTcaType(getResources().getString(R.string.loose));
            Intent intentTca = new Intent(this, TcaActivity.class);
            Bundle bundleTca = new Bundle();
            bundleTca.putSerializable(TcaData.getTcaId(), dadaTca);
            intentTca.putExtras(bundleTca);
            startActivity(intentTca);
        } else if (id == R.id.btn_overflow_autos_restantes) {
            DialogMaterial.getBottomSheet(DatabaseCreator
                    .getBalanceDatabaseAdapter(this)
                    .getShowView(), Color.BLACK, this).show();
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onItemClick(IDrawerItem drawerItem) {

        if (drawerItem != null && drawerItem instanceof Nameable) {
            int title = ((Nameable) drawerItem).getName().getTextRes();
            getSupportActionBar().setTitle(title);
            if (title == R.string.renavan_search) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ConsultPlateFragment.newInstance()).commit();
            } else if (title == R.string.search_cnh) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SearchCnhFragment.newInstance()).commit();
            } else if (title == R.string.document_history) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, LogListFragment.newInstance()).commit();
            } else if (title == R.string.infraction_table) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, InfractionTable.newInstance()).commit();
            } else if (title == R.string.logout) {
                AppObject.getTinyDB(this).putBoolean(AppConstants.isLogin, false);
                finish();
                //triggerRebirth(this);
            } else if (title == R.string.setting) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SettingFragment.newInstance()).commit();
            } else if (title == R.string.app_help) {//getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, AjudaFragment.newInstance()).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ImageFragment.newInstance()).commit();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, VolleyFragment.newInstance()).commit();
            } else if (title == R.string.profile) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ProfileFragment.newInstance()).commit();
            }
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            root.closeDrawer(slider);
        },200L);

        return false;
    }

    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    private void checkFirstTimeAppSetup() {
        if (!AppObject.getTinyDB(this).getBoolean("is_first_time", false)) {
            (new FirstInstall()).execute();
            AppObject.getTinyDB(this).putBoolean("is_first_time", true);
        }
    }

    private class FirstInstall extends AsyncTask<String, String, String> {
        ProgressDialog pd = new ProgressDialog(MainUserActivity.this, getResources().getString(R.string.processing));

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
            pd.setCancelable(false);
        }

        protected String doInBackground(String... params) {
            try {

                AitEquipmentEntryJsonTask
                        entryJsonTask = new AitEquipmentEntryJsonTask(AppObject.getHttpClient()
                        .executeHttpGet(WebClient.URL_EQUIPMENTS), MainUserActivity.this);

                entryJsonTask.prepareEntry();

            } catch (Exception e) {
                e.printStackTrace();
            }
            CheckNumber checkNumber = new CheckNumber(MainUserActivity.this);
            checkNumber.check();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
        }

    }

    @Override
    public void onBackPressed() {

        if (root != null && root.isDrawerOpen(slider)) {
                root.closeDrawer(slider);
        } else {
            super.onBackPressed();
        }

    }

    private void getPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }

        }

    }

    public void OpenItemMenu(){

        AitData data = new AitData();
        data.setDataisNull(true);
        Intent intent = new Intent(this, AitActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AitData.getAitID(), data);
        intent.putExtras(bundle);
        startActivity(intent);

    }


    public PrimaryDrawerItem getPrimaryDrawerItem(int nameId, int icon  ,Long identifier){
        PrimaryDrawerItem item = new PrimaryDrawerItem();
        item.setName(new StringHolder(nameId));
        item.setIcon(new ImageHolder(icon));
        item.setIdentifier(identifier);
        return item;
    }

    public  ProfileDrawerItem getProfileDrawerItem(String name,String email,Bitmap bitmap){
        ProfileDrawerItem item = new ProfileDrawerItem();
        item.setName(new StringHolder(name));

        item.setDescription(new StringHolder(email));
        if(bitmap != null){
            item.setIcon( new ImageHolder(bitmap));
        }
       return  item;
    }

}
