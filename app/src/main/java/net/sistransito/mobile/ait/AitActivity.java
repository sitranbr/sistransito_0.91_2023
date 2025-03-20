package net.sistransito.mobile.ait;

import android.content.Context;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.viewpagerindicator.UnderlinePageIndicator;

import net.sistransito.mobile.adapter.TabAitStartSectionsPagerAdapter;
import net.sistransito.mobile.adapter.TabAitSubTitleSectionsPagerAdapter;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.fragment.AnyAlertDialog;
import net.sistransito.mobile.fragment.UpdateFragment;
import net.sistransito.mobile.utility.Routine;
import net.sistransito.mobile.viewpager.AnySwipeableViewPager;
import net.sistransito.mobile.viewpager.DepthPageTransformer;
import net.sistransito.mobile.viewpager.FixedSpeedScroller;
import net.sistransito.mobile.viewpager.ZoomOutPageTransformer;
import net.sistransito.R;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AitActivity extends AppCompatActivity implements OnClickListener {
	private AnySwipeableViewPager pager;
	private AnySwipeableViewPager pageSubtitle;
	private Field mScroller;
	private FixedSpeedScroller scroller;
	private UnderlinePageIndicator indicator;
	private ImageView imBtnBack;
	private TabAitStartSectionsPagerAdapter adapter;
	private AitData aitData;

	private ImageView[] tabButtons;

	private static final int TAB_VEHICLE = 0;
	private static final int TAB_CONDUCTOR = 1;
	private static final int TAB_ADDRESS = 2;
	private static final int TAB_INFRACTION = 3;
	private static final int TAB_GENERATION = 4;

	private Map<Integer, Runnable> actionMap = new HashMap<>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.ait_main);

		getAitObject();
		initializedView();
		setupActionMap();
		setMainPager();
		setPagerSubtitle();
		setIndicatorTitle();
		if (aitData.isStoreFullData()) {
			setPagerPosition(3);
		}
	}

	private void getAitObject() {
		Bundle bundle = getIntent().getExtras();
		AitObject.setAitData((AitData) bundle.getSerializable(AitData.getAitID()));
		aitData = AitObject.getAitData();
	}

	private void setIndicatorTitle() {
		indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(pager);
		indicator.setFades(false);

		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (position == 4) {
					UpdateFragment fragment = (UpdateFragment) adapter
							.instantiateItem(pager, position);
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

	private void setPagerSubtitle() {
		pageSubtitle = (AnySwipeableViewPager) findViewById(R.id.ait_title_pager);
		pageSubtitle.setSwipeable(false);
		pageSubtitle.setAdapter(new TabAitSubTitleSectionsPagerAdapter(
				getSupportFragmentManager()));
		pageSubtitle.setPageMargin(20);
		pageSubtitle.setPageTransformer(true,
				new ZoomOutPageTransformer());
		try {

			mScroller.set(pageSubtitle, scroller);

		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}

	}

	private void setMainPager() {
		adapter = new TabAitStartSectionsPagerAdapter(
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

	public void sendToFragment(String tab){
		TabAitVehicleFragment vehicleFragment = new TabAitVehicleFragment();
		Bundle args = new Bundle();
		args.putString("idTag", tab);
		vehicleFragment.setArguments(args);
	}

	public void setCancel(Context context, String reason){
		if (!DatabaseCreator.getInfractionDatabaseAdapter(context).cancelAit(aitData, reason))
			Routine.showAlert(getResources().getString(R.string.update_erro), context);
	}

	private void setupActionMap() {
		actionMap.put(R.id.im_btn_tab_vehicle, () -> setPagerPosition(TAB_VEHICLE));
		actionMap.put(R.id.im_btn_tab_conductor, () -> setPagerPosition(TAB_CONDUCTOR));
		actionMap.put(R.id.im_btn_tab_address, () -> setPagerPosition(TAB_ADDRESS));
		actionMap.put(R.id.im_btn_tab_infraction, () -> setPagerPosition(TAB_INFRACTION));
		actionMap.put(R.id.im_btn_tab_generation, () -> setPagerPosition(TAB_GENERATION));
		actionMap.put(R.id.im_ait_btn_back, () -> AnyAlertDialog.dialogView(this, this.getResources().getString(R.string.alert_motive), "ait"));
	}

	@Override
	public void onClick(View v) {
		Runnable action = actionMap.get(v.getId());
		if (action != null) {
			action.run();
		}
	}

	private void setPagerPosition(int position) {
		if (aitData.isStoreFullData() && position == 0) {
			return;
		}
		setCurrentTabSelectedItens(position);
		pageSubtitle.setCurrentItem(position);
		pager.setCurrentItem(position);
	}

	private void initializedView() {
		tabButtons = new ImageView[]{
			(ImageView) findViewById(R.id.im_btn_tab_vehicle),
			(ImageView) findViewById(R.id.im_btn_tab_conductor),
			(ImageView) findViewById(R.id.im_btn_tab_address),
			(ImageView) findViewById(R.id.im_btn_tab_infraction),
			(ImageView) findViewById(R.id.im_btn_tab_generation)
		};

		for (ImageView tabButton : tabButtons) {
			tabButton.setOnClickListener(this);
		}

		imBtnBack = (ImageView) findViewById(R.id.im_ait_btn_back);
		imBtnBack.setOnClickListener(this);
	}

	private void setCurrentTabSelectedItens(int position) {
		if (tabButtons != null) {
			for (int i = 0; i < tabButtons.length; i++) {
				if (tabButtons[i] != null) {
					tabButtons[i].setSelected(i == position);
				}
			}
		}
	}

	public void setTabActual(int tabActual) { setPagerPosition(tabActual); }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
        return !(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() >= 0);
    }

}
