package net.sistransito.mobile.ait.table;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import net.sistransito.mobile.database.DatabaseCreator;
import net.sistransito.mobile.database.PrepopulatedDBOpenHelper;
import net.sistransito.R;
import net.sistransito.mobile.util.Routine;

import java.util.LinkedHashMap;
import java.util.Map;

public class ListerExpandableAdapterTable extends CursorTreeAdapter {
    private LayoutInflater layoutInflater;
    private final Context context;

    public ListerExpandableAdapterTable(Cursor cursor, Context context) {
        super(cursor, context);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    protected void bindChildView(View arg0, Context arg1, Cursor arg2,
                                 boolean arg3) {
    }

    @Override
    protected void bindGroupView(View view, Context arg1, Cursor cursor,
                                 boolean arg3) {

       /* TextView tvArticle, tvCode, tvUnfolding, tvDescription;

        tvArticle = (TextView) view.findViewById(R.id.tv_article_table);
        tvCode = (TextView) view.findViewById(R.id.tv_code_table);
        tvUnfolding = (TextView) view.findViewById(R.id.tv_unfolding_table);
        tvDescription = (TextView) view.findViewById(R.id.tv_description_table);

        tvCode
                .setText(cursor.getString(cursor
                        .getColumnIndex(PrepopulatedDBOpenHelper.AIT_FLAMING_CODE)));
        tvUnfolding
                .setText("-" + cursor.getString(cursor
                        .getColumnIndex(PrepopulatedDBOpenHelper.AIT_UNFOLDING)));
        tvArticle.setText("Art. " + cursor.getString(cursor
                .getColumnIndex(PrepopulatedDBOpenHelper.AIT_ARTICLE)));
        tvDescription.setText(cursor.getString(cursor
                .getColumnIndex(PrepopulatedDBOpenHelper.AIT_DESCRIPTION)));*/

    }

    @Override
    protected View newChildView(Context arg0, Cursor arg1, boolean arg2,
                                ViewGroup arg3) {
        return null;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor,
                                boolean isExpendable, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.table_infraction_listview_parent,
                null);

        TextView tvArticle, tvFraming, tvUnfolding, tvDescription;

        tvArticle = (TextView) view.findViewById(R.id.tv_article_table);
        tvFraming = (TextView) view.findViewById(R.id.tv_framing_table);
        tvUnfolding = (TextView) view.findViewById(R.id.tv_unfolding_table);
        tvDescription = (TextView) view.findViewById(R.id.tv_description_table);

        tvArticle.setText(cursor.getString(cursor
                .getColumnIndex(PrepopulatedDBOpenHelper.AIT_ARTICLE)));
        tvFraming
                .setText(cursor.getString(cursor
                        .getColumnIndex(PrepopulatedDBOpenHelper.AIT_FLAMING_CODE)));
        tvUnfolding
                .setText(cursor.getString(cursor
                        .getColumnIndex(PrepopulatedDBOpenHelper.AIT_UNFOLDING)));

        tvDescription.setText(cursor.getString(cursor
                .getColumnIndex(PrepopulatedDBOpenHelper.AIT_DESCRIPTION)));

        return view;
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {

        int groupId = groupCursor.getInt(groupCursor
                .getColumnIndex(PrepopulatedDBOpenHelper.AIT_ID));

        return (DatabaseCreator.getPrepopulatedDBOpenHelper(context))
                .getInfrationCursor(groupId);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        View view = convertView != null
                ? convertView
                : layoutInflater.inflate(R.layout.table_listview_child, parent, false);

        // Obtém o Cursor do grupo
        Cursor cursor = getGroup(groupPosition);

        // Mapeamento de colunas para valores com segurança
        Map<String, String> dataMap = new LinkedHashMap<>();
        String[] columns = {
                PrepopulatedDBOpenHelper.AIT_GRAVITY,
                PrepopulatedDBOpenHelper.AIT_RESPONSIBLE,
                PrepopulatedDBOpenHelper.AIT_POINTS,
                PrepopulatedDBOpenHelper.AIT_AMOUNT,
                PrepopulatedDBOpenHelper.AIT_TRANSIT_AUTHORITY,
                PrepopulatedDBOpenHelper.AIT_ADMINISTRATIVE_PROCEDURE,
                PrepopulatedDBOpenHelper.AIT_OBSERVATION,
                PrepopulatedDBOpenHelper.AIT_ANNOTATIONS
        };

        for (String column : columns) {
            int columnIndex = cursor.getColumnIndex(column);
            dataMap.put(column, columnIndex >= 0 ? cursor.getString(columnIndex) : "");
        }

        // Extrai valores do mapa
        String gravity = dataMap.get(PrepopulatedDBOpenHelper.AIT_GRAVITY);
        String responsible = dataMap.get(PrepopulatedDBOpenHelper.AIT_RESPONSIBLE);
        String points = dataMap.get(PrepopulatedDBOpenHelper.AIT_POINTS);
        String amount = dataMap.get(PrepopulatedDBOpenHelper.AIT_AMOUNT);
        String transitCompetency = dataMap.get(PrepopulatedDBOpenHelper.AIT_TRANSIT_AUTHORITY);
        String procedures = TextUtils.isEmpty(dataMap.get(PrepopulatedDBOpenHelper.AIT_ADMINISTRATIVE_PROCEDURE))
                ? context.getString(R.string.no_information_available)
                : dataMap.get(PrepopulatedDBOpenHelper.AIT_ADMINISTRATIVE_PROCEDURE);
        String observations = TextUtils.isEmpty(dataMap.get(PrepopulatedDBOpenHelper.AIT_OBSERVATION))
                ? context.getString(R.string.no_information_available)
                : dataMap.get(PrepopulatedDBOpenHelper.AIT_OBSERVATION);
        String notes = TextUtils.isEmpty(dataMap.get(PrepopulatedDBOpenHelper.AIT_ANNOTATIONS))
                ? context.getString(R.string.no_information_available)
                : dataMap.get(PrepopulatedDBOpenHelper.AIT_ANNOTATIONS);

        // Configuração dos TextViews
        TextView tvGravity = view.findViewById(R.id.tv_gravity_table);
        TextView tvPoints = view.findViewById(R.id.tv_points_table);
        TextView tvAmount = view.findViewById(R.id.tv_amount_table);
        TextView tvResponsible = view.findViewById(R.id.tv_responsible_table);
        TextView tvCompetency = view.findViewById(R.id.tv_competency_table);
        TextView tvProcedures = view.findViewById(R.id.tv_procedure_table);
        TextView tvObservation = view.findViewById(R.id.tv_observation_table);
        TextView tvNotes = view.findViewById(R.id.tv_notes_table);

        // Configurações de formatação
        Resources res = context.getResources();
        Routine.TextAlignment normal = Routine.TextAlignment.NORMAL;
        Routine.TextAlignment center = Routine.TextAlignment.CENTER;

        // Formatação dos textos com negrito e alinhamento
        Map<TextView, SpannableString> textViewMap = new LinkedHashMap<>();
        textViewMap.put(tvGravity, Routine.textWithBoldAndCenter(res.getString(R.string.nature_format), gravity, true, center));
        textViewMap.put(tvPoints, Routine.textWithBoldAndCenter(res.getString(R.string.points_format), points, true, center));
        textViewMap.put(tvAmount, Routine.textWithBoldAndCenter(res.getString(R.string.value_format), res.getString(R.string.coin_format) + amount, true, center));
        textViewMap.put(tvResponsible, Routine.textWithBoldAndCenter(res.getString(R.string.responsible_format), responsible, true, center));
        textViewMap.put(tvCompetency, Routine.textWithBoldAndCenter(res.getString(R.string.competency_format), transitCompetency, true, center));
        textViewMap.put(tvProcedures, Routine.applyBold(res.getString(R.string.administrative_measure_format) + procedures));
        textViewMap.put(tvObservation, Routine.applyBold(res.getString(R.string.observation_format) + observations));
        textViewMap.put(tvNotes, Routine.applyBold(res.getString(R.string.notes_format) + notes));

        // Aplica os textos aos TextViews
        textViewMap.forEach(TextView::setText);

        return view;
    }


}

