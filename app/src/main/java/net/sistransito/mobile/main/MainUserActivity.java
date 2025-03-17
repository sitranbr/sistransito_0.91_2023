package net.sistransito.mobile.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import net.sistransito.R;
import net.sistransito.mobile.ait.AitActivity;
import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.ait.table.InfractionTable;
import net.sistransito.mobile.aitcompany.AitCompanyActivity;
import net.sistransito.mobile.aitcompany.pjData;
import net.sistransito.mobile.appconstants.AppConstants;
import net.sistransito.mobile.appobject.AppObject;
import net.sistransito.mobile.cnh.SearchCnhFragment;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.equipments.AitEquipmentEntryJsonTask;
import net.sistransito.mobile.http.WebClient;
import net.sistransito.mobile.login.ActivityLogin;
import net.sistransito.mobile.login.LoginData;
import net.sistransito.mobile.number.CheckNumber;
import net.sistransito.mobile.plate.lister.LogListFragment;
import net.sistransito.mobile.plate.search.ConsultPlateFragment;
import net.sistransito.mobile.profile.ProfileFragment;
import net.sistransito.mobile.rrd.RrdActivity;
import net.sistransito.mobile.rrd.RrdData;
import net.sistransito.mobile.setting.SettingFragment;
import net.sistransito.mobile.tav.TavActivity;
import net.sistransito.mobile.tav.TavData;
import net.sistransito.mobile.tca.TcaActivity;
import net.sistransito.mobile.tca.TcaData;
import net.sistransito.mobile.utility.DialogMaterial;
import net.sistransito.mobile.utility.User;

import java.util.concurrent.Executors;

public class MainUserActivity extends AppCompatActivity {
    private Context context;
    private AccountHeaderView headerView = null;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private SharedPreferences preferences;
    private IProfile profile;
    private User user;
    private LoginData loginData;
    private DialogMaterial dialogMaterial;
    private MaterialDrawerSliderView slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        slider = findViewById(R.id.slider);

        initImageLoader();
        setupToolbar();
        setupMenu(savedInstanceState);
        checkFirstTimeAppSetup();
        requestPermissions();

        setupOnBackPressed();
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .displayer(new FadeInBitmapDisplayer(500))
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(defaultOptions)
                .build();

