package com.brz.converter.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brz.converter.Currency;
import com.brz.converter.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CurrencySpinnerAdapter extends ArrayAdapter<Currency> {

    CurrencySpinnerAdapter(@NonNull Context context, int resource, @NonNull Currency[] objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View grid = getView(position, convertView, parent);
        TextView fullName = grid.findViewById(R.id.full_name_text);
        TextView shortName = grid.findViewById(R.id.short_name_text);
        fullName.setTextColor(Color.BLACK);
        shortName.setTextColor(Color.BLACK);
        return grid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View grid;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.spinner_item, parent, false);
        } else {
            grid = convertView;
        }

        ImageView imageView = grid.findViewById(R.id.flag_icon);
        TextView fullName = grid.findViewById(R.id.full_name_text);
        TextView shortName = grid.findViewById(R.id.short_name_text);


        fullName.setText(getItem(position).getName());
        shortName.setText(getItem(position).getCharCode());

        return grid;
    }
}
