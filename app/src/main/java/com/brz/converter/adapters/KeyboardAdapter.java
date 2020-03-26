package com.brz.converter.adapters;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.brz.converter.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class KeyboardAdapter extends ArrayAdapter<String> {

    public KeyboardAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context,resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView keyboardItem;
        if(convertView == null){
            keyboardItem = new TextView(getContext());
        }else{
            keyboardItem = (TextView) convertView;
        }
        keyboardItem.setGravity(Gravity.CENTER);
        keyboardItem.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        keyboardItem.setHeight(parent.getHeight()/4);
        keyboardItem.setTextColor(getContext().getResources().getColor(R.color.colorPrimaryDark));

        if (getContext().getResources().getStringArray(R.array.keyboard)[position].equalsIgnoreCase("DEL")){
            //add clear icon
            keyboardItem.setText(getItem(position));

        }else {
            keyboardItem.setText(getItem(position));
        }
        return keyboardItem;
    }
}
