package net.sistransito.mobile.aitcompany;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.viewpagerindicator.UnderlinePageIndicator;

import net.sistransito.mobile.adapter.TabPJStartSectionsPagerAdapter;
import net.sistransito.mobile.adapter.TabPJSubTitleSectionsPagerAdapter;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.AnyAlertDialog;
import net.sistransito.mobile.fragment.UpdateFragment;
import net.sistransito.mobile.util.Routine;
import net.sistransito.mobile.viewpager.AnySwipeableViewPager;
import net.sistransito.mobile.viewpager.DepthPageTransformer;
import net.sistransito.mobile.viewpager.FixedSpeedScroller;
import net.sistransito.mobile.viewpager.ZoomOutPageTransformer;
import net.sistransito.R;

import java.lang.reflect.Field;

public class AitCompanyActivity extends AppCompatActivity implements OnClickListener {
    private AnySwipeableViewPager pager;
    private AnySwipeableViewPager subTituloDaPagina;
    private Field mScroller;
    private FixedSpeedScroller scroller;
    private UnderlinePageIndicator indicator;
    private ImageView imBtnTabConductor, imBtnTabAddress, imBtnTabInfraction, imBtnBack;
    private TabPJStartSectionsPagerAdapter adapter;
    private pjData pjData;

    private int tabAtual;

    String tabConductor;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.ait_pj_main);

        getAitObject();
        initializedView();
        setMainPager();
        setPagerSubTitle();
        setIndicatorTitle();

        if (pjData.isStorePJFullData()) {
            setPagerPosition(2);
        }

    }

    private void getAitObject() {
        Bundle bundle = getIntent().getExtras();
        ObjectAutoPJ.setDadosAuto((pjData) bundle.getSerializable(pjData.getIDAuto()));
        pjData = ObjectAutoPJ.getDadosAuto();
    }

    private void setIndicatorTitle() {
        indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        indicator.setFades(false);

        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int positon) {

                if (positon == 3) {
                    UpdateFragment fragment = (UpdateFragment) adapter
                            .instantiateItem(pager, positon);
                    fragment.Update();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void setPagerSubTitle() {

        subTituloDaPagina = (AnySwipeableViewPager) findViewById(R.id.ait_title_pager);
        subTituloDaPagina.setSwipeable(false);
        subTituloDaPagina.setAdapter(new TabPJSubTitleSectionsPagerAdapter(
                getSupportFragmentManager()));
        subTituloDaPagina.setPageMargin(20);
        subTituloDaPagina.setPageTransformer(true,
                new ZoomOutPageTransformer());
        try {

            mScroller.set(subTituloDaPagina, scroller);

        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

    }

    private void setMainPager() {

        adapter = new TabPJStartSectionsPagerAdapter(
                getSupportFragmentManager());
        pager = (AnySwipeableViewPager) findViewById(R.id.ait_start_pager);
        pager.setSwipeable(false);
        pager.setPageMargin(20);
        pager.setPageTransformer(true, new DepthPageTransformer());
        pager.setOffscreenPageLimit(adapter.getCount());
        pager.setAdapter(adapter);

        try {
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new FixedSpeedScroller(this);
            scroller.setFixedDuration(2000);
            mScroller.set(pager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

    }

    public void setCancel(Context context, String motive){
        if (!DatabaseCreator.getAitPJDatabaseAdapter(context).setCancelarAuto(pjData, motive))
            Routine.showAlert(getResources().getString(R.string.update_erro), context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_pj_btn_tab_conductor:
                setPagerPosition(0);
                break;
            case R.id.img_pj_btn_tab_address:
                setPagerPosition(1);
                break;
            case R.id.img_pj_btn_tab_infraction:
                setPagerPosition(2);
                break;
            case R.id.im_pj_btn_back:
                AnyAlertDialog.dialogView(this, this.getResources().getString(R.string.alert_motive), null);
                break;
        }
    }

    private void setPagerPosition(int position) {

        if (pjData.isStorePJFullData() && position == 0) {
            return;
        }
        Log.d("isStore ? ", String.valueOf(pjData.isStorePJFullData()));
        setCurrentTabSelectedIteams(position);
        subTituloDaPagina.setCurrentItem(position);
        pager.setCurrentItem(position);
    }

    private void setCurrentTabSelectedIteams(int positon) {
        switch (positon) {
            case 0:
                imBtnTabConductor.setSelected(true);
                imBtnTabAddress.setSelected(false);
                imBtnTabInfraction.setSelected(false);
                break;
            case 1:
                imBtnTabConductor.setSelected(false);
                imBtnTabAddress.setSelected(true);
                imBtnTabInfraction.setSelected(false);
                break;
            case 2:
                imBtnTabConductor.setSelected(false);
                imBtnTabAddress.setSelected(false);
                imBtnTabInfraction.setSelected(true);
                break;
        }
    }

    private void initializedView() {
        imBtnTabConductor = (ImageView) findViewById(R.id.img_pj_btn_tab_conductor);
        imBtnTabAddress = (ImageView) findViewById(R.id.img_pj_btn_tab_address);
        imBtnTabInfraction = (ImageView) findViewById(R.id.img_pj_btn_tab_infraction);

        imBtnTabConductor.setOnClickListener(this);
        imBtnTabAddress.setOnClickListener(this);
        imBtnTabInfraction.setOnClickListener(this);

        imBtnBack = (ImageView) findViewById(R.id.im_pj_btn_back);
        imBtnBack.setOnClickListener(this);
    }

    public void setTabAtual(int tabAtual) { setPagerPosition(tabAtual); }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return !(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() >= 0);
    }

}
