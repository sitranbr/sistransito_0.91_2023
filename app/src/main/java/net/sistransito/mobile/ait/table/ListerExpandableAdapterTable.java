package net.sistransito.mobile.ait.table;

import android.content.Context;
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
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View view, ViewGroup parent) {
        final int gPosition = groupPosition;

        final String gravity = getGroup(gPosition).getString(
                getGroup(gPosition).getColumnIndex(
                        PrepopulatedDBOpenHelper.AIT_GRAVITY));

        final String responsible = getGroup(gPosition).getString(
                getGroup(gPosition).getColumnIndex(
                        PrepopulatedDBOpenHelper.AIT_RESPONSIBLE));

        final String points = getGroup(gPosition).getString(
                getGroup(gPosition).getColumnIndex(
                        PrepopulatedDBOpenHelper.AIT_POINTS));

        final String amount = getGroup(gPosition).getString(
                getGroup(gPosition).getColumnIndex(
                        PrepopulatedDBOpenHelper.AIT_AMOUNT));

        final String transitCompetency = getGroup(gPosition).getString(
                getGroup(gPosition).getColumnIndex(
                        PrepopulatedDBOpenHelper.AIT_TRANSIT_AUTHORITY));

        String procedures = getGroup(gPosition).getString(
                getGroup(gPosition).getColumnIndex(
                        PrepopulatedDBOpenHelper.AIT_ADMINISTRATIVE_PROCEDURE));

        String observations = getGroup(gPosition).getString(
                getGroup(gPosition).getColumnIndex(
                        PrepopulatedDBOpenHelper.AIT_OBSERVATION));

        String notes = getGroup(gPosition).getString(
                getGroup(gPosition).getColumnIndex(
                        PrepopulatedDBOpenHelper.AIT_ANNOTATIONS));

        View mView = null;
        if (view != null) {
            mView = view;
        } else {
            mView = layoutInflater.inflate(R.layout.table_listview_child,
                    parent, false);
        }

        final TextView tvGravity, tvPoints, tvAmount, tvResponsible,
                tvCompetency, tvProcedures, tvObservation, tvNotes;

        tvGravity = (TextView) mView.findViewById(R.id.tv_gravity_table);
        tvPoints = (TextView) mView.findViewById(R.id.tv_points_table);
        tvAmount = (TextView) mView.findViewById(R.id.tv_amount_table);
        tvResponsible = (TextView) mView.findViewById(R.id.tv_responsible_table);
        tvCompetency = (TextView) mView.findViewById(R.id.tv_competency_table);
        tvProcedures = (TextView) mView.findViewById(R.id.tv_procedure_table);
        tvObservation = (TextView) mView.findViewById(R.id.tv_observation_table);
        tvNotes = (TextView) mView.findViewById(R.id.tv_notes_table);

        // Call method to apply bold to string

        final String NO_INFORMATION_AVAILABLE = context.getString(R.string.no_information_available);

        if (TextUtils.isEmpty(procedures)) {
            procedures = NO_INFORMATION_AVAILABLE;
        }

        if (TextUtils.isEmpty(observations)) {
            observations = NO_INFORMATION_AVAILABLE;
        }

        if (TextUtils.isEmpty(notes)) {
            notes = NO_INFORMATION_AVAILABLE;
        }

        Routine.TextAlignment normal = Routine.TextAlignment.NORMAL;
        Routine.TextAlignment center = Routine.TextAlignment.CENTER;

        SpannableString boldGravity = Routine.textWithBoldAndCenter((context.getString(R.string.nature_format).toString()), gravity, true, center);
        SpannableString boldPoint = Routine.textWithBoldAndCenter((context.getString(R.string.points_format).toString()), points, true, center);
        SpannableString boldAmount = Routine.textWithBoldAndCenter((context.getString(R.string.value_format).toString()), context.getResources().getString(R.string.coin_format) + amount, true, center);
        SpannableString boldResponsible = Routine.textWithBoldAndCenter((context.getString(R.string.responsible_format).toString()), responsible, true, center);
        SpannableString boldTransitCompetency = Routine.textWithBoldAndCenter((context.getString(R.string.competency_format).toString()), transitCompetency, true, center);

        SpannableString boldProcedures = Routine.applyBold(context.getString(R.string.administrative_measure_format) + procedures);
        SpannableString boldObservation = Routine.applyBold(context.getResources().getString(R.string.observation_format) + observations);
        SpannableString boldNotes = Routine.applyBold(context.getResources().getString(R.string.notes_format) + notes);


        tvGravity.setText(boldGravity);
        tvPoints.setText(boldPoint);
        tvAmount.setText(boldAmount);
        tvResponsible.setText(boldResponsible);
        tvCompetency.setText(boldTransitCompetency);
        tvProcedures.setText(boldProcedures);
        tvObservation.setText(boldObservation);
        tvNotes.setText(boldNotes);

        return mView;

    }


}

