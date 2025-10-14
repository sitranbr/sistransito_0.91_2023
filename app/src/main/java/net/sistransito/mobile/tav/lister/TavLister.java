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
    private TavData expandTavData;
    private boolean shouldExpand = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tav_lister_main);
        
        // Verificar se deve expandir um TAV específico
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            shouldExpand = extras.getBoolean("SHOULD_EXPAND", false);
            if (shouldExpand) {
                expandTavData = (TavData) extras.getSerializable("EXPAND_TAV_DATA");
            }
        }
        
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
        
        // Se deve expandir um TAV específico, encontrar e expandir
        if (shouldExpand && expandTavData != null) {
            expandSpecificTav(expandableListView, expandableAdapter);
        }
    }

    /**
     * Expande automaticamente o TAV específico na listagem
     */
    private void expandSpecificTav(ExpandableListView expandableListView, TAVListerExpandableAdapter adapter) {
        try {
            // Buscar o grupo que corresponde ao TAV específico
            for (int i = 0; i < adapter.getGroupCount(); i++) {
                android.database.Cursor groupCursor = adapter.getGroup(i);
                if (groupCursor != null && !groupCursor.isClosed()) {
                    String tavNumber = groupCursor.getString(groupCursor.getColumnIndex(net.sistransito.mobile.database.TavDatabaseHelper.TAV_NUMBER));
                    String aitNumber = groupCursor.getString(groupCursor.getColumnIndex(net.sistransito.mobile.database.TavDatabaseHelper.AIT_NUMBER));
                    
                    // Verificar se é o TAV que queremos expandir
                    if (expandTavData.getTavNumber().equals(tavNumber) && 
                        expandTavData.getAitNumber().equals(aitNumber)) {
                        // Expandir este grupo
                        expandableListView.expandGroup(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            android.util.Log.e("TavLister", "Erro ao expandir TAV específico", e);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.im_btn_back) {
            finish();
        }
    }
}
