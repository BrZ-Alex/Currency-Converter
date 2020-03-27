package com.brz.converter.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_del);
            bitmap = Bitmap.createScaledBitmap(bitmap, parent.getWidth()/3, parent.getHeight()/4, false);
            Drawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            keyboardItem.setBackground(drawable);

        }else {
            keyboardItem.setBackgroundColor(Color.WHITE);
            keyboardItem.setText(getItem(position));
        }
        return keyboardItem;
    }
}
