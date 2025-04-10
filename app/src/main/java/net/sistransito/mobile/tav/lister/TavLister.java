package net.sistransito.mobile.tav.lister;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.bluetoothprint.PrintBitmap.TavPrintBitmap;
import net.sistransito.mobile.bluetoothprint.PrintBitmap.base.BasePrintBitmap;
import net.sistransito.mobile.tav.TavData;
import net.sistransito.ui.BasePrintActivity;
import net.sistransito.R;

public class TavLister extends BasePrintActivity implements OnClickListener {
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tav_lister_main);
        initializedView();
    }

    @Override
    protected void onPrintData(Object mainData, Object aitData) {
        BasePrintBitmap tavPrintBitmap = new TavPrintBitmap(this,
                (TavData) mainData, (AitData) aitData);
        startPrint(tavPrintBitmap);
    }

    @Override
    protected void onPrintData(Object mainData, Object aitData, String copy) {

    }

    private void initializedView() {
        ImageButton imBtnBack = (ImageButton) findViewById(R.id.im_btn_back);
        imBtnBack.setOnClickListener(this);

        if (checkCursor()) {
            addResultView();
        } else {
            addNoResultView();
        }
    }

    private boolean checkCursor() {
        cursor = (DatabaseCreator.getTavDatabaseAdapter(this)).getTavCursor();
        return cursor.getCount() > 0;
    }

    private void addNoResultView() {
        RelativeLayout tavListerLayout = (RelativeLayout) findViewById(R.id.tav_lister_layout);
        cursor.close();
        TextView tvMessage = new TextView(this);
        tvMessage.setText(getResources().getString(
                R.string.no_result_returned));
        tvMessage.setGravity(Gravity.CENTER);
        tvMessage.setTextAppearance(this, android.R.style.TextAppearance_Large);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        tvMessage.setLayoutParams(params);
        tvMessage.setTextColor(getResources().getColor(R.color.line_color));
        if (tvMessage.getParent() == null) {
            tavListerLayout.addView(tvMessage);
        }
    }

    private void addResultView() {
        TAVListerExpandableAdapter expandableAdapter = new TAVListerExpandableAdapter(cursor,
                TavLister.this);
        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandable_ListView_Tav);
        expandableListView.setAdapter(expandableAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.im_btn_back) {
            finish();
        }
    }
}
