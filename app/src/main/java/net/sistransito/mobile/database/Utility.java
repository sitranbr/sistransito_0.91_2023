package net.sistransito.mobile.database;

import android.content.Context;
import android.util.Log;

import net.sistransito.R;

public class Utility {

    private final Context context;

    public Utility(Context context) {
        this.context = context;
    }

    public String filter(String filterData, String xml) {
        try {
            int index = Integer.parseInt(filterData);
            String[] listXml = getXmlArray(xml);
            return listXml[index];
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException exception) {
            Log.d("Error: ", String.valueOf(exception));
            return null;
        }
    }

    private String[] getXmlArray(String xml) {
        switch (xml) {
            case "LICENSING_STATUS":
                return context.getResources().getStringArray(R.array.filter_vehicle_licensing_status);
            case "TYPE":
                return context.getResources().getStringArray(R.array.filter_vehicle_type);
            case "SPECIES":
                return context.getResources().getStringArray(R.array.ait_species);
            case "COLOR":
                return context.getResources().getStringArray(R.array.filter_color);
            case "STATE":
                return context.getResources().getStringArray(R.array.state_array);
            case "CNH_STATE":
                return context.getResources().getStringArray(R.array.state_array);
            case "IMPEDIMENT_INDICATOR":
                return context.getResources().getStringArray(R.array.filter_theft_occurrence);
            case "OCCURRENCE_INDICATOR":
                return context.getResources().getStringArray(R.array.filter_theft_occurrence);
            case "CATEGORY":
                return context.getResources().getStringArray(R.array.list_category);
            default:
                throw new IllegalArgumentException("Invalid xml type");
        }
    }
}