        ImageLoader.getInstance().init(config);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ham_burger_menu);
    }

    private void setupMenu(Bundle savedInstanceState) {
        user = AppObject.getTinyDB(this).getObject(AppConstants.user, User.class);

        drawerLayout = findViewById(R.id.root);
        setupDrawerToggle();

        headerView = new AccountHeaderView(this);
        headerView.attachToSliderView(slider);
        headerView.addProfile(createProfileDrawerItem(user.getCompanyName(), user.getCompanyName(), null), 0);

        if (savedInstanceState != null) {
            headerView.saveInstanceState(savedInstanceState);
        }

        headerView.setHeaderBackground(new ImageHolder(R.drawable.header));
        loadUserProfileImage();

        setupDrawerItems();
        slider.setSelection(0, true);
    }

    private void setupDrawerToggle() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                com.mikepenz.materialdrawer.R.string.material_drawer_open,
                com.mikepenz.materialdrawer.R.string.material_drawer_close
        );
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void loadUserProfileImage() {
        ImageLoader.getInstance().loadImage(user.getProfileImage(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                headerView.removeProfile(0);
                headerView.addProfile(createProfileDrawerItem(user.getCompanyName(), user.getCompanyName(), loadedImage), 0);
            }
        });
    }

    private void setupDrawerItems() {
        slider.getItemAdapter().add(createPrimaryDrawerItem(R.string.renavan_search, R.mipmap.ic_i_car_p, 0L));
        slider.getItemAdapter().add(createPrimaryDrawerItem(R.string.search_cnh, R.mipmap.ic_i_id_p, 1L));
        slider.getItemAdapter().add(createPrimaryDrawerItem(R.string.document_history, R.mipmap.ic_log_n, 2L));
        slider.getItemAdapter().add(new DividerDrawerItem());
        slider.getItemAdapter().add(createPrimaryDrawerItem(R.string.infraction_table, R.mipmap.ic_infraction, 3L));
        slider.getItemAdapter().add(new DividerDrawerItem());
        slider.getItemAdapter().add(createPrimaryDrawerItem(R.string.setting, R.mipmap.ic_settting_nav, 4L));
        slider.getItemAdapter().add(createPrimaryDrawerItem(R.string.profile, R.mipmap.ic_profile, 5L));
        slider.getItemAdapter().add(createPrimaryDrawerItem(R.string.app_help, R.mipmap.ic_help_nav, 6L));
        slider.getItemAdapter().add(new DividerDrawerItem());
        slider.getItemAdapter().add(createPrimaryDrawerItem(R.string.logout, R.mipmap.ic_logout, 7L));

        slider.setOnDrawerItemClickListener((view, iDrawerItem, integer) -> {
            handleDrawerItemClick(iDrawerItem);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btn_overflow_autuar) {
            startActivityWithBundle(AitActivity.class, AitData.getAitID(), new AitData(this));
        } else if (id == R.id.btn_overflow_autuar_pj) {
            startActivityWithBundle(AitCompanyActivity.class, pjData.getIDAuto(), new pjData(this));
        } else if (id == R.id.btn_overflow_rrd_avulso) {
            startActivityWithBundle(RrdActivity.class, RrdData.getRRDId(), new RrdData());
        } else if (id == R.id.btn_overflow_tav_avulso) {
            startActivityWithBundle(TavActivity.class, TavData.getTavId(), new TavData());
        } else if (id == R.id.btn_overflow_tca_avulso) {
            startActivityWithBundle(TcaActivity.class, TcaData.getTcaId(), new TcaData());
        } else if (id == R.id.btn_overflow_autos_restantes) {
            DialogMaterial.getBottomSheet(DatabaseCreator.getBalanceDatabaseAdapter(this).getShowView(), Color.BLACK, this).show();
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void startActivityWithBundle(Class<?> activityClass, String key, Object data) {
        Intent intent = new Intent(this, activityClass);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, (java.io.Serializable) data);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private boolean handleDrawerItemClick(IDrawerItem drawerItem) {
        if (drawerItem instanceof Nameable) {
            int title = ((Nameable) drawerItem).getName().getTextRes();
            getSupportActionBar().setTitle(title);
            navigateToFragment(title);
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> drawerLayout.closeDrawer(slider), 200L);

        return false;
    }

    private void navigateToFragment(int title) {
        if (title == R.string.renavan_search) {
            replaceFragment(ConsultPlateFragment.newInstance());
        } else if (title == R.string.search_cnh) {
            replaceFragment(SearchCnhFragment.newInstance());
        } else if (title == R.string.document_history) {
            replaceFragment(LogListFragment.newInstance());
        } else if (title == R.string.infraction_table) {
            replaceFragment(InfractionTable.newInstance());
        } else if (title == R.string.logout) {
            logout();
        } else if (title == R.string.setting) {
            replaceFragment(SettingFragment.newInstance());
        } else if (title == R.string.app_help) {
            //replaceFragment(ImageFragment.newInstance());
        } else if (title == R.string.profile) {
            replaceFragment(ProfileFragment.newInstance());
        }
    }

    private void replaceFragment(androidx.fragment.app.Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void logout() {
        AppObject.getTinyDB(this).putBoolean(AppConstants.isLogin, false);
        startActivity(new Intent(this, ActivityLogin.class));
        finish();
    }

    private void checkFirstTimeAppSetup() {
        if (!AppObject.getTinyDB(this).getBoolean("is_first_time", false)) {
            Executors.newSingleThreadExecutor().execute(() -> {
                showProgressDialog();
                performInitialSetup();
                dismissProgressDialog();
            });
            AppObject.getTinyDB(this).putBoolean("is_first_time", true);
        }
    }

    private void showProgressDialog() {
        ProgressDialog pd = new ProgressDialog(MainUserActivity.this, getResources().getString(R.string.processing));
        runOnUiThread(() -> {
            pd.show();
            pd.setCancelable(false);
        });
    }

    private void dismissProgressDialog() {
        ProgressDialog pd = new ProgressDialog(MainUserActivity.this, getResources().getString(R.string.processing));
        runOnUiThread(pd::dismiss);
    }

    private void performInitialSetup() {
        try {
            AitEquipmentEntryJsonTask entryJsonTask = new AitEquipmentEntryJsonTask(AppObject.getHttpClient().executeHttpGet(WebClient.URL_EQUIPMENTS), MainUserActivity.this);
            entryJsonTask.prepareEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new CheckNumber(MainUserActivity.this).check();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    private void setupOnBackPressed() {
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout != null && drawerLayout.isDrawerOpen(slider)) {
                    drawerLayout.closeDrawer(slider);
                } else {
                    finish();
                }
            }
        });
    }

    public PrimaryDrawerItem createPrimaryDrawerItem(int nameId, int icon, Long identifier) {
        PrimaryDrawerItem item = new PrimaryDrawerItem();
        item.setName(new StringHolder(nameId));
        item.setIcon(new ImageHolder(icon));
        item.setIdentifier(identifier);
        return item;
    }

    public ProfileDrawerItem createProfileDrawerItem(String name, String email, Bitmap bitmap) {
        ProfileDrawerItem item = new ProfileDrawerItem();
        item.setName(new StringHolder(name));
        item.setDescription(new StringHolder(email));
        if (bitmap != null) {
            item.setIcon(new ImageHolder(bitmap));
        }
        return item;
    }
}
