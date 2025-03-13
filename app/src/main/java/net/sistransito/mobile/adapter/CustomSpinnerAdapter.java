package net.sistransito.mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import net.sistransito.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    private List<String> items;

    public CustomSpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent, false);
    }

    private View getView(int position, View convertView, ViewGroup parent, boolean isDropDownView) {
        ViewHolder holder;
        if (convertView == null) {
            if (isDropDownView) {
                convertView = inflater.inflate(R.layout.spinner_custom_item, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.spinner_custom_item, parent, false);
            }
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(android.R.id.text1);
            holder.divider = convertView.findViewById(R.id.divider);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(items.get(position));

        if (isDropDownView) {
            if (position == getCount() - 1) {
                holder.divider.setVisibility(View.GONE);
            } else {
                holder.divider.setVisibility(View.VISIBLE);
            }
        }

        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        View divider;
    }

    public static CustomSpinnerAdapter createStateAdapter(Context context, List<String> listStateDriver) {
        return new CustomSpinnerAdapter(
                context,
                R.layout.spinner_custom_item,
                listStateDriver
        );
    }
}