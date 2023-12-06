package net.sistransito.mobile.ait.table;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import android.database.Cursor;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import net.sistransito.mobile.ait.AitData;
import net.sistransito.mobile.database.DatabaseCreator;

import java.util.List;
import net.sistransito.mobile.database.PrepopulatedDBOpenHelper;
import net.sistransito.mobile.util.Routine;
import net.sistransito.R;

public class InfractionTable extends Fragment implements View.OnClickListener {

    private ListerExpandableAdapterTable expandableAdapter;
    private ExpandableListView expandableListView;

    private View view;
    private AitData aitData;

    private EditText etSearchTable;
    private List<String> listInfractionArray, listAitInfractionArray;
    private ArrayAdapter<String> aaInfraction;
    private TextView tvMessage, tvClearSearch, tvSearchIcon;
    private RelativeLayout rlTable;
    private Cursor cursor;
    private PrepopulatedDBOpenHelper idInfraction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.table_fragment, null, false);
        initializedView();
        return view;
    }

    public static InfractionTable newInstance() {
        return new InfractionTable();
    }

    private void initializedView() {

        etSearchTable = (EditText) view.findViewById(R.id.et_tabela_search);
        tvClearSearch = (TextView) view.findViewById(R.id.tv_clear_search);
        tvSearchIcon = (TextView) view.findViewById(R.id.tv_search_loupe);

        tvClearSearch.setOnClickListener(this);

        if (checkCursor()) {
            setInfraction();
            addResultView();
        } else {
            addNoResultView();
        }

    }

    private boolean checkCursor() {
        cursor = (DatabaseCreator.getPrepopulatedDBOpenHelper(getActivity()))
                .getInfrationCursor();
        return cursor.getCount() > 0;
    }

    private void addNoResultView() {
        rlTable = (RelativeLayout) view.findViewById(R.id.lister_layout_table);
        cursor.close();
        tvMessage = new TextView(getActivity());
        tvMessage.setText(getResources().getString(R.string.no_result_returned));
        tvMessage.setGravity(Gravity.CENTER);
        tvMessage.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        tvMessage.setLayoutParams(params);
        tvMessage.setTextColor(getResources().getColor(R.color.line_color));
        if (tvMessage.getParent() == null) {
            rlTable.addView(tvMessage);
        }
    }

    private void addResultView() {
        expandableAdapter = new ListerExpandableAdapterTable(cursor,
                getActivity());
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_listview_table);
        expandableListView.setAdapter(expandableAdapter);
    }

    private void addResultView(Cursor mCursor) {
        expandableAdapter = new ListerExpandableAdapterTable(mCursor,
                getActivity());
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandable_listview_table);
        expandableListView.setAdapter(expandableAdapter);
    }

    private void setInfraction() {

        etSearchTable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                searchTable();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tvClearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etSearchTable.setText("");
                tvClearSearch.setVisibility(View.GONE);
                tvSearchIcon.setVisibility(View.VISIBLE);
                Routine.openKeyboard(etSearchTable, getActivity());
                addResultView();

            }
        });

        tvSearchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchTable();
                tvClearSearch.setVisibility(View.VISIBLE);
                tvSearchIcon.setVisibility(View.GONE);
                Routine.openKeyboard(etSearchTable, getActivity());

            }
        });

    }

    public void searchTable(){

        Cursor result = DatabaseCreator.getPrepopulatedDBOpenHelper(getActivity())
                .getInfrationCursor(etSearchTable.getText().toString());

        addResultView(result);

    }

    @Override
    public void onClick(View v) {

    }

}
